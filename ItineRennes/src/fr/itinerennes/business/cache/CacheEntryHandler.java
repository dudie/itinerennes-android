package fr.itinerennes.business.cache;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;

import android.database.sqlite.SQLiteDatabase;

/**
 * Implementing this interface defines how a {@link CacheProvider} will handle the values it has to
 * cache.
 * <p>
 * <code>type</code> and <code>id</code> are the metadata keys the {@link CacheProvider} uses to
 * identify the value.
 * 
 * @param <T>
 *            the type this handler can manage
 * @author Jérémie Huchet
 */
public interface CacheEntryHandler<T> {

    /**
     * Tells the handler to save or replace the given value.
     * 
     * @param type
     *            the metadata type
     * @param id
     *            the metadata identifier
     * @param value
     *            the value to save
     * @param database
     *            the database
     */
    void replace(String type, String id, T value, SQLiteDatabase database);

    /**
     * Tells the handler to delete the given value.
     * 
     * @param type
     *            the metadata type
     * @param id
     *            the metadata identifier
     * @param database
     *            the database
     */
    void delete(String type, String id, SQLiteDatabase database);

    /**
     * Ask the handler to retrieve the cached value for the given type/identifier.
     * 
     * @param type
     *            the metadata type
     * @param id
     *            the metadata identifier
     * @param database
     *            the database
     * @return the value
     */
    T load(String type, String id, SQLiteDatabase database);

    /**
     * Ask the handler to retrieve the cached value of the given type located in the given bouding
     * box.
     * 
     * @param type
     *            the metadata type
     * @param bbox
     *            a bounding box
     * @param database
     *            the database
     * @return a list of values located in the given bounding box
     */
    List<T> load(String type, BoundingBoxE6 bbox, SQLiteDatabase database);

    /**
     * Ask the handler to get the class of cached entries.
     * 
     * @return the handled class
     */
    Class<T> getHandledClass();
}
