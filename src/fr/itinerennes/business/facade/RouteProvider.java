package fr.itinerennes.business.facade;

import java.util.List;

import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusRoute;
import fr.itinerennes.model.Station;

/**
 * Providers implementing this interface will be able to serve requests for routes.
 * 
 * @author Olivier Boudet
 */
public interface RouteProvider {

    /**
     * Gets a route by its identifier.
     * 
     * @param id
     *            the identifier of the route
     * @return the requested route
     * @throws GenericException
     *             unable to retrieve the requested route
     */
    Station getRoute(String id) throws GenericException;

    /**
     * Gets all routes of a station.
     * 
     * @param id
     *            the identifier of the station
     * @return the list of routes
     * @throws GenericException
     *             unable to retrieve routes
     */
    List<BusRoute> getStationRoutes(String stationId) throws GenericException;

    /**
     * Releases all needed connections.
     */
    void release();
}
