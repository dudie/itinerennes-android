package fr.itinerennes.business.http.oba;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.oba.Route;
import fr.itinerennes.model.oba.Stop;
import fr.itinerennes.model.oba.TripSchedule;
import fr.itinerennes.model.oba.TripStopTime;

/**
 * Handles responses for a call to the "trip-details" method of the OneBusAway API.
 * 
 * @author Olivier Boudet
 */
public class TripDetailsHttpResponseHandler extends HttpResponseHandler<TripSchedule> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(TripDetailsHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected final TripSchedule handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject data = OneBusAwayUtils.getServiceResponse(content);

        TripSchedule schedule = null;
        if (data != null) {
            schedule = new TripSchedule();

            try {
                final JSONObject entry = data.getJSONObject("entry");
                final Long serviceDate = entry.getLong("serviceDate");
                final JSONObject schedules = entry.getJSONObject("schedule");
                final JSONArray stopTimes = schedules.getJSONArray("stopTimes");

                final JSONObject references = data.getJSONObject("references");

                final HashMap<String, Route> routes = OneBusAwayUtils
                        .getReferencedRoutes(references.getJSONArray("routes"));

                final HashMap<String, Stop> stops = OneBusAwayUtils.getReferencedStops(
                        references.getJSONArray("stops"), routes);

                for (int i = 0; !stopTimes.isNull(i); i++) {
                    schedule.getStopTimes().add(
                            convertJsonObjectToStopTime(stopTimes.optJSONObject(i), serviceDate,
                                    stops));
                }

                schedule.setPreviousTripId(schedules.optString("previousTripId"));
                schedule.setNextTripId(schedules.optString("nextTripId"));

            } catch (final JSONException e) {
                LOGGER.error("OneBusAway API response malformed", e);
                throw new GenericException(ErrorCodeConstants.OBA_RESPONSE_ERROR,
                        "OneBusAway API response contains invalid data");
            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }
        return schedule;
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
    private TripStopTime convertJsonObjectToStopTime(final JSONObject jsonObject,
            final Long serviceDate, final HashMap<String, Stop> stops) {

        final TripStopTime stopTime = new TripStopTime();
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
