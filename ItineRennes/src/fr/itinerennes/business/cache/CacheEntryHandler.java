package fr.itinerennes.business.cache;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;

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
     */
    void replace(String type, String id, T value);

    /**
     * Tells the handler to update the given value.
     * 
     * @param type
     *            the metadata type
     * @param id
     *            the metadata identifier
     * @param value
     *            the value to save
     */
    void update(String type, String id, T value);

    /**
     * Tells the handler to delete the given value.
     * 
     * @param type
     *            the metadata type
     * @param id
     *            the metadata identifier
     */
    void delete(String type, String id);

    /**
     * Ask the handler to retrieve the cached value for the given type/identifier.
     * 
     * @param type
     *            the metadata type
     * @param id
     *            the metadata identifier
     * @return the value
     */
    T load(String type, String id);

    /**
     * Ask the handler to retrieve the cached value of the given type located in the given bouding
     * box.
     * 
     * @param type
     *            the metadata type
     * @param bbox
     * @return
     */
    List<T> load(String type, BoundingBoxE6 bbox);

    /**
     * Ask the handler to get the class name of cached entries.
     * 
     * @return the name of the handled class
     */
    String getObjectClassName();
}
