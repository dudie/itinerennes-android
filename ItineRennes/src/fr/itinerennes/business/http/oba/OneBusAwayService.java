package fr.itinerennes.business.http.oba;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.http.GenericHttpService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.model.oba.Schedule;

/**
 * Manage calls to the OneBusAway API.
 * 
 * @author Jérémie Huchet
 */
public class OneBusAwayService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(OneBusAwayService.class);

    /** The HTTP client. */
    private final GenericHttpService httpService = new GenericHttpService();

    /**
     * Creates a generic request to the OneBusAway API. This method will set the request headers.
     * 
     * @param path
     *            the url where to make the call
     * @param parameters
     *            the request parameters
     * @return an {@link HttpPost} to send to execute the request
     * @throws GenericException
     *             unable to encode request parameters
     */
    private HttpGet createOBARequest(final String path, final List<BasicNameValuePair> parameters)
            throws GenericException {

        parameters.add(new BasicNameValuePair("version", ItineRennesConstants.OBA_API_VERSION));
        parameters.add(new BasicNameValuePair("key", ItineRennesConstants.OBA_API_KEY));

        final StringBuilder urlCall = new StringBuilder();
        urlCall.append(path).append("?");
        for (final BasicNameValuePair param : parameters) {
            urlCall.append("&").append(param.getName());
            urlCall.append("=").append(param.getValue());
        }

        final HttpGet req = new HttpGet(urlCall.toString());
        req.addHeader("Accept", "text/json");
        req.addHeader("Accept", "application/json");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("createOBARequest - {}", urlCall.toString());
        }

        return req;
    }

    /**
     * Gets the bus stations for the given route.
     * 
     * @param routeId
     *            the identifier of the route you wants the stops
     * @return the list of stations for the given route
     * @throws GenericException
     *             an error occurred
     */
    public List<BusStation> getStopsForRoute(final String routeId, final String direction)
            throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("stopsForRoute.start - routeId={}", routeId);
        }
        final String urlCall = String.format(ItineRennesConstants.OBA_API_URL, "stops-for-route",
                routeId);

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(2);
        final HttpGet request = createOBARequest(urlCall, params);

        final List<BusStation> stops = httpService.execute(request,
                new StopsForRouteHttpResponseHandler(direction));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("stopsForRoute.end");
        }
        return stops;
    }

    /**
     * Gets the trip details.
     * 
     * @param tripId
     *            the identifier of the trip
     * @return the schedule for the given trip
     * @throws GenericException
     *             an error occurred
     */
    public Schedule getTripDetails(final String tripId) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getTripDetails.start - routeId={}", tripId);
        }
        final String urlCall = String.format(ItineRennesConstants.OBA_API_URL, "trip-details",
                tripId);

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(2);
        final HttpGet request = createOBARequest(urlCall, params);

        final Schedule schedule = httpService
                .execute(request, new TripDetailsHttpResponseHandler());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("stopsForRoute.end");
        }
        return schedule;
    }

}
