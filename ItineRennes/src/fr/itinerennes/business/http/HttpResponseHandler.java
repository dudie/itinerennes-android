package fr.itinerennes.business.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import fr.itinerennes.business.event.IHttpRequestProgressListener;
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
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(HttpResponseHandler.class);

    /** The HTTP success result code. */
    private static final int HTTP_SUCCESS = 200;

    /** Default charset to use. */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /** An optional listener to notify on request progress. */
    private IHttpRequestProgressListener listener;

    /**
     * Creates an Http response handler.
     */
    public HttpResponseHandler() {

    }

    /**
     * Creates an Http response handler observed by the given listener.
     * 
     * @param listener
     *            the listener observing the progress of the request handling
     */
    public HttpResponseHandler(final IHttpRequestProgressListener listener) {

        this.listener = listener;
    }

    /**
     * Gets the optional listener notified on request progress.
     * 
     * @return the optional listener notified on request progress
     */
    public final IHttpRequestProgressListener getListener() {

        return listener;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.http.client.ResponseHandler#handleResponse(org.apache.http.HttpResponse)
     */
    @Override
    public final T handleResponse(final HttpResponse response) throws ClientProtocolException,
            IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleResponse.start - status={}", response.getStatusLine());
        }

        if (listener != null) {
            listener.onReceiveResponseHeaders(response);
        }

        T result = null;
        if (isHttpSuccess(response)) {
            try {
                result = handleContent(toString(response.getEntity()));
            } catch (final GenericException e) {
                throw new ClientProtocolException("unable to handle response content", e);
            }
        } else {
            throw new HttpResponseException(response.getStatusLine().getStatusCode(), response
                    .getStatusLine().getReasonPhrase());
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
     * @param entity
     * @param defaultCharset
     * @return
     * @throws IOException
     * @throws ParseException
     */
    private final String toString(final HttpEntity entity) throws IOException, ParseException {

        if (entity == null) {
            return "";
        }
        final InputStream instream = entity.getContent();
        if (instream == null) {
            return "";
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
        }
        int i = (int) entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = EntityUtils.getContentCharSet(entity);
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        final Reader reader = new InputStreamReader(instream, charset);
        final CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            final char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
                if (listener != null) {
                    listener.onReceiveContentProgress(l);
                }
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
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
