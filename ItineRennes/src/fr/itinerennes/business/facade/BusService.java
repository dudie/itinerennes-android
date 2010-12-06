package fr.itinerennes.business.facade;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;

import fr.itinerennes.beans.BusStation;
import fr.itinerennes.business.http.wfs.WFSService;
import fr.itinerennes.exceptions.GenericException;

/**
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class BusService implements StationProvider {

    /** The WFS service. */
    private static final WFSService wfsService = new WFSService();

    /**
     * Gets a bus station by its identifier.
     * 
     * @param id
     *            the identifier of the station
     * @return the requested station
     * @throws GenericException
     *             unable to retrieve the requested station
     */
    @Override
    public final BusStation getStation(final String id) throws GenericException {

        return wfsService.getBusStation(id);
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
    @SuppressWarnings("unchecked")
    @Override
    public List<BusStation> getStations(final BoundingBoxE6 bbox) throws GenericException {

        return wfsService.getBusStationsFromBbox(bbox);
    }
}
