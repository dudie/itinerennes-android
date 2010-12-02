package fr.itinerennes.business.http.wfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;

/**
 * A handler to receive Geoserver WMS API responses.
 * 
 * @author Olivier Boudet
 */

public class WFSResponseHandler extends HttpResponseHandler<JSONObject> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(WFSResponseHandler.class);

    /** The default char reader buffer length. */
    private static final int CHAR_BUF_SIZE = 512;

    /**
     * Handles the http response and returns the json object containing expected data.
     * 
     * @param response
     *            the http response returned by the server
     * @return the json object containing the expected data
     * @throws GenericException
     *             network exception during request
     */
    @Override
    public final JSONObject handleResponse(final HttpResponse response) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.start");
        }
        super.handleResponse(response);

        JSONObject jsonResult = null;
        try {
            final String stringResult = toString(response.getEntity().getContent());

            jsonResult = new JSONObject(stringResult);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("type : {}", jsonResult.getString("type"));
            }

            if (!jsonResult.getString("type").equalsIgnoreCase("FeatureCollection")) {
                final GenericException e = new GenericException(
                        ErrorCodeConstants.WFS_RESPONSE_ERROR,
                        String.format("No FeatureCollection in response."));
                throw e;
            }

        } catch (final JSONException e) {
            LOGGER.error(e.getMessage(), e);
            throw new GenericException(ErrorCodeConstants.JSON_MALFORMED,
                    "error while parsing json response");
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new GenericException(ErrorCodeConstants.NETWORK, "i/o exception");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.end - {}", jsonResult == null);
        }
        return jsonResult;
    }

    /**
     * Reads an {@link InputStream} as a character buffer and return its content as a {@link String}
     * .
     * 
     * @param in
     *            the input stream to read
     * @return the string read from the input stream
     * @throws GenericException
     *             an problem occurred when reading the stream
     */
    private String toString(final InputStream in) throws GenericException {

        final StringBuilder result = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        final char[] buf = new char[CHAR_BUF_SIZE];
        int len;

        try {
            while ((len = reader.read(buf)) != -1) {
                result.append(buf, 0, len);
            }
        } catch (final IOException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new GenericException(ErrorCodeConstants.NETWORK, "i/o exception");
        }
        return result.toString();
    }
}
