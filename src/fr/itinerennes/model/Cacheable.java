package fr.itinerennes.model;

import fr.itinerennes.business.cache.CacheProvider;

/**
 * Classes implementing this interface may be stored using a {@link CacheProvider}.
 * 
 * @author Jérémie Huchet
 */
public interface Cacheable {

    /** Default cache lifetime : {@value #TTL} seconds. */
    int TTL = 3600;

    /**
     * Gets the identifier of this bean.
     * 
     * @return the identifier of this bean
     */
    String getId();
}
