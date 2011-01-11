package fr.itinerennes.business.facade;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;

import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Station;

/**
 * Providers implementing this interface will be able to serve requests for stations.
 * 
 * @param <T>
 *            the type of station handled by the provider
 * @author Jérémie Huchet
 */
public interface StationProvider<T extends Station> {

    /**
     * Gets a station by its identifier.
     * 
     * @param id
     *            the identifier of the station
     * @return the requested station
     * @throws GenericException
     *             unable to retrieve the requested station
     */
    T getStation(String id) throws GenericException;

    /**
     * Gets stations located in the given bounding box.
     * 
     * @param bbox
     *            stations located in this bounding box will be returned
     * @return the stations which are located in the given bounding box
     * @throws GenericException
     *             unable to retrieve the requested station
     */
    List<T> getStations(final BoundingBoxE6 bbox) throws GenericException;
}
