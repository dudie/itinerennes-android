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

/**
 * Handles responses for a call to the "stop" method of the OneBusAway API.
 * 
 * @author Olivier Boudet
 */
public class StopHttpResponseHandler extends HttpResponseHandler<Stop> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(StopHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected final Stop handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject data = OneBusAwayUtils.getServiceResponse(content);

        Stop stop = null;
        if (data != null) {
            stop = new Stop();

            try {
                final JSONObject jsonEntry = data.getJSONObject("entry");
                final JSONObject jsonReferences = data.getJSONObject("references");
                final JSONArray jsonRoutes = jsonReferences.getJSONArray("routes");

                final HashMap<String, Route> routes = OneBusAwayUtils
                        .getReferencedRoutes(jsonRoutes);
                stop = OneBusAwayUtils.convertJsonObjectToStop(jsonEntry, routes);

            } catch (final JSONException e) {
                LOGGER.error("OneBusAway API response malformed", e);
                throw new GenericException(ErrorCodeConstants.OBA_RESPONSE_ERROR,
                        "OneBusAway API response contains invalid data");
            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }
        return stop;
    }
}
