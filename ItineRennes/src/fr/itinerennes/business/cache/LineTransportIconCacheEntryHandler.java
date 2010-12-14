package fr.itinerennes.business.cache;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.model.LineTransportIcon;

/**
 * Handles save / update / load / delete for {@link LineTransportIcon}.
 * 
 * @author Jérémie Huchet
 */
public class LineTransportIconCacheEntryHandler implements
        CacheEntryHandler<LineTransportIconCacheEntryHandler> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(LineTransportIconCacheEntryHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#replace(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    @Override
    public void replace(final String type, final String id,
            final LineTransportIconCacheEntryHandler value) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("replace.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("replace.end");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#update(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    @Override
    public void update(final String type, final String id,
            final LineTransportIconCacheEntryHandler value) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("update.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("update.end");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#delete(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public void delete(final String type, final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("delete.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("delete.end");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#load(java.lang.String, java.lang.String)
     */
    @Override
    public LineTransportIconCacheEntryHandler load(final String type, final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.end");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#load(java.lang.String,
     *      org.andnav.osm.util.BoundingBoxE6)
     */
    @Override
    public List<LineTransportIconCacheEntryHandler> load(final String type, final BoundingBoxE6 bbox) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.end");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#getObjectClassName()
     */
    @Override
    public String getObjectClassName() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getObjectClassName.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getObjectClassName.end");
        }
        return null;
    }
}
