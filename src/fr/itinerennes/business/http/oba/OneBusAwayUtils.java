package fr.itinerennes.business.http.oba;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.oba.Route;
import fr.itinerennes.model.oba.Stop;

/**
 * @author Olivier Boudet
 */
public final class OneBusAwayUtils {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(OneBusAwayUtils.class);

    private final static int RESPONSE_CODE_SUCCESS = 200;

    /**
     * Constructor.
     */
    private OneBusAwayUtils() {

    }

    /**
     * Checks the response code of the OneBusAway API and return the JSON Object named "data".
     * 
     * @param reponse
     *            the reponse of the OneBusAway API
     * @return the JSON Object named "data"
     * @throws GenericException
     *             the response does not contains valid data
     */
    public static JSONObject getServiceResponse(final String reponse) throws GenericException {

        try {
            final JSONObject jsonResult = new JSONObject(reponse);

            final int code = jsonResult.getInt("code");

            if (code != RESPONSE_CODE_SUCCESS) {
                final String message = String.format("OneBusAway API error : code=%S, %s ", code,
                        jsonResult.getString("text"));
                LOGGER.error(message);
                throw new GenericException(ErrorCodeConstants.OBA_RESPONSE_ERROR, message);
            }
            return jsonResult.getJSONObject("data");
        } catch (final JSONException e) {
            LOGGER.error("OneBusAway API response malformed", e);
            throw new GenericException(ErrorCodeConstants.OBA_RESPONSE_ERROR,
                    "OneBusAway API response contains invalid data");
        }
    }

    /**
     * Convert a timestamp to a Date.
     * 
     * @param timestamp
     *            timestamp to convert
     * @return the date
     */
    public static Date dateFromTimestamp(final Long timestamp) {

        return dateFromTimestamps(timestamp, (long) 0);

    }

    /**
     * Add two timestamps and returns a Date.
     * 
     * @param timestamp1
     *            first timestamp
     * @param timestamp2
     *            second timestamp
     * @return the date
     */
    public static Date dateFromTimestamps(final Long timestamp1, final Long timestamp2) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp1 + timestamp2 * 1000);
        return calendar.getTime();

    }

    /**
     * Gets stops from the "references" part of OneBusAway response.
     * 
     * @param jsonArray
     *            jsonArray containing the references part of the responses.
     * @param routes
     *            list of routes @link {@link #getReferencedRoutes(JSONArray)}
     * @return the list of stops
     */
    public static HashMap<String, Stop> getReferencedStops(final JSONArray jsonArray,
            final HashMap<String, Route> routes) {

        HashMap<String, Stop> stops = null;

        if (jsonArray != null) {
            stops = new HashMap<String, Stop>();
            for (int i = 0; !jsonArray.isNull(i); i++) {
                final JSONObject jsonStop = jsonArray.optJSONObject(i);
                stops.put(jsonStop.optString("id"), convertJsonObjectToStop(jsonStop, routes));
            }
        }
        return stops;
    }

    /**
     * Gets routes from the "references" part of OneBusAway response.
     * 
     * @param jsonArray
     *            jsonArray containing the references part of the responses.
     * @return the list of routes
     */
    public static HashMap<String, Route> getReferencedRoutes(final JSONArray jsonArray) {

        HashMap<String, Route> routes = null;

        if (jsonArray != null) {
            routes = new HashMap<String, Route>();
            for (int i = 0; !jsonArray.isNull(i); i++) {
                final JSONObject jsonRoute = jsonArray.optJSONObject(i);
                routes.put(jsonRoute.optString("id"), convertJsonObjectToRoute(jsonRoute));
            }
        }
        return routes;
    }

    /**
     * Converts a JSONObject to a Stop object.
     * 
     * @param jsonStop
     *            JSONObject describing a Stop
     * @param routes
     *            list of routes @link {@link #getReferencedRoutes(JSONArray)}
     * @return the Stop object
     * @throws JSONException
     */
    public static Stop convertJsonObjectToStop(final JSONObject jsonStop,
            final HashMap<String, Route> routes) {

        final Stop stop = new Stop();
        stop.setId(jsonStop.optString("id"));
        stop.setLon(jsonStop.optDouble("lon"));
        stop.setLat(jsonStop.optDouble("lon"));
        stop.setDirection(jsonStop.optString("direction"));
        stop.setName(jsonStop.optString("name"));
        stop.setCode(jsonStop.optInt("code"));
        final JSONArray jsonRoutes = jsonStop.optJSONArray("routeIds");
        for (int i = 0; !jsonRoutes.isNull(i); i++) {
            stop.getRoutes().add(routes.get(jsonRoutes.optString(i)));
        }

        return stop;
    }

    /**
     * Converts a JSONObject to a route object.
     * 
     * @param jsonRoute
     *            JSONObject describing a Route
     * @return the Route object
     */
    public static Route convertJsonObjectToRoute(final JSONObject jsonRoute) {

        final Route route = new Route();
        route.setId(jsonRoute.optString("id"));
        route.setLongName(jsonRoute.optString("longName"));
        route.setShortName(jsonRoute.optString("shortName"));
        route.setTextColor(jsonRoute.optString("textColor"));
        route.setColor(jsonRoute.optString("color"));
        route.setAgencyId(jsonRoute.optString("agencyId"));
        route.setDescription(jsonRoute.optString("description"));
        route.setType(jsonRoute.optInt("type"));

        return route;
    }
}
