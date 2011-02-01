package fr.itinerennes.business.cache;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

/**
 * * Implementing this interface defines how a {@link CacheRelationProvider} will handle the values
 * it has to cache.
 * <p>
 * <code>type</code> and <code>id</code> are the metadata keys the {@link CacheRelationProvider}
 * uses to identify the value.
 * 
 * @author Olivier Boudet
 * @param <T>
 *            the type this handler can manage
 */
public interface CacheRelationEntryHandler<T> {

    /**
     * Tells the handler to save or replace the given value.
     * 
     * @param type
     *            the metadata type
     * @param id
     *            the metadata identifier
     * @param value
     *            the value to save
     * @param relationKey
     *            identifier of the relation key
     * @param database
     *            the database
     */
    void replace(String type, String id, T value, String relationKey, SQLiteDatabase database);

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
    void delete(String type, String id, final SQLiteDatabase database);

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
    List<T> load(String type, String id, final SQLiteDatabase database);

    /**
     * Ask the handler to get the class of cached entries.
     * 
     * @return the handled class
     */
    Class<T> getHandledClass();
}
