package fr.itinerennes.business.http.oba;

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
import fr.itinerennes.model.oba.ArrivalAndDeparture;

/**
 * Handles responses for a call to the "arrivals-and-departures-for-stop" method of the OneBusAway
 * API.
 * 
 * @author Olivier Boudet
 */
public class ArrivalsAndDeparturesForStopHttpResponseHandler extends
        HttpResponseHandler<List<ArrivalAndDeparture>> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(ArrivalsAndDeparturesForStopHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected final List<ArrivalAndDeparture> handleContent(final String content)
            throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject data = OneBusAwayUtils.getServiceResponse(content);

        List<ArrivalAndDeparture> arrivalsAndDepartures = null;
        if (data != null) {
            arrivalsAndDepartures = new ArrayList<ArrivalAndDeparture>();

            try {
                final JSONObject jsonEntry = data.getJSONObject("entry");
                final JSONArray jsonArrivalsAndDepartures = jsonEntry
                        .getJSONArray("arrivalsAndDepartures");

                for (int i = 0; !jsonArrivalsAndDepartures.isNull(i); i++) {
                    arrivalsAndDepartures
                            .add(convertJsonObjectToArrivalAndDeparture(jsonArrivalsAndDepartures
                                    .optJSONObject(i)));
                }

            } catch (final JSONException e) {
                LOGGER.error("OneBusAway API response malformed", e);
                throw new GenericException(ErrorCodeConstants.OBA_RESPONSE_ERROR,
                        "OneBusAway API response contains invalid data");
            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }
        return arrivalsAndDepartures;
    }

    /**
     * Converts a JSON object to a ArrivalAndDeparture object.
     * 
     * @param jsonArrivalAndDeparture
     *            JSON Object describing an ArrivalAndDeparture.
     * @return the arrival and departure object
     */
    private ArrivalAndDeparture convertJsonObjectToArrivalAndDeparture(
            final JSONObject jsonArrivalAndDeparture) {

        final ArrivalAndDeparture arrivalAndDeparture = new ArrivalAndDeparture();
        arrivalAndDeparture.setDistanceFromStop(jsonArrivalAndDeparture
                .optDouble("distanceFromStop"));
        arrivalAndDeparture.setScheduledArrivalTime(OneBusAwayUtils
                .dateFromTimestamp(jsonArrivalAndDeparture.optLong("scheduledArrivalTime")));
        arrivalAndDeparture.setScheduledDepartureTime(OneBusAwayUtils
                .dateFromTimestamp(jsonArrivalAndDeparture.optLong("scheduledDepartureTime")));
        arrivalAndDeparture.setTripHeadsign(jsonArrivalAndDeparture.optString("tripHeadsign"));
        arrivalAndDeparture.setTripId(jsonArrivalAndDeparture.optString("tripId"));
        arrivalAndDeparture.setRouteId(jsonArrivalAndDeparture.optString("routeId"));
        arrivalAndDeparture.setRouteLongName(jsonArrivalAndDeparture.optString("routeLongName"));
        arrivalAndDeparture.setRouteShortName(jsonArrivalAndDeparture.optString("routeShortName"));

        arrivalAndDeparture.setServiceDate(OneBusAwayUtils
                .dateFromTimestamp(jsonArrivalAndDeparture.optLong("serviceDate")));
        arrivalAndDeparture.setStopSequence(jsonArrivalAndDeparture.optInt("stopSequence"));
        arrivalAndDeparture.setStopId(jsonArrivalAndDeparture.optString("stopId"));

        return arrivalAndDeparture;
    }
}
