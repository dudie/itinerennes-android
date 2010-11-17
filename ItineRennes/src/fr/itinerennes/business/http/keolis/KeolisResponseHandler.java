package fr.itinerennes.business.http.keolis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.itinerennes.ErrorCodeContants;
import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;

/**
 * A handler to recieve Keolis API responses.
 * 
 * @author Jérémie Huchet
 */
public class KeolisResponseHandler extends HttpResponseHandler<JSONObject> {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(KeolisResponseHandler.class
            .getSimpleName());

    /** The default char reader buffer length. */
    private static final int CHAR_BUF_SIZE = 512;

    /**
     * Handles the http response and returns the json object containing expected data.
     * 
     * @param response
     *            the http response returned by the server
     * @return the json object containing the expected data
     */
    @Override
    public JSONObject handleResponse(final HttpResponse response) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.start");
        }
        super.handleResponse(response);

        JSONObject data = null;
        try {
            final String stringResult = toString(response.getEntity().getContent());

            final JSONObject jsonResult = new JSONObject(stringResult);

            final JSONObject opendata = jsonResult.getJSONObject("opendata");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("checking response for request : {}", opendata.getString("request"));
            }
            final JSONObject answer = opendata.getJSONObject("answer");
            final JSONObject status = answer.getJSONObject("status");
            final JSONObject attributes = status.getJSONObject("@attributes");
            final int code = attributes.getInt("code");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("status : {}, {}", code, attributes.getString("message"));
            }
            if (code != 0) {
                final GenericException e = new GenericException(
                        ErrorCodeContants.KEOLIS_REQUEST_ERROR, String.format("code=%s, %s ", code,
                                attributes.getString("message")));
                throw e;
            }

            data = answer.getJSONObject("data");

        } catch (final JSONException e) {
            LOGGER.error(e.getMessage(), e);
            throw new GenericException(ErrorCodeContants.JSON_MALFORMED,
                    "error while parsing json response");
        } catch (final IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new GenericException(ErrorCodeContants.NETWORK, "i/o exception");
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.end - {}", data == null);
        }
        return data;
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
            throw new GenericException(ErrorCodeContants.NETWORK, "i/o exception");
        }
        return result.toString();
    }

}
