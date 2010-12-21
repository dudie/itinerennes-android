package fr.itinerennes.business.http.otp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.exceptions.GenericException;

/**
 * Utilities for OpenTripPlanner service.
 * 
 * @author Olivier Boudet
 */
public class OTPUtils {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(OTPUtils.class);

    /** The date format to parse OTP responses. */
    private static final SimpleDateFormat OTP_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssZ");

    /**
     * Checks response the OpenTripPlanner API and return the JSON Object representing data.
     * 
     * @param response
     *            the response of the OpenTripPlanner API
     * @return the JSON Object representing data
     * @throws GenericException
     *             the response does not contain valid data
     */
    public static final JSONObject getServiceResponse(final String response)
            throws GenericException {

        try {
            final JSONObject jsonResult = new JSONObject(response);

            if (!jsonResult.getString("type").equalsIgnoreCase("stop")
                    || jsonResult.getJSONObject("stop") == null) {
                throw new GenericException(ErrorCodeConstants.OTP_RESPONSE_ERROR,
                        String.format("no stop information in response"));
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("type : {}", jsonResult.getString("type"));
            }
            return jsonResult;
        } catch (final JSONException e) {
            LOGGER.error(e.getMessage(), e);
            throw new GenericException(ErrorCodeConstants.OTP_RESPONSE_ERROR,
                    String.format("OTP API response malformed"));
        }

    }

    /**
     * Converts OTP date string to a {@link Date}.
     * 
     * @param stringDate
     *            the date as a string ("2010-11-11T20:47:06+01:00")
     * @return the date
     */
    public static final Date convertJsonStringToDate(final String stringDate) {

        try {
            return OTP_DATE_FORMAT.parse(stringDate);
        } catch (final ParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
