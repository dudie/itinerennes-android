package fr.itinerennes.business.cache;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.business.service.AbstractService;
import fr.itinerennes.database.Columns.MetadataColumns;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.model.Cacheable;

/**
 * Manages a cache of data. Uses a database to store metadata information about saved entries.
 * <p>
 * Metadata contains :
 * <ul>
 * <li>a <b>type</b> (the fully qualified class name of the objet)</li>
 * <li>an <b>identifier</b>, must be unique for the same type</li>
 * <li>a <b>last update date</b></li>
 * </ul>
 * 
 * @param <T>
 * @author Olivier Boudet
 */

public class CacheRelationProvider<T extends Cacheable> extends AbstractService implements
        MetadataColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(CacheRelationProvider.class);

    /** The name of the metadata table. */
    private static final String METADATA_TABLE_NAME = "cache_metadata";

    /** SQL query to find a metadata cache entry : {@value #QUERY_METADATA}. */
    private static final String QUERY_METADATA = String.format(
            "SELECT * FROM %s WHERE %s = ? AND %s = ?", METADATA_TABLE_NAME, TYPE, ID);

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
     * @param dbHelper
     *            the database helper
     * @param handler
     *            the cache relation entry handler to use to store the values
     * @param ttl
     *            the "time to live" for the entries saved in this cache
     */
    public CacheRelationProvider(final DatabaseHelper dbHelper,
            final CacheRelationEntryHandler<T> handler, final int ttl) {

        super(dbHelper);
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

        final SQLiteDatabase database = dbHelper.getWritableDatabase();

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

        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final Cursor c = database.rawQuery(QUERY_METADATA, new String[] { type, relationKeyId });
        List<T> values = null;
        if (c.moveToFirst() && ttl > (System.currentTimeMillis() - c.getLong(3)) / 1000) {
            values = handler.load(type, relationKeyId);
        }

        c.close();
        return values;
    }
}
