package fr.itinerennes.business.service;

import java.util.List;

import fr.itinerennes.business.cache.BusRouteCacheEntryHandler;
import fr.itinerennes.business.cache.CacheRelationProvider;
import fr.itinerennes.business.http.otp.OTPService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusRoute;
import fr.itinerennes.model.Station;

/**
 * Service to consult informations about the bus routes service.
 * <p>
 * Every method call is cached using the {@link CacheRelationProvider}.
 * 
 * @author Olivier Boudet
 */
public class BusRouteService extends AbstractService implements RouteProvider {

    /** The OTP service. */
    private final OTPService otpService;

    /** The cache for bus route. */
    private final CacheRelationProvider<BusRoute> routeCache;

    /**
     * Creates an OTP service.
     * 
     * @param dbHelper
     *            the database helper
     */
    public BusRouteService(final DatabaseHelper dbHelper) {

        super(dbHelper);
        otpService = new OTPService();
        routeCache = new CacheRelationProvider<BusRoute>(dbHelper, new BusRouteCacheEntryHandler(),
                BusRoute.TTL);
    }

    @Override
    public Station getRoute(final String id) throws GenericException {

        // TOBO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.service.RouteProvider#getStationRoutes(java.lang.String)
     */
    @Override
    public List<BusRoute> getStationRoutes(final String stationId) throws GenericException {

        List<BusRoute> routes = routeCache.load(stationId);
        if (routes == null) {
            routes = otpService.getStopRoutes(stationId);
            for (final BusRoute route : routes) {
                routeCache.replace(route, stationId);
            }
        }
        return routes;
    }

    @Override
    public void release() {

        // TOBO Auto-generated method stub

    }

}
