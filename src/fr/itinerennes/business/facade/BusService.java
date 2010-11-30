package fr.itinerennes.business.facade;

import java.util.List;

import fr.itinerennes.beans.BoundingBox;
import fr.itinerennes.beans.BusStation;
import fr.itinerennes.business.RemoteDataCacheProvider;
import fr.itinerennes.business.service.WFSService;
import fr.itinerennes.exceptions.GenericException;

/**
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class BusService {

    /** The WFS service. */
    private static final WFSService wfsService = WFSService.getInstance();

    /**
     * Gets a bus station by its identifier.
     * 
     * @param id
     *            the identifier of the station
     * @return the requested station
     * @throws GenericException
     *             unable to retrieve the requested station
     */
    public static BusStation getStation(final String id) throws GenericException {

        BusStation station = RemoteDataCacheProvider.get(BusStation.class, id);
        if (station == null) {
            station = wfsService.getBusStation(id);
            RemoteDataCacheProvider.put(id, station);
        }
        return station;
    }

    /**
     * Gets all bus stations within a bounding box.
     * 
     * @param bbox
     *            The bounding box from which request stations.
     * @return the requested stations
     * @throws GenericException
     *             unable to retrieve the stations
     */
    public static List<BusStation> getBusStationsFromBbox(final BoundingBox bbox)
            throws GenericException {

        final List<BusStation> allStations = wfsService.getBusStationsFromBbox(bbox);
        for (final BusStation station : allStations) {
            RemoteDataCacheProvider.put(station.getId(), station);
        }
        return allStations;
    }
}
