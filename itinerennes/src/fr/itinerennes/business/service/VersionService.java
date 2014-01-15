package fr.itinerennes.business.service;

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
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import fr.itinerennes.Conf;
import fr.itinerennes.startup.version.model.PackageVersion;
import fr.itinerennes.startup.version.model.UpdateInfo;
import fr.itinerennes.utils.VersionUtils;

/**
 * Service to get informations about latest version.
 * 
 * @author Jérémie Huchet
 */
@EBean
public class VersionService {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionService.class);

    /** The context. */
    private final Context context;

    /** The HTTP client. */
    @Bean(ItineRennesHttpClient.class)
    HttpClient httpClient;

    /**
     * Constructor.
     * 
     * @param context
     *            the context.
     */
    public VersionService(final Context context) {

        this.context = context;
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
