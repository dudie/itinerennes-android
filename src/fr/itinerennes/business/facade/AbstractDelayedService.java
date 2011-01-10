package fr.itinerennes.business.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Cacheable;
import fr.itinerennes.utils.Utils;

/**
 * An abstract cache implementing common functions to avoid repetitive calls to the same method
 * between a specified time interval.
 * <p>
 * Typical use is avoiding calls to {@link KeolisService#getAllLineIcons()} as we know the result
 * will not change after 5 minutes.
 * 
 * @param <T>
 *            the type of data intercepted
 * @author Jérémie Huchet
 */
public abstract class AbstractDelayedService<T extends Cacheable> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(AbstractDelayedService.class);

    /** The cache for bike stations. */
    private final CacheProvider<T> cache;

    /** The minimum time between two calls. */
    private final int minTimeBetweenCalls;

    /** The last time the call occurred. */
    private long lastRefresh = 0;

    /**
     * Creates the call delayer.
     * 
     * @param cache
     *            the cache to use to store the data
     * @param minTimeBetweenCalls
     *            the minimum time between two calls
     */
    public AbstractDelayedService(final CacheProvider<T> cache, final int minTimeBetweenCalls) {

        this.minTimeBetweenCalls = minTimeBetweenCalls;
        this.cache = cache;
    }

    /**
     * Gets the cached entry identified by the given key.
     * <p>
     * The network service is used only if the minimum time between two calls has expired.
     * <ul>
     * <li>if min time between 2 calls haven't expired : use the cache to get stations ;</li>
     * <li>else
     * <ol>
     * <li>retrieve all stations</li>
     * <li>store (or update) them in the cache</li>
     * <li>update last fetch time</li>
     * <li>use the cache to load only the bike stations located in the given bounding box.</li>
     * </ol>
     * </li>
     * </ul>
     * 
     * @param id
     *            the key identifying the resource to retrieve
     * @return the requested resource, or null if no resource was found
     * @throws GenericException
     *             an error occured while retrieving the resource
     */
    public final T getResource(final String id) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getResource.start - id={}", id);
        }

        if (Utils.getCurrentTimeSeconds() - lastRefresh > minTimeBetweenCalls) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("last refresh date expired : {}, trying to refresh data", lastRefresh);
            }
            final List<T> allResources = getAll();
            cache.replace(allResources);
            lastRefresh = System.currentTimeMillis();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("setting new refresh date : {}", lastRefresh);
            }
        }

        final T resource = cache.load(id);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getResource.end - resourceNotNull={}", null != resource);
        }
        return resource;
    }

    /**
     * Must be implemented by subclasses to define how to retrieve all resources.
     * <p>
     * Implementations should make the (expensive) call and return the result.
     * 
     * @return the result of the method which is expensive to use
     * @throws GenericException
     *             an error occured while retrieving the resource
     */
    protected abstract List<T> getAll() throws GenericException;

    /**
     * Gets the cache.
     * 
     * @return the cache
     */
    public final CacheProvider<T> getCache() {

        return cache;
    }

}
