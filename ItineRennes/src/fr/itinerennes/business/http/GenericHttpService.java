package fr.itinerennes.business.http;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.exceptions.GenericException;

/**
 * An HTTP service which can send requests and handle them with the given
 * {@link HttpResponseHandler}.
 * <p>
 * It uses one {@link DefaultHttpClient}, so it is syncrhonized.
 * 
 * @author Jérémie Huchet
 */
public class GenericHttpService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(GenericHttpService.class);

    /** The HTTP client used to execute the requests. */
    private final DefaultHttpClient httpClient = new DefaultHttpClient();

    /**
     * Executes the given request and handle the result with the given handler.
     * 
     * @param <T>
     *            The expected object type as result
     * @param request
     *            the request to execute
     * @param handler
     *            the handler to manage the HTTP response
     * @return the result of the request converted to the expected type with the handler
     * @throws GenericException
     *             a problem occurred while executing the request or converting the result
     */
    public final synchronized <T> T execute(final HttpUriRequest request,
            final HttpResponseHandler<T> handler) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("execute.start - request={}", request.getRequestLine());
        }

        T response = null;
        try {
            response = httpClient.execute(request, handler);
        } catch (final ClientProtocolException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new GenericException(ErrorCodeConstants.NETWORK, "client protocol exception");
        } catch (final IOException e) {
            LOGGER.debug(e.getMessage(), e);
            throw new GenericException(ErrorCodeConstants.NETWORK, "i/o exception");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("execute.end");
        }
        return response;
    }
}
