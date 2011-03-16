package fr.itinerennes.business.http.keolis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.exceptions.GenericException;

/**
 * Utilities for Keolis service.
 * 
 * @author Jérémie Huchet
 */
public class KeoUtils {

    /** The date format the Keolis service use to send. */
    private static final SimpleDateFormat KEOLIS_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssZ");

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(KeoUtils.class);

    /**
     * Checks the response code of the keolis API and return the JSON Object named "data".
     * 
     * @param reponse
     *            the reponse of the keolis API
     * @return the JSON Object named "data"
     * @throws GenericException
     *             the response does not contains valid data
     */
    public static final JSONObject getServiceResponse(final String reponse) throws GenericException {

        try {
            final JSONObject jsonResult = new JSONObject(reponse);

            final JSONObject opendata = jsonResult.getJSONObject("opendata");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("checking response for request : {}", opendata.getString("request"));
            }
            final JSONObject answer = opendata.getJSONObject("answer");
            final JSONObject status = answer.getJSONObject("status");
            final JSONObject attributes = status.getJSONObject("@attributes");
            final int code = attributes.getInt("code");
            if (code != 0) {
                final String message = String.format("Keolis API error : code=%S, %s ", code,
                        attributes.getString("message"));
                LOGGER.error(message);
                throw new GenericException(ErrorCodeConstants.KEOLIS_REQUEST_ERROR, message);
            }
            return answer.getJSONObject("data");
        } catch (final JSONException e) {
            LOGGER.error("Keolis API response malformed", e);
            throw new GenericException(ErrorCodeConstants.KEOLIS_REQUEST_ERROR,
                    "Keolis API response contains invalid data");
        }
    }

    /**
     * Converts Keolis date string to a {@link Date}.
     * 
     * @param stringDate
     *            the date as a string ("2010-11-11T20:47:06+01:00")
     * @return the date
     */
    public static final Date convertJsonStringToDate(final String stringDate) {

        try {
            return KEOLIS_DATE_FORMAT.parse(stringDate);
        } catch (final ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * Converts Keolis int status to a {@link Boolean}.
     * 
     * @param status
     *            the status to convert
     * @return false if the given integer is equals to 0, else true
     */
    public static final boolean convertJsonIntToBoolean(final int status) {

        return 0 != status;
    }
}
