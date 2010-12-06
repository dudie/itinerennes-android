package fr.itinerennes.business.facade;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;

import fr.itinerennes.beans.Station;
import fr.itinerennes.exceptions.GenericException;

/**
 * Providers implementing this interface will be able to serve requests for stations.
 * 
 * @author Jérémie Huchet
 */
public interface StationProvider {

    /**
     * Gets a station by its identifier.
     * 
     * @param id
     *            the identifier of the station
     * @return the requested station
     * @throws GenericException
     *             unable to retrieve the requested station
     */
    Station getStation(String id) throws GenericException;

    /**
     * Gets a bike station by its identifier.
     * 
     * @param <T>
     *            a subclass of {@link Station}
     * @param bbox
     *            stations located in this bounding box will be returned
     * @return the stations which are located in the given bounding box
     * @throws GenericException
     *             unable to retrieve the requested station
     */
    <T extends Station> List<T> getStations(final BoundingBoxE6 bbox) throws GenericException;
}
