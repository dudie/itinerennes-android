package fr.itinerennes.business.http.oba;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.exceptions.GenericException;

/**
 * @author Olivier Boudet
 */
public class OneBusAwayUtils {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(OneBusAwayUtils.class);

    /**
     * Checks the response code of the OneBusAway API and return the JSON Object named "data".
     * 
     * @param reponse
     *            the reponse of the OneBusAway API
     * @return the JSON Object named "data"
     * @throws GenericException
     *             the response does not contains valid data
     */
    public static final JSONObject getServiceResponse(final String reponse) throws GenericException {

        try {
            final JSONObject jsonResult = new JSONObject(reponse);

            final int code = jsonResult.getInt("code");

            if (code != 200) {
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
     * @return the date
     */
    public static final Date dateFromTimestamp(final Long timestamp) {

        return dateFromTimestamps(timestamp, (long) 0);

    }

    /**
     * Add two timestamps and returns a Date.
     * 
     * @param timestamp1
     * @param timestamp2
     * @return the date
     */
    public static final Date dateFromTimestamps(final Long timestamp1, final Long timestamp2) {

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp1 + timestamp2 * 1000);
        return calendar.getTime();

    }
}
