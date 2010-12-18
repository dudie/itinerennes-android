package fr.itinerennes.business.facade;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;

import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.BikeStationCacheEntryHandler;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.GeoCacheProvider;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BikeStation;

/**
 * Service to consult informations about the bike transport service.
 * <p>
 * Every method call is cached using the {@link CacheProvider} and {@link GeoCacheProvider}.
 * 
 * @author Jérémie Huchet
 */
public final class BikeService implements StationProvider {

    /** The Keolis service. */
    private final KeolisService keolisService;;

    /** The cache for bike stations. */
    private final CacheProvider<BikeStation> bikeCache;

    /** The geo cache. */
    private final GeoCacheProvider geoCache;

    /**
     * Creates a bike service.
     * 
     * @param database
     *            the database
     */
    public BikeService(final SQLiteDatabase database) {

        keolisService = new KeolisService();
        bikeCache = new CacheProvider<BikeStation>(database, new BikeStationCacheEntryHandler(
                database), ItineRennesConstants.TTL_BIKE_STATIONS);
        geoCache = GeoCacheProvider.getInstance(database);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#getStation(java.lang.String)
     */
    @Override
    public BikeStation getStation(final String id) throws GenericException {

        BikeStation station = bikeCache.load(id);
        if (station == null) {
            station = keolisService.getBikeStation(id);
            bikeCache.replace(station);
        }
        return station;
    }

    /**
     * {@inheritDoc}
     * <ul>
     * <li>if bounding box has already been explored : use the cache to get stations ;</li>
     * <li>else
     * <ol>
     * <li>retrieve all stations</li>
     * <li>store (or update) them in the cache</li>
     * <li>mark the whole world as explored</li>
     * <li>use the cache to load only the bike stations located in the given bounding box.</li>
     * </ol>
     * </li>
     * </ul>
     * 
     * @see fr.itinerennes.business.facade.StationProvider#getStations(org.andnav.osm.util.BoundingBoxE6)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<BikeStation> getStations(final BoundingBoxE6 bbox) throws GenericException {

        if (geoCache.isExplored(bbox, BikeStation.class.getName())) {
            return bikeCache.load(bbox);
        } else {
            final List<BikeStation> allStations = keolisService.getAllBikeStations();
            for (final BikeStation station : allStations) {
                bikeCache.replace(station);
            }
            // mark the whole world expored
            geoCache.markExplored(new BoundingBoxE6(180 * 1E6, 180 * 1E6, -180 * 1E6, -180 * 1E6),
                    BikeStation.class.getName());
            return bikeCache.load(bbox);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#release()
     */
    @Override
    public void release() {

        bikeCache.release();
        geoCache.release();

    }

}
