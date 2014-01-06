package fr.itinerennes.business.service;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.itinerennes.commons.utils.StringUtils;

/**
 * An {@link HttpClient} delegating requests to {@link HttpURLConnection}.
 * 
 * @author Jérémie Huchet
 */
public final class SimpleHttpClient implements HttpClient {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleHttpClient.class);

    /** Defaults headers to apply to every request. */
    private final List<Header> defaultHeaders = new ArrayList<Header>();

    /**
     * Add a default header to apply to every request.
     * 
     * @param name
     *            the header name
     * @param value
     *            the header value
     */
    public void addDefaultHeader(final String name, final String value) {

        defaultHeaders.add(new BasicHeader(name, value));
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @deprecated do not use this method
     * @see org.apache.http.client.HttpClient#getParams()
     */
    @Deprecated
    @Override
    public HttpParams getParams() {

        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @deprecated do not use this method
     * @see org.apache.http.client.HttpClient#getConnectionManager()
     */
    @Deprecated
    @Override
    public ClientConnectionManager getConnectionManager() {

        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * Executes the given request using {@link HttpURLConnection}.
     * <ul>
     * <li>Any POST parameter will be ignored</li>
     * <li>Only the URL part of the request is used to make the call</li>
     * </ul>
     * <p>
     * {@inheritDoc}
     * 
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest)
     */
    @Override
    public HttpResponse execute(final HttpUriRequest request) throws IOException {

        final HttpURLConnection cx = (HttpURLConnection) request.getURI().toURL().openConnection();
        for (final Header header : defaultHeaders) {
            cx.addRequestProperty(header.getName(), header.getValue());
        }

        final StatusLine status = new BasicStatusLine(HttpVersion.HTTP_1_1, cx.getResponseCode(),
                cx.getResponseMessage());
        final BasicHttpResponse response = new BasicHttpResponse(status);

        for (final Entry<String, List<String>> header : cx.getHeaderFields().entrySet()) {
            for (final String headerValue : header.getValue()) {
                if (!StringUtils.isBlank(header.getKey())) {
                    response.addHeader(header.getKey(), headerValue);
                }
            }
        }
        final InputStreamEntity entity = new InputStreamEntity(cx.getInputStream(),
                cx.getContentLength());
        response.setEntity(entity);
        return response;
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * This implementation implies restrictions, see {@link #execute(HttpUriRequest)} to get more
     * informations. {@inheritDoc}
     * 
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest,
     *      org.apache.http.client.ResponseHandler)
     */
    @Override
    public <T> T execute(final HttpUriRequest request,
            final ResponseHandler<? extends T> responseHandler) throws IOException {

        final HttpResponse response = execute(request);
        return responseHandler.handleResponse(response);
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @deprecated do not use this method
     * @throws UnsupportedOperationException
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest,
     *      org.apache.http.protocol.HttpContext)
     */
    @Deprecated
    @Override
    public HttpResponse execute(final HttpUriRequest request, final HttpContext context)
            throws IOException {

        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @deprecated do not use this method
     * @throws UnsupportedOperationException
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost,
     *      org.apache.http.HttpRequest)
     */
    @Deprecated
    @Override
    public HttpResponse execute(final HttpHost target, final HttpRequest request)
            throws IOException {

        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @deprecated do not use this method
     * @throws UnsupportedOperationException
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost,
     *      org.apache.http.HttpRequest, org.apache.http.protocol.HttpContext)
     */
    @Deprecated
    @Override
    public HttpResponse execute(final HttpHost target, final HttpRequest request,
            final HttpContext context) throws IOException {

        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @deprecated do not use this method
     * @throws UnsupportedOperationException
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.client.methods.HttpUriRequest,
     *      org.apache.http.client.ResponseHandler, org.apache.http.protocol.HttpContext)
     */
    @Deprecated
    @Override
    public <T> T execute(final HttpUriRequest request,
            final ResponseHandler<? extends T> responseHandler, final HttpContext context)
            throws IOException {

        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @deprecated do not use this method
     * @throws UnsupportedOperationException
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost,
     *      org.apache.http.HttpRequest, org.apache.http.client.ResponseHandler)
     */
    @Deprecated
    @Override
    public <T> T execute(final HttpHost target, final HttpRequest request,
            final ResponseHandler<? extends T> responseHandler) throws IOException {

        throw new UnsupportedOperationException("Method not implemented!");
    }

    /**
     * <strong>Always throws {@link UnsupportedOperationException}.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @deprecated do not use this method
     * @throws UnsupportedOperationException
     * @see org.apache.http.client.HttpClient#execute(org.apache.http.HttpHost,
     *      org.apache.http.HttpRequest, org.apache.http.client.ResponseHandler,
     *      org.apache.http.protocol.HttpContext)
     */
    @Deprecated
    @Override
    public <T> T execute(final HttpHost target, final HttpRequest request,
            final ResponseHandler<? extends T> responseHandler, final HttpContext context)
            throws IOException {

        throw new UnsupportedOperationException("Method not implemented!");
    }
}
