package fr.itinerennes.business.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.business.facade.AbstractService;
import fr.itinerennes.database.Columns.MetadataColumns;
import fr.itinerennes.database.DatabaseHelper;
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
public class CacheProvider<T extends Cacheable> extends AbstractService implements MetadataColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(CacheProvider.class);

    /** The name of the metadata table. */
    private static final String METADATA_TABLE_NAME = "cache_metadata";

    /** SQL query to find a metadata cache entry : {@value #QUERY_METADATA}. */
    private static final String QUERY_METADATA = String
            .format("SELECT %s FROM %s WHERE %s = ? AND %s = ?", LAST_UPDATE, METADATA_TABLE_NAME,
                    TYPE, ID);

    /** SQL query to insert a new metadata cache entry : {@value #INSERT_METADATA}. */
    // private static final String INSERT_METADATA = String.format(
    // "INSERT INTO %s (%s, %s, %s) VALUES (?, ?, ?)", METADATA_TABLE_NAME, TYPE, ID,
    // LAST_UPDATE);

    /** SQL query to update an existing metadata cache entry : {@value #UPDATE_METADATA}. */
    // private static final String UPDATE_METADATA = String.format(
    // "UPDATE %s SET (%s = ?) WHERE %s = ? AND %s = ?", METADATA_TABLE_NAME, LAST_UPDATE,
    // TYPE, ID);

    /** The cache entry handler used to save, update, load and delete cache entry values. */
    private final CacheEntryHandler<T> handler;

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
     *            the cache entry handler to use to store the values
     */
    public CacheProvider(final DatabaseHelper dbHelper, final CacheEntryHandler<T> handler) {

        super(dbHelper);
        this.handler = handler;
        this.type = handler.getHandledClass().getName();
    }

    /**
     * Represents an entry of the cache.
     * 
     * @author Jérémie Huchet
     * @param <T>
     *            the type of the value thsi entry represents.
     */
    public static class CacheEntry<V extends Cacheable> {

        /** The last update date of the entry. */
        private Date lastUpdate;

        /** The value. */
        private V value;

        /**
         * Gets the lastUpdate.
         * 
         * @return the lastUpdate
         */
        public final Date getLastUpdate() {

            return lastUpdate;
        }

        /**
         * Sets the lastUpdate.
         * 
         * @param lastUpdate
         *            the lastUpdate to set
         */
        private void setLastUpdate(final Date lastUpdate) {

            this.lastUpdate = lastUpdate;
        }

        /**
         * Gets the value.
         * 
         * @return the value
         */
        public final V getValue() {

            return value;
        }

        /**
         * Sets the value.
         * 
         * @param value
         *            the value to set
         */
        private void setValue(final V value) {

            this.value = value;
        }

        /**
         * Converts the given cache entries to a list of values.
         * 
         * @param <K>
         *            a Cacheable type
         * @param entries
         *            the cache entries to convert to a list of values
         * @return a list containing the values of the cache entries.
         */
        public static <K extends Cacheable> List<K> values(final List<CacheEntry<K>> entries) {

            final ArrayList<K> values = new ArrayList<K>(entries.size());
            for (final CacheEntry<K> entry : entries) {
                values.add(entry.getValue());
            }
            return values;
        }

    }

    /**
     * Saves or updates the given value to the cache. A metadata is inserted (or updated) in the
     * metadata table ( {@value CacheProvider#METADATA_TABLE_NAME} ) and
     * {@link CacheEntryHandler#replace(String, String, Object)} is called to store the value.
     * 
     * @param value
     *            the value to store
     */
    public final synchronized void replace(final T value) {

        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final ContentValues metadata = new ContentValues(3);
        metadata.put(TYPE, value.getClass().getName());
        metadata.put(ID, value.getId());
        metadata.put(LAST_UPDATE, System.currentTimeMillis());

        database.replace(METADATA_TABLE_NAME, null, metadata);
        handler.replace(type, value.getId(), value);
    }

    /**
     * Saves or updates the list of given values to the cache. A metadata is inserted (or updated)
     * for each value in the metadata table ( {@value CacheProvider#METADATA_TABLE_NAME} ) and
     * {@link CacheEntryHandler#replace(String, String, Object)} is called to store the value.
     * 
     * @param value
     *            the list of values to store
     */
    public final synchronized void replace(final List<T> values) {

        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.beginTransaction();
        try {
            for (final T value : values) {
                replace(value);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }

    }

    /**
     * Search for a value in the cache identified by the given <code>id</code>.
     * 
     * @param id
     *            the identifier of the value
     * @return the cached value, or null if the result was outdated or if no result was found
     */
    public final CacheEntry<T> load(final String id) {

        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor c = database.rawQuery(QUERY_METADATA, new String[] { type, id });
        CacheEntry<T> entry = null;

        if (c.moveToFirst()) {
            entry = new CacheEntry<T>();
            entry.setLastUpdate(new Date(c.getLong(0)));
            entry.setValue(handler.load(type, id));
        }

        c.close();
        return entry;
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
    public final List<CacheEntry<T>> load(final BoundingBoxE6 bbox) {

        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final List<T> values = handler.load(type, bbox);
        final List<CacheEntry<T>> entries = new ArrayList<CacheEntry<T>>(values == null ? 0
                : values.size());

        for (final T value : values) {

            final CacheEntry<T> entry = new CacheEntry<T>();
            final Cursor c = database
                    .rawQuery(QUERY_METADATA, new String[] { type, value.getId() });
            if (c.moveToFirst()) {
                entry.setLastUpdate(new Date(c.getLong(0)));
                entry.setValue(value);
            } else {
                LOGGER.warn("no metadata found for entry type={}, id={}", type, value.getId());
            }
            c.close();
            entries.add(entry);
        }
        return entries;
    }

    /**
     * Search for the availability of a value in the cache.
     * 
     * @param id
     *            the identifier of the value
     * @return true if an up to date value was found, else false
     */
    public final boolean contains(final String id) {

        final SQLiteDatabase database = dbHelper.getReadableDatabase();
        final Cursor c = database.rawQuery(QUERY_METADATA, new String[] { type, id });
        final boolean contains = c.getCount() > 0;
        c.close();
        return contains;
    }

}
