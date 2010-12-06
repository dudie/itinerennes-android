package fr.itinerennes.business.http;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.exceptions.GenericException;

/**
 * Provides common functionalities for HTTP response handlers.
 * 
 * @param <T>
 *            the type objects returned by the handler
 * @author Jérémie Huchet
 */
public abstract class HttpResponseHandler<T> implements ResponseHandler<T> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(HttpResponseHandler.class);

    /** The HTTP success result code. */
    private static final int HTTP_SUCCESS = 200;

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
     */
    @Override
    public final T handleResponse(final HttpResponse response) throws ClientProtocolException,
            IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.start");
        }

        T result = null;
        if (isHttpSuccess(response)) {
            try {
                result = handleContent(EntityUtils.toString(response.getEntity()));
            } catch (final ParseException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (final GenericException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.end - isResultNotNull={}", result != null);
        }
        return result;
    }

    /**
     * Check the code of the given HTTP response is equals to {@value #HTTP_SUCCESS}.
     * 
     * @param response
     *            the HTTP response
     * @return true if HTTP response code is {@value #HTTP_SUCCESS}.
     */
    private boolean isHttpSuccess(final HttpResponse response) {

        return response == null || response.getStatusLine() == null
                || response.getStatusLine().getStatusCode() == HTTP_SUCCESS;
    }

    /**
     * Subclasses must implement this method to handle the content of the HTTP response.
     * 
     * @param content
     *            the content of the HTTP response
     * @return the content
     * @throws GenericException
     *             an error occurred while processing content
     */
    protected abstract T handleContent(String content) throws GenericException;
}
