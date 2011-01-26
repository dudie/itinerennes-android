package fr.itinerennes.business.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpRequestHandler;

/**
 * Implementing this interface allows you to be notified of an http request progress.
 * 
 * @author Jérémie Huchet
 */
public interface IHttpRequestProgressListener {

    /**
     * Method called before the Http request is sent.
     * 
     * @param request
     *            the request which will be executed
     */
    void onRequestStart(HttpUriRequest request);

    /**
     * Method called when the connection has been made and the Http header retrieved. The Http
     * service will start fetching the content after this call.
     * 
     * @param response
     *            the Http response received
     */
    void onReceiveResponseHeaders(HttpResponse response);

    /**
     * Method called during the download of the content data.
     * 
     * @param lenght
     *            the amount of bytes received since the last call (or since
     *            {@link #onReceiveResponseHeaders(HttpResponse)}).
     */
    void onReceiveContentProgress(int lenght);

    /**
     * Method called when the request is terminated and the content has been handled.
     * 
     * @param result
     *            the result returned by the {@link HttpRequestHandler}
     */
    void onRequestFinish(Object result);
}
