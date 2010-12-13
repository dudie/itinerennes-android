package fr.itinerennes.business.facade;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;

import android.database.sqlite.SQLiteDatabase;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.beans.BusStation;
import fr.itinerennes.business.cache.BusStationCacheEntryHandler;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.GeoCacheProvider;
import fr.itinerennes.business.http.wfs.WFSService;
import fr.itinerennes.exceptions.GenericException;

/**
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class BusService implements StationProvider {

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
    public BusStation getStation(final String id) throws GenericException {

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
    public List<BusStation> getStations(final BoundingBoxE6 bbox) throws GenericException {

        if (geoCache.isExplored(bbox, BusStation.class.getName())) {
            return busCache.load(bbox);
        } else {
            final List<BusStation> stations = wfsService.getBusStationsFromBbox(bbox);

            for (final BusStation station : stations) {
                busCache.replace(station);
            }

            geoCache.markExplored(bbox, BusStation.class.getName());
            return stations;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#release()
     */
    @Override
    public void release() {

        busCache.release();

    }

}
