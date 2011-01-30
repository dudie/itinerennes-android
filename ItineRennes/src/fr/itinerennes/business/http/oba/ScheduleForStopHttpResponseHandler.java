package fr.itinerennes.business.http.oba;

import java.util.Collections;
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
import fr.itinerennes.model.oba.ScheduleStopTime;
import fr.itinerennes.model.oba.Stop;
import fr.itinerennes.model.oba.StopSchedule;

/**
 * Handles responses for a call to the "schedule-for-stop" method of the OneBusAway API.
 * 
 * @author Olivier Boudet
 */
public class ScheduleForStopHttpResponseHandler extends HttpResponseHandler<StopSchedule> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(ScheduleForStopHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected StopSchedule handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject data = OneBusAwayUtils.getServiceResponse(content);

        StopSchedule stopSchedule = null;
        if (data != null) {
            stopSchedule = new StopSchedule();

            try {
                final JSONObject references = data.getJSONObject("references");
                final JSONObject jsonEntry = data.getJSONObject("entry");

                final HashMap<String, Route> routes = OneBusAwayUtils
                        .getReferencedRoutes(references.getJSONArray("routes"));

                final HashMap<String, Stop> stops = OneBusAwayUtils.getReferencedStops(
                        references.getJSONArray("stops"), routes);
                stopSchedule.setStop(stops.get(jsonEntry.optString("stopId")));

                final JSONArray jsonStopRouteSchedules = jsonEntry
                        .getJSONArray("stopRouteSchedules");

                for (int i = 0; !jsonStopRouteSchedules.isNull(i); i++) {
                    final JSONObject jsonStopRouteSchedule = jsonStopRouteSchedules
                            .optJSONObject(i);
                    final Route route = routes.get(jsonStopRouteSchedule.optString("routeId"));

                    final JSONArray jsonStopRouteDirectionSchedules = jsonStopRouteSchedule
                            .getJSONArray("stopRouteDirectionSchedules");

                    for (int j = 0; !jsonStopRouteDirectionSchedules.isNull(j); j++) {
                        final JSONObject jsonStopRouteDirectionSchedule = jsonStopRouteDirectionSchedules
                                .optJSONObject(j);
                        final String headSign = jsonStopRouteDirectionSchedule
                                .optString("tripHeadsign");

                        final JSONArray jsonScheduleStopTimes = jsonStopRouteDirectionSchedule
                                .getJSONArray("scheduleStopTimes");

                        for (int k = 0; !jsonScheduleStopTimes.isNull(k); k++) {
                            stopSchedule
                                    .getStopTimes()
                                    .add(convertJsonObjectToScheduleStopTime(
                                            jsonScheduleStopTimes.optJSONObject(k), route, headSign));
                        }

                    }
                }

                Collections.sort(stopSchedule.getStopTimes());

            } catch (final JSONException e) {
                LOGGER.error("OneBusAway API response malformed", e);
                throw new GenericException(ErrorCodeConstants.OBA_RESPONSE_ERROR,
                        "OneBusAway API response contains invalid data");
            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }
        return stopSchedule;
    }

    /**
     * Converts a JSON object to a ScheduleStopTime object.
     * 
     * @param jsonScheduleStopTime
     * @param route
     * @param headSign
     * @return
     */
    private ScheduleStopTime convertJsonObjectToScheduleStopTime(
            final JSONObject jsonScheduleStopTime, final Route route, final String headSign) {

        final ScheduleStopTime scheduleStopTime = new ScheduleStopTime();

        scheduleStopTime.setArrivalTime(OneBusAwayUtils.dateFromTimestamp(jsonScheduleStopTime
                .optLong("arrivalTime")));
        scheduleStopTime.setDepartureTime(OneBusAwayUtils.dateFromTimestamp(jsonScheduleStopTime
                .optLong("departureTime")));
        scheduleStopTime.setTripId(jsonScheduleStopTime.optString("tripId"));
        scheduleStopTime.setHeadsign(headSign);
        scheduleStopTime.setRoute(route);
        scheduleStopTime.setServiceId(jsonScheduleStopTime.optString("serviceId"));

        return scheduleStopTime;
    }

}
