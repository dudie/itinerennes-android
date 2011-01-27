package fr.itinerennes.business.http.oba;

import java.util.ArrayList;
import java.util.HashMap;
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
import fr.itinerennes.model.oba.Stop;
import fr.itinerennes.model.oba.StopTime;

/**
 * @author orgoz
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
    protected List<ArrivalAndDeparture> handleContent(final String content) throws GenericException {

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

    /**
     * Converts a json object to a bean representing a stop time.
     * 
     * @param jsonObject
     *            the json object to convert to a stop time
     * @param serviceDate
     *            the service date of the trip for this stop time
     * @param stops
     *            map with stops referenced in the response
     * @return the stop time bean
     */
    private StopTime convertJsonObjectToStopTime(final JSONObject jsonObject,
            final Long serviceDate, final HashMap<String, Stop> stops) {

        final StopTime stopTime = new StopTime();
        stopTime.setStop(stops.get(jsonObject.optString("stopId")));
        stopTime.setArrivalTime(OneBusAwayUtils.dateFromTimestamps(serviceDate,
                jsonObject.optLong("arrivalTime")));
        stopTime.setDepartureTime(OneBusAwayUtils.dateFromTimestamps(serviceDate,
                jsonObject.optLong("departureTime")));
        stopTime.setDistanceAlongTrip(jsonObject.optDouble("distanceAlongTrip"));
        stopTime.setStopHeadsign(jsonObject.optString("stopHeadsign"));

        return stopTime;
    }
}
