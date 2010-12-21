package fr.itinerennes.business.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.MetadataColumns;
import fr.itinerennes.model.Cacheable;

public class CacheRelationProvider<T extends Cacheable> implements MetadataColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(CacheRelationProvider.class);

    /** The name of the metadata table. */
    private static final String METADATA_TABLE_NAME = "cache_metadata";

    /** SQL query to find a metadata cache entry : {@value #QUERY_METADATA}. */
    private static final String QUERY_METADATA = String.format(
            "SELECT * FROM %s WHERE %s = ? AND %s = ?", METADATA_TABLE_NAME, TYPE, ID);

    /** The database used to store metadata. */
    private final SQLiteDatabase database;

    /** The cache entry handler used to save, update, load and delete cache entry values. */
    private final CacheRelationEntryHandler<T> handler;

    /** The time to live for this type of cache entries. */
    private final int ttl;

    /**
     * A type to make unique the identifiers of the entries of this cache (use the cached class
     * name).
     */
    private final String type;

    /**
     * Creates the cache provider.
     * 
     * @param database
     *            the database
     * @param handler
     *            the cache relation entry handler to use to store the values
     * @param ttl
     *            the "time to live" for the entries saved in this cache
     */
    public CacheRelationProvider(final SQLiteDatabase database,
            final CacheRelationEntryHandler<T> handler, final int ttl) {

        this.database = database;
        this.handler = handler;
        this.ttl = ttl;

        this.type = handler.getHandledClass().getName();
    }

    /**
     * Saves or updates the given value to the cache. A metadata is inserted (or updated) in the
     * metadata table ( {@value CacheProvider#METADATA_TABLE_NAME} ) and
     * {@link CacheEntryHandler#replace(String, String, Object)} is called to store the value.
     * 
     * @param value
     *            the value to store
     */
    public final synchronized void replace(final T value, final String relationKey) {

        final ContentValues metadata = new ContentValues(3);
        metadata.put(TYPE, value.getClass().getName());
        metadata.put(ID, relationKey);
        metadata.put(LAST_UPDATE, System.currentTimeMillis());

        database.replace(METADATA_TABLE_NAME, null, metadata);

        handler.replace(type, value.getId(), value, relationKey);
    }

    /**
     * Search for values in the cache identified by the given relation key <code>id</code>.
     * 
     * @param relationKeyId
     *            the identifier of the relation key
     * @return the cached values, or null if the result was outdated or if no result was found
     */
    public final List<T> load(final String relationKeyId) {

        final Cursor c = database.rawQuery(QUERY_METADATA, new String[] { type, relationKeyId });
        List<T> values = null;
        if (c.moveToFirst() && ttl > (System.currentTimeMillis() - c.getLong(3)) / 1000) {
            values = handler.load(type, relationKeyId);
        }

        c.close();
        return values;
    }

    /**
     * Releases all needed connections.
     */
    public void release() {

        this.database.close();
    }
}
