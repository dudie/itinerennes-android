package fr.itinerennes.business.facade;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.GeoCacheProvider;
import fr.itinerennes.business.cache.SubwayStationCacheEntryHandler;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BikeStation;
import fr.itinerennes.model.SubwayStation;

/**
 * Service to consult informations about the subway transport service.
 * <p>
 * Every method call is cached using the {@link CacheProvider} and {@link GeoCacheProvider}.
 * 
 * @author Jérémie Huchet
 */
public class SubwayService implements StationProvider {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(SubwayService.class);

    /** The Keolis service. */
    private final KeolisService keolisService;;

    /** The cache for bike stations. */
    private final CacheProvider<SubwayStation> subwayCache;

    /** The geo cache. */
    private final GeoCacheProvider geoCache;

    /**
     * Creates a bike service.
     * 
     * @param database
     *            the database
     */
    public SubwayService(final SQLiteDatabase database) {

        keolisService = new KeolisService();
        subwayCache = new CacheProvider<SubwayStation>(database,
                new SubwayStationCacheEntryHandler(database),
                ItineRennesConstants.TTL_SUBWAY_STATIONS);
        geoCache = GeoCacheProvider.getInstance(database);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#getStation(java.lang.String)
     */
    @Override
    public SubwayStation getStation(final String id) throws GenericException {

        SubwayStation station = subwayCache.load(id);
        if (station == null) {
            station = keolisService.getSubwayStation(id);
            subwayCache.replace(station);
        }
        return station;
    }

    /**
     * {@inheritDoc}
     * 
     * @see BikeService#getStations(BoundingBoxE6)
     * @see fr.itinerennes.business.facade.StationProvider#getStations(org.andnav.osm.util.BoundingBoxE6)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<SubwayStation> getStations(final BoundingBoxE6 bbox) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStations.start - bbox={}", bbox);
        }

        final List<SubwayStation> stations;

        if (geoCache.isExplored(bbox, BikeStation.class.getName())) {
            stations = subwayCache.load(bbox);
        } else {
            final List<SubwayStation> allStations = keolisService.getAllSubwayStations();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("caching {} subway stations", null != allStations ? allStations.size()
                        : 0);
            }

            for (final SubwayStation station : allStations) {
                subwayCache.replace(station);
            }
            // mark the whole world expored
            geoCache.markExplored(new BoundingBoxE6(180 * 1E6, 180 * 1E6, -180 * 1E6, -180 * 1E6),
                    BikeStation.class.getName());
            stations = subwayCache.load(bbox);
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

        subwayCache.release();
        geoCache.release();

    }
}
