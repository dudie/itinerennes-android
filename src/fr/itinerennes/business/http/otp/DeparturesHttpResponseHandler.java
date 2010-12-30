package fr.itinerennes.business.http.otp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusDeparture;

/**
 * Handles http responses containing departures info from OTP in json format.
 * 
 * <pre>
 * "departures": [{
 *  "route":{
 *      "shortName":"8",
 *      "longName":"Ligne 8",
 *      "mode":"BUS",
 *      "agencyId":"1",
 *      "routeId":"08"
 *      },
 *      "headsign":"8 St GrÃ©goire Ricocquais",
 *      "date":"2010-12-19T17:01:00+01:00"
 *  }, {
 *  "route":{
 *      "shortName":"8",
 *      "longName":"Ligne 8",
 *      "mode":"BUS",
 *      "agencyId":"1",
 *      "routeId":"08"
 *      },
 *      "headsign":"8 St GrÃ©goire Ricocquais",
 *      "date":"2010-12-19T17:46:00+01:00"
 *  },{
 *  "route":{
 *      "shortName":"8",
 *      "longName":"Ligne 8",
 *      "mode":"BUS",
 *      "agencyId":"1",
 *      "routeId":"08"
 *      },
 *      "headsign":"8 St GrÃ©goire Ricocquais",
 *      "date":"2010-12-19T18:31:00+01:00"
 *  }]
 * </pre>
 * 
 * @author Olivier Boudet
 */
public final class DeparturesHttpResponseHandler extends HttpResponseHandler<List<BusDeparture>> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(DeparturesHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected List<BusDeparture> handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject data = OTPUtils.getServiceResponse(content);
        List<BusDeparture> departures = null;

        if (null != data) {
            final JSONObject jsonStop = data.optJSONObject("stop");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("response contains 1 stop");
            }

            try {
                if (null != jsonStop) {
                    // try to handle the response as if there is one departure
                    final JSONObject jsonDeparture = jsonStop.optJSONObject("departures");
                    if (null != jsonDeparture) {
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("response contains 1 departure");
                        }
                        departures = new ArrayList<BusDeparture>(1);
                        departures.add(convertJsonObjectToDeparture(jsonDeparture));
                    } else {

                        JSONArray jsonDepartures;

                        jsonDepartures = jsonStop.getJSONArray("departures");

                        departures = new ArrayList<BusDeparture>(jsonDepartures.length());

                        for (int i = 0; !jsonDepartures.isNull(i); i++) {
                            departures.add(convertJsonObjectToDeparture(jsonDepartures
                                    .getJSONObject(i)));
                        }
                    }
                }
            } catch (final JSONException e) {
                final String message = "a departure can't be converted";
                LOGGER.error(message, e);
                throw new GenericException(ErrorCodeConstants.JSON_MALFORMED, message, e);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }

        return departures;
    }

    /**
     * Converts a json object to a bean representing a departure.
     * 
     * @param jsonObject
     *            the json object to convert to a departure
     * @return the bus departure bean
     * @throws JSONException
     */
    private BusDeparture convertJsonObjectToDeparture(final JSONObject jsonObject)
            throws JSONException {

        final BusDeparture departure = new BusDeparture();

        departure.setRouteId(jsonObject.getJSONObject("route").optString("routeId"));
        departure.setRouteShortName(jsonObject.getJSONObject("route").optString("shortName"));
        departure.setHeadsign(jsonObject.optString("headsign"));
        departure.setDepartureDate(OTPUtils.convertJsonStringToDate(jsonObject.optString("date")));

        return departure;
    }
}
