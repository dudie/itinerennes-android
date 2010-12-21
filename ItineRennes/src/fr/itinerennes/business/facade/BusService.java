package fr.itinerennes.business.facade;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.BusStationCacheEntryHandler;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.GeoCacheProvider;
import fr.itinerennes.business.http.wfs.WFSService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusStation;

/**
 * Service to consult informations about the bus transport service.
 * <p>
 * Every method call is cached using the {@link CacheProvider} and {@link GeoCacheProvider}.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class BusService implements StationProvider {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BusService.class);

    /** The cache for bus stations. */
    private final CacheProvider<BusStation> busCache;

    /** The geo cache. */
    private final GeoCacheProvider geoCache;

    /** The WFS service. */
    private final WFSService wfsService;

    /**
     * Creates a bike service.
     * 
     * @param database
     *            the database
     */
    public BusService(final SQLiteDatabase database) {

        wfsService = new WFSService();
        busCache = new CacheProvider<BusStation>(database,
                new BusStationCacheEntryHandler(database), ItineRennesConstants.TTL_BUS_STATIONS);
        geoCache = GeoCacheProvider.getInstance(database);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#getStation(java.lang.String)
     */
    @Override
    public final BusStation getStation(final String id) throws GenericException {

        BusStation station = busCache.load(id);
        if (station == null) {
            station = wfsService.getBusStation(id);
            busCache.replace(station);
        }
        return station;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#getStations(org.andnav.osm.util.BoundingBoxE6)
     */
    @SuppressWarnings("unchecked")
    @Override
    public final List<BusStation> getStations(final BoundingBoxE6 bbox) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStations.start - bbox={}", bbox);
        }

        final List<BusStation> stations;

        final BoundingBoxE6 normalizedBbox = GeoCacheProvider.normalize(bbox);
        if (geoCache.isExplored(normalizedBbox, BusStation.class.getName())) {
            stations = busCache.load(bbox);
        } else {
            stations = wfsService.getBusStationsFromBbox(normalizedBbox);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("caching {} bus stations", null != stations ? stations.size() : 0);
            }

            for (final BusStation station : stations) {
                busCache.replace(station);
            }

            geoCache.markExplored(normalizedBbox, BusStation.class.getName());
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStations.start - {} stations", null != stations ? stations.size() : 0);
        }
        return stations;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#release()
     */
    @Override
    public void release() {

        busCache.release();
        geoCache.release();
    }

}
