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
import java.io.InputStreamReader;
import java.net.URI;

import org.acra.ErrorReporter;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import com.google.gson.Gson;

import fr.itinerennes.Conf;
import fr.itinerennes.startup.version.model.PackageVersion;
import fr.itinerennes.startup.version.model.UpdateInfo;
import fr.itinerennes.utils.VersionUtils;

/**
 * Service to get informations about latest version.
 * 
 * @author Jérémie Huchet
 */
public final class VersionService {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionService.class);

    /** The context. */
    private final Context context;

    /** The HTTP client. */
    private final HttpClient httpClient;

    /**
     * Constructor.
     * 
     * @param context
     *            the context.
     * @param httpClient
     *            the HTTP client
     */
    public VersionService(final Context context, final HttpClient httpClient) {

        this.context = context;
        this.httpClient = httpClient;
    }

    /**
     * Retrieve informations about an eventual update.
     * 
     * @return the update informations
     */
    public UpdateInfo getUpdateInfo() {

        LOGGER.debug("getUpdateInfo.start");

        final HttpGet request = new HttpGet();
        request.setURI(URI.create(String.format("%s/%d.json", Conf.ITINERENNES_VERSION_URL,
                VersionUtils.getCode(context))));

        UpdateInfo updateInfo = null;

        try {
            final HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                final PackageVersion version = new Gson().fromJson(new InputStreamReader(response
                        .getEntity().getContent()), PackageVersion.class);

                if (null != version) {
                    updateInfo = version.getUpdate();
                }

            } else {
                LOGGER.warn("Can't get update informations: {} {}", response.getStatusLine()
                        .getStatusCode(), response.getStatusLine().getReasonPhrase());
            }
        } catch (final ClientProtocolException e) {
            ErrorReporter.getInstance().handleSilentException(e);
            LOGGER.warn("Unable to retrieve update informations", e);
        } catch (final IOException e) {
            ErrorReporter.getInstance().handleSilentException(e);
            LOGGER.warn("Unable to retrieve update informations", e);
        }

        LOGGER.debug("getUpdateInfo.end - {}", updateInfo);
        return updateInfo;
    }
}
