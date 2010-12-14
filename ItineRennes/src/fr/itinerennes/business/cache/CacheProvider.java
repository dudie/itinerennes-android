package fr.itinerennes.business.cache;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.itinerennes.database.Columns.MetadataColumns;
import fr.itinerennes.model.Cacheable;

/**
 * Manages a cache of data. Uses a database to store metadata information about saved entries.
 * <p>
 * Metadata contains :
 * <ul>
 * <li>a <b>type</b> (the fully qualified class name of the objet)</li>
 * <li>an <b>identifier</b>, must be unique fo the same type</li>
 * <li>a <b>last update date</b></li>
 * <li>an optionnal <b>URI</b> to a file where the value was stored</li>
 * </ul>
 * 
 * @param <T>
 * @author Jérémie Huchet
 */
public class CacheProvider<T extends Cacheable> implements MetadataColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(CacheProvider.class);

    /** The name of the metadata table. */
    private static final String METADATA_TABLE_NAME = "cache_metadata";

    /** SQL query to find a metadata cache entry : {@value #QUERY_METADATA}. */
    private static final String QUERY_METADATA = String.format(
            "SELECT * FROM %s WHERE %s = ? AND %s = ?", METADATA_TABLE_NAME, TYPE, ID);

    /** SQL query to insert a new metadata cache entry : {@value #INSERT_METADATA}. */
    // private static final String INSERT_METADATA = String.format(
    // "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", METADATA_TABLE_NAME, TYPE, ID,
    // LAST_UPDATE);

    /** SQL query to update an existing metadata cache entry : {@value #UPDATE_METADATA}. */
    // private static final String UPDATE_METADATA = String.format(
    // "UPDATE %s SET (%s = ?) WHERE %s = ? AND %s = ?", METADATA_TABLE_NAME, LAST_UPDATE,
    // TYPE, ID);

    /** The database used to store metadata. */
    private final SQLiteDatabase database;

    /** The cache entry handler used to save, update, load and delete cache entry values. */
    private final CacheEntryHandler<T> handler;

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
     *            the cache entry handler to use to store the values
     * @param ttl
     *            the "time to live" for the entries saved in this cache
     */
    public CacheProvider(final SQLiteDatabase database, final CacheEntryHandler<T> handler,
            final int ttl) {

        this.database = database;
        this.handler = handler;
        this.ttl = ttl;
        this.type = handler.getObjectClassName();
    }

    /**
     * Saves the given value to the cache. A metadata is inserted in the metadata table (
     * {@value CacheProvider#METADATA_TABLE_NAME} ) and
     * {@link CacheEntryHandler#replace(String, String, Object)} is called to store the value.
     * 
     * @param value
     *            the value to store
     */
    public final synchronized void replace(final T value) {

        final ContentValues metadata = new ContentValues(3);
        metadata.put(TYPE, value.getClass().getName());
        metadata.put(ID, value.getId());
        metadata.put(LAST_UPDATE, System.currentTimeMillis());

        database.replace(METADATA_TABLE_NAME, null, metadata);
        handler.replace(type, value.getId(), value);
    }

    /**
     * Updates the given value of the cache. The metadata will be updated in table (
     * {@value CacheProvider#METADATA_TABLE_NAME} ) and
     * {@link CacheEntryHandler#update(String, String, Object)} is called to store the value.
     * 
     * @param value
     *            the value to update
     */
    public final synchronized void update(final T value) {

        final ContentValues metadata = new ContentValues(1);
        metadata.put(LAST_UPDATE, System.currentTimeMillis());

        database.update(METADATA_TABLE_NAME, metadata, String.format("%s = ", ID),
                new String[] { value.getId() });
        handler.update(type, value.getId(), value);
    }

    /**
     * Search for a value in the cache identified by the given <code>id</code>.
     * 
     * @param id
     *            the identifier of the value
     * @return the cached value, or null if the result was outdated or if no result was found
     */
    public final T load(final String id) {

        final Cursor c = database.rawQuery(QUERY_METADATA, new String[] { type, id });
        T value = null;
        if (c.moveToFirst() && ttl > System.currentTimeMillis() - c.getLong(3)) {
            value = handler.load(type, id);
        }

        c.close();
        return value;
    }

    /**
     * Search for values localized in the given bounding box. This method has meaning only for
     * cached data having a geographical dimension.
     * 
     * @param bbox
     *            a bounding box
     * @return the values related to the given bounding box (in most cases, these should be values
     *         located in the given bounding box..) or null if nothing is found
     */
    public final List<T> load(final BoundingBoxE6 bbox) {

        return handler.load(type, bbox);
    }

    /**
     * Search for the availability of a value in the cache.
     * 
     * @param id
     *            the identifier of the value
     * @return true if an up to date value was found, else false
     */
    public final boolean contains(final String id) {

        final Cursor c = database.rawQuery(QUERY_METADATA, new String[] { type, id });
        return c.getCount() > 1;
    }

    /**
     * Releases all needed connections.
     */
    public void release() {

        this.database.close();
    }

}
