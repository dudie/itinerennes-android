package fr.itinerennes.business.http.otp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusRoute;

/**
 * Handles http responses containing stop info from OTP in json format.
 * 
 * <pre>
 * "stop": {
 *      "name":"Avenir",
 *      "stopId":"avenir1",
 *      "agencyId":"DefaultAgencyIdGoesHere",
 *      "routes":[{
 *          "shortName":"55",
 *          "longName":"Le Verger / Mordelles / Rennes",
 *          "mode":"BUS",
 *          "agencyId":"1",
 *          "routeId":"55-89"
 *      ]
 * }
 * </pre>
 * 
 * @author Olivier Boudet
 */
public final class StopRoutesHttpResponseHandler extends HttpResponseHandler<List<BusRoute>> {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(StopRoutesHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected List<BusRoute> handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject data = OTPUtils.getServiceResponse(content);
        List<BusRoute> stopRoutes = null;

        if (null != data) {
            final JSONObject jsonStop = data.optJSONObject("stop");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("response contains 1 stop");
            }

            try {
                if (null != jsonStop) {
                    // try to handle the response as if there is one route
                    final JSONObject jsonRoute = jsonStop.optJSONObject("routes");
                    if (null != jsonRoute) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("response contains 1 route");
                        }
                        stopRoutes = new ArrayList<BusRoute>(1);
                        stopRoutes.add(convertJsonObjectToRoute(jsonRoute));
                    } else {

                        JSONArray jsonRoutes;

                        jsonRoutes = jsonStop.getJSONArray("routes");

                        stopRoutes = new ArrayList<BusRoute>(jsonRoutes.length());

                        for (int i = 0; !jsonRoutes.isNull(i); i++) {
                            stopRoutes.add(convertJsonObjectToRoute(jsonRoutes.getJSONObject(i)));
                        }
                    }
                }
            } catch (final JSONException e) {
                final String message = "a station can't be converted";
                LOGGER.error(message, e);
                throw new GenericException(ErrorCodeConstants.JSON_MALFORMED, message, e);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }

        return stopRoutes;
    }

    /**
     * Converts a json object to a bean representing a stop.
     * 
     * @param jsonObject
     *            the json object to convert to a stop
     * @return the stop bean
     */
    private BusRoute convertJsonObjectToRoute(final JSONObject jsonObject) {

        final BusRoute route = new BusRoute();

        route.setId(jsonObject.optString("routeId"));
        route.setShortName(jsonObject.optString("shortName"));
        route.setLongName(jsonObject.optString("longName"));
        route.setAgencyId(jsonObject.optString("agencyId"));

        return route;
    }
}
