package fr.itinerennes.business.facade;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.BusRouteCacheEntryHandler;
import fr.itinerennes.business.cache.CacheRelationProvider;
import fr.itinerennes.business.http.otp.OTPService;
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
public class BusRouteService implements RouteProvider {

    /** The OTP service. */
    private final OTPService otpService;

    /** The cache for bus route. */
    private final CacheRelationProvider<BusRoute> routeCache;

    /**
     * Creates an OTP service.
     * 
     * @param database
     *            the database
     */
    public BusRouteService(final SQLiteDatabase database) {

        otpService = new OTPService();
        routeCache = new CacheRelationProvider<BusRoute>(database, new BusRouteCacheEntryHandler(
                database), ItineRennesConstants.TTL_BUS_ROUTE);
    }

    @Override
    public Station getRoute(final String id) throws GenericException {

        // TOBO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.RouteProvider#getStationRoutes(java.lang.String)
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
