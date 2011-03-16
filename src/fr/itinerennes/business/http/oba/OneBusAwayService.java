package fr.itinerennes.business.http.oba;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.http.GenericHttpService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.model.oba.ArrivalAndDeparture;
import fr.itinerennes.model.oba.Stop;
import fr.itinerennes.model.oba.StopSchedule;
import fr.itinerennes.model.oba.TripSchedule;

/**
 * Manage calls to the OneBusAway API.
 * 
 * @author Jérémie Huchet
 */
public class OneBusAwayService {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(OneBusAwayService.class);

    /** Parameter used by OneBusAway API to include references or not. */
    private static final String OBA_INCLUDE_REFERENCE = "includeReferences";

    /** Date format for OBA requests. */
    private final SimpleDateFormat obaSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /** The HTTP client. */
    private final GenericHttpService httpService = new GenericHttpService();

    /**
     * Creates a generic request to the OneBusAway API. This method will set the request headers.
     * 
     * @param path
     *            the url where to make the call
     * @param parameters
     *            the request parameters
     * @return an {@link HttpGet} to send to execute the request
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
     * @param direction
     *            direction of the route to get stops
     * @return the list of stations for the given route
     * @throws GenericException
     *             an error occurred
     */
    public final List<BusStation> getStopsForRoute(final String routeId, final String direction)
            throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("stopsForRoute.start - routeId={}", routeId);
        }
        final String urlCall = String.format(ItineRennesConstants.OBA_API_URL, "stops-for-route",
                routeId);

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(2);
        params.add(new BasicNameValuePair(OBA_INCLUDE_REFERENCE, "false"));

        final HttpGet request = createOBARequest(urlCall, params);

        // final List<BusStation> stops = httpService.execute(request,
        // new StopsForRouteHttpResponseHandler(direction));
        final List<BusStation> stops = null;

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
    public final TripSchedule getTripDetails(final String tripId) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getTripDetails.start - tripId={}", tripId);
        }
        final String urlCall = String.format(ItineRennesConstants.OBA_API_URL, "trip-details",
                tripId);

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(2);
        params.add(new BasicNameValuePair(OBA_INCLUDE_REFERENCE, "true"));

        final HttpGet request = createOBARequest(urlCall, params);

        final TripSchedule schedule = httpService.execute(request,
                new TripDetailsHttpResponseHandler());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getTripDetails.end");
        }
        return schedule;
    }

    /**
     * Gets arrivals and departures for stop.
     * 
     * @param stopId
     *            id of the stop to get arrivals and departures
     * @return the list of arrivals and departures
     * @throws GenericException
     */
    public final List<ArrivalAndDeparture> getArrivalsAndDeparturesForStop(final String stopId)
            throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getArrivalsAndDeparturesForStop.start - stopId={}", stopId);
        }
        final String urlCall = String.format(ItineRennesConstants.OBA_API_URL,
                "arrivals-and-departures-for-stop", stopId);

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(2);
        params.add(new BasicNameValuePair(OBA_INCLUDE_REFERENCE, "false"));

        final HttpGet request = createOBARequest(urlCall, params);

        final List<ArrivalAndDeparture> arrivalsAndDepartures = httpService.execute(request,
                new ArrivalsAndDeparturesForStopHttpResponseHandler());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getArrivalsAndDeparturesForStop.end");
        }
        return arrivalsAndDepartures;
    }

    /**
     * Gets schedule for a stop.
     * 
     * @param stopId
     *            id of the stop to get schedule
     * @return the full schedule
     * @throws GenericException
     */
    public final StopSchedule getScheduleForStop(final String stopId, final Date date)
            throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getScheduleForStop.start - stopId={}", stopId);
        }
        final String urlCall = String.format(ItineRennesConstants.OBA_API_URL, "schedule-for-stop",
                stopId);

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(2);
        params.add(new BasicNameValuePair(OBA_INCLUDE_REFERENCE, "true"));
        params.add(new BasicNameValuePair("date", obaSimpleDateFormat.format(date)));

        final HttpGet request = createOBARequest(urlCall, params);

        final StopSchedule schedule = httpService.execute(request,
                new ScheduleForStopHttpResponseHandler());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getScheduleForStop.end");
        }
        return schedule;
    }

    /**
     * Gets schedule for a stop.
     * 
     * @param stopId
     *            id of the stop to retrieve
     * @return the stop with routes
     * @throws GenericException
     */
    public final Stop getStop(final String stopId) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStop.start - stopId={}", stopId);
        }
        final String urlCall = String.format(ItineRennesConstants.OBA_API_URL, "stop", stopId);

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(1);
        params.add(new BasicNameValuePair(OBA_INCLUDE_REFERENCE, "true"));

        final HttpGet request = createOBARequest(urlCall, params);

        final Stop stop = httpService.execute(request, new StopHttpResponseHandler());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStop.end");
        }
        return stop;
    }
}
