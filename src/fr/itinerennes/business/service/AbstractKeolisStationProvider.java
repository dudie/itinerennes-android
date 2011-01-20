package fr.itinerennes.business.service;

import java.util.Iterator;
import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.CacheProvider.CacheEntry;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Station;
import fr.itinerennes.utils.DateUtils;

/**
 * Manages commons functionalities offered by {@link StationProvider}s using {@link KeolisService}.
 * 
 * @param <T>
 *            the type of stations returned by the service
 * @author Jérémie Huchet
 */
public abstract class AbstractKeolisStationProvider<T extends Station> extends AbstractService
        implements StationProvider<T> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(AbstractKeolisStationProvider.class);

    /**
     * The cache of stations (this reference points to the same instance the
     * <code>delayedService</code> uses).
     */
    private final CacheProvider<T> cache;

    /** The last time all the cache was updated (in seconds). */
    private long lastGlobalUpdate = 0;

    /**
     * Creates the service.
     * 
     * @param dbHelper
     *            the database helper
     * @param cache
     *            a cache provider to handle stations caching
     */
    public AbstractKeolisStationProvider(final DatabaseHelper dbHelper, final CacheProvider<T> cache) {

        super(dbHelper);
        this.cache = cache;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.service.StationProvider#getStation(java.lang.String)
     */
    @Override
    public final T getStation(final String id) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStation.start - id={}", id);
        }

        T station = null;
        final CacheEntry<T> cachedStation = cache.load(id);
        if (DateUtils.isExpired(cachedStation.getLastUpdate(), Station.TTL)) {
            station = retrieveFreshStation(id);
            cache.replace(station);
        } else {
            station = cachedStation.getValue();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStation.end - stationNotNull=%s", station != null);
        }
        return station;
    }

    /**
     * Gets a station by its identifier. Unlike {@link #getStation(String)}, this method ensures the
     * result is up to date.
     * 
     * @param id
     *            the station identifier
     * @return the up to date station
     * @throws GenericException
     *             an error occurred
     */
    public final T getFreshStation(final String id) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getFreshStation.start - id={}", id);
        }

        T station = null;
        final CacheEntry<T> cachedStation = cache.load(id);
        if (DateUtils.isExpired(cachedStation.getLastUpdate(),
                ItineRennesConstants.KEOLIS_INSTANT_UPDATE_TIME)) {
            station = retrieveFreshStation(id);
            cache.replace(station);
        } else {
            station = cachedStation.getValue();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getFreshStation.end - stationNotNull=%s", station != null);
        }
        return station;
    }

    /**
     * Gets an up to date station from the keolis service.
     * 
     * @param id
     *            the identifier of the station
     * @return the up to date station
     * @throws GenericException
     *             an error occurred
     */
    protected abstract T retrieveFreshStation(final String id) throws GenericException;

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.service.StationProvider#getStations(org.andnav.osm.util.BoundingBoxE6)
     */
    @Override
    public final List<T> getStations(final BoundingBoxE6 bbox) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStations.start - bbox={}", bbox);
        }

        List<T> stations = null;

        // load cached data
        final List<CacheEntry<T>> cachedStations = cache.load(bbox);

        // if last global update call was a long time ago
        // force update if there is no cached result
        // or check expire dates and update if necessary
        boolean doUpdate = false;
        if (DateUtils.isExpired(lastGlobalUpdate,
                ItineRennesConstants.MIN_TIME_BETWEEN_KEOLIS_GET_ALL_CALLS)) {
            if (null == cachedStations || cachedStations.isEmpty()) {
                doUpdate = true;
            } else {
                for (final CacheEntry<T> entry : cachedStations) {
                    if (DateUtils.isExpired(entry.getLastUpdate(), entry.getValue().TTL)) {
                        doUpdate = true;
                    }
                }
            }
        }

        if (doUpdate) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("starting cache global update");
            }
            stations = retrieveAllStations();
            cache.replace(stations);
            lastGlobalUpdate = DateUtils.currentTimeSeconds();

            // remove stations out of the bbox
            final Iterator<T> i = stations.iterator();
            while (i.hasNext()) {
                final T station = i.next();
                if (station.getGeoPoint().getLongitudeE6() < bbox.getLonWestE6()
                        || station.getGeoPoint().getLongitudeE6() > bbox.getLonEastE6()
                        || station.getGeoPoint().getLatitudeE6() < bbox.getLatSouthE6()
                        || station.getGeoPoint().getLatitudeE6() > bbox.getLatNorthE6()) {
                    i.remove();
                }
            }
        } else {
            stations = CacheEntry.values(cachedStations);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStations.end - {} stations", stations == null ? 0 : stations.size());
        }

        return stations;
    }

    protected abstract List<T> retrieveAllStations() throws GenericException;
}
