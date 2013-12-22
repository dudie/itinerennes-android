package fr.itinerennes.business.service;

/*
 * [license]
 * Java-based tests
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

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xtremelabs.robolectric.Robolectric;

import fr.itinerennes.business.service.VersionService;
import fr.itinerennes.business.service.VersionService_;
import fr.itinerennes.startup.version.model.UpdateInfo;
import fr.itinerennes.test.ItineRennesRobolelectricTestRunner;

/**
 * @author Jérémie Huchet
 */
@RunWith(ItineRennesRobolelectricTestRunner.class)
public final class VersionServiceTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionServiceTest.class);

    private HttpClient httpClient;

    private HttpResponse response = null;
    
    private VersionService versionService;

    @Before
    public void setup() throws ClientProtocolException, IOException {

        response = mock(HttpResponse.class);

        httpClient = mock(HttpClient.class);
        when(httpClient.execute(any(HttpGet.class))).then(new Answer<HttpResponse>() {

            public HttpResponse answer(final InvocationOnMock invocation) throws Throwable {

                return response;
            }
        });

        versionService = VersionService_.getInstance_(Robolectric.application);
        versionService.httpClient = httpClient;
    }

    @Test
    public void test404NotFound() {

        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_NOT_FOUND));

        final UpdateInfo update = versionService.getUpdateInfo();

        assertNull(update);
    }

    @Test
    public void testEmptyFile() throws UnsupportedEncodingException {

        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(new StringEntity(""));

        final UpdateInfo update = versionService.getUpdateInfo();

        assertNull(update);
    }

    @Test
    public void testNoUpdateElement() throws UnsupportedEncodingException {

        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(new StringEntity("{ }"));

        final UpdateInfo update = versionService.getUpdateInfo();

        assertNull(update);
    }

    @Test
    public void testUpdateNotAvailable() throws UnsupportedEncodingException {

        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(
                new StringEntity("{ update : { available : false } }"));

        final UpdateInfo update = versionService.getUpdateInfo();

        assertNotNull(update);
        assertFalse(update.isAvailable());
        assertFalse(update.isMandatory());
    }

    @Test
    public void testUpdateAvailable() throws UnsupportedEncodingException {

        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity())
                .thenReturn(new StringEntity("{ update : { available : true } }"));

        final UpdateInfo update = versionService.getUpdateInfo();

        assertNotNull(update);
        assertTrue(update.isAvailable());
        assertFalse(update.isMandatory());
    }

    @Test
    public void testUpdateAvailableNotMandatory() throws UnsupportedEncodingException {

        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(
                new StringEntity("{ update : { available : true, mandatory : false } }"));

        final UpdateInfo update = versionService.getUpdateInfo();

        assertNotNull(update);
        assertTrue(update.isAvailable());
        assertFalse(update.isMandatory());
    }

    @Test
    public void testUpdateAvailableMandatory() throws UnsupportedEncodingException {

        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(
                new StringEntity("{ update : { available : true, mandatory : true } }"));

        final UpdateInfo update = versionService.getUpdateInfo();

        assertNotNull(update);
        assertTrue(update.isAvailable());
        assertTrue(update.isMandatory());
    }

    private class MockStatusLine implements StatusLine {

        private final int statusCode;

        public MockStatusLine(final int statusCode) {

            this.statusCode = statusCode;
        }

        public ProtocolVersion getProtocolVersion() {

            return null;
        }

        public int getStatusCode() {

            return statusCode;
        }

        public String getReasonPhrase() {

            return null;
        }

    }
}
