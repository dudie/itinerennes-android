package fr.itinerennes.business;

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
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.itinerennes.business.service.VersionService;
import fr.itinerennes.startup.version.model.UpdateInfo;

/**
 * @author Jérémie Huchet
 */
@RunWith(RobolectricTestRunner.class)
public final class VersionServiceTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionServiceTest.class);

    private HttpClient httpClient;

    private HttpResponse response = null;

    @Before
    public void setup() throws ClientProtocolException, IOException {

        httpClient = mock(HttpClient.class);
        when(httpClient.execute(any(HttpGet.class))).thenAnswer(new Answer<HttpResponse>() {

            public HttpResponse answer(final InvocationOnMock invocation) throws Throwable {

                return response;
            }
        });
    }

    @Test
    public void test404NotFound() {

        response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_NOT_FOUND));

        final VersionService versionService = new VersionService(Robolectric.application,
                httpClient);
        final UpdateInfo update = versionService.getUpdateInfo();

        assertNull(update);
    }

    @Test
    public void testEmptyFile() throws UnsupportedEncodingException {

        response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(new StringEntity(""));

        final VersionService versionService = new VersionService(Robolectric.application,
                httpClient);
        final UpdateInfo update = versionService.getUpdateInfo();

        assertNull(update);
    }

    @Test
    public void testNoUpdateElement() throws UnsupportedEncodingException {

        response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(new StringEntity("{ }"));

        final VersionService versionService = new VersionService(Robolectric.application,
                httpClient);
        final UpdateInfo update = versionService.getUpdateInfo();

        assertNull(update);
    }

    @Test
    public void testUpdateNotAvailable() throws UnsupportedEncodingException {

        response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(
                new StringEntity("{ update : { available : false } }"));

        final VersionService versionService = new VersionService(Robolectric.application,
                httpClient);
        final UpdateInfo update = versionService.getUpdateInfo();

        assertNotNull(update);
        assertFalse(update.isAvailable());
        assertFalse(update.isMandatory());
    }

    @Test
    public void testUpdateAvailable() throws UnsupportedEncodingException {

        response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity())
                .thenReturn(new StringEntity("{ update : { available : true } }"));

        final VersionService versionService = new VersionService(Robolectric.application,
                httpClient);
        final UpdateInfo update = versionService.getUpdateInfo();

        assertNotNull(update);
        assertTrue(update.isAvailable());
        assertFalse(update.isMandatory());
    }

    @Test
    public void testUpdateAvailableNotMandatory() throws UnsupportedEncodingException {

        response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(
                new StringEntity("{ update : { available : true, mandatory : false } }"));

        final VersionService versionService = new VersionService(Robolectric.application,
                httpClient);
        final UpdateInfo update = versionService.getUpdateInfo();

        assertNotNull(update);
        assertTrue(update.isAvailable());
        assertFalse(update.isMandatory());
    }

    @Test
    public void testUpdateAvailableMandatory() throws UnsupportedEncodingException {

        response = mock(HttpResponse.class);
        when(response.getStatusLine()).thenReturn(new MockStatusLine(HttpStatus.SC_OK));
        when(response.getEntity()).thenReturn(
                new StringEntity("{ update : { available : true, mandatory : true } }"));

        final VersionService versionService = new VersionService(Robolectric.application,
                httpClient);
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
