package fr.itinerennes.business.service;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;

import fr.itinerennes.exceptions.GenericException;

/**
 * Providers implementing this interface will be able to serve requests for stations.
 * 
 * @param <T>
 *            the type of marker handled by the provider
 * @author Jérémie Huchet
 */
public interface MarkerProvider<T> {

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
