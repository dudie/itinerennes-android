package fr.itinerennes.business.http.wfs;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.exceptions.GenericException;

/**
 * Common utilities for WFS API.
 * 
 * @author Jérémie Huchet
 */
public final class WFSUtils {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(WFSUtils.class);

    /**
     * Checks the response code of the WFS API and return the JSON Object representinf the data.
     * 
     * @param reponse
     *            the reponse of the keolis API
     * @return the JSON Object named "data"
     * @throws GenericException
     *             the response does not contains valid data
     */
    public final static JSONObject getServiceResponse(final String response)
            throws GenericException {

        try {
            final JSONObject jsonResult = new JSONObject(response);

            if (!jsonResult.getString("type").equalsIgnoreCase("FeatureCollection")) {
                throw new GenericException(ErrorCodeConstants.WFS_RESPONSE_ERROR,
                        String.format("no FeatureCollection in response"));
            }
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("type : {}", jsonResult.getString("type"));
            }
            return jsonResult;
        } catch (final JSONException e) {
            LOGGER.error(e.getMessage(), e);
            throw new GenericException(ErrorCodeConstants.WFS_RESPONSE_ERROR,
                    String.format("WFS API response malformed"));
        }

    }
}
