package fr.itinerennes;

import java.lang.reflect.Method;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.apache.http.client.HttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;

import fr.dudie.keolis.client.JsonKeolisClient;
import fr.dudie.keolis.client.KeolisClient;
import fr.dudie.nominatim.client.JsonNominatimClient;
import fr.dudie.nominatim.client.NominatimClient;
import fr.dudie.nominatim.model.BoundingBox;
import fr.dudie.onebusaway.client.IOneBusAwayClient;
import fr.dudie.onebusaway.client.JsonOneBusAwayClient;

import fr.itinerennes.business.service.AccessibilityService;
import fr.itinerennes.business.service.BookmarkService;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.business.service.SimpleHttpClient;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.database.MarkerDao;
import fr.itinerennes.exceptions.DefaultExceptionHandler;
import fr.itinerennes.exceptions.ExceptionHandler;
import fr.itinerennes.startup.LoadingActivity;
import fr.itinerennes.utils.VersionUtils;

/**
 * @author Jérémie Huchet
 */
@ReportsCrashes(formKey = "dDRaN2hHdUFrUDctQXc3Zl85ZjZOYWc6MQ")
public class ItineRennesApplication extends Application {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItineRennesApplication.class);

    /** The database helper. */
    private DatabaseHelper databaseHelper;

    /** The itinerennes shared preferences. */
    private SharedPreferences sharedPreferences;

    /** The default exception handler. */
    private final ExceptionHandler exceptionHandler = new DefaultExceptionHandler(this);

    /** The marker DAO. */
    private MarkerDao markerDao;

    /** The line icon service. */
    private LineIconService lineIconService;

    /** The bookmarks service. */
    private BookmarkService bookmarksService;

    /** The accessibility service. */
    private AccessibilityService accessibilityService;

    /** The keolis client. */
    private KeolisClient keolisClient;

    /** The OneBusAway client. */
    private IOneBusAwayClient oneBusAwayClient;

    /** The Nominatim client. */
    private NominatimClient nominatimClient;

    /** The Progress Http Client. */
    private HttpClient httpClient;

    /**
     * Inits ACRA and strict mode.
     * 
     * @see android.app.Application#onCreate()
     */
    @Override
    public final void onCreate() {

        if (Conf.ACRA_ENABLED) {
            // The following line triggers the initialization of ACRA
            ACRA.init(this);
        }

        // trying to manage strict mode if the current api level supports it
        try {
            final Class<?> sMode = Class.forName("android.os.StrictMode");
            // enable the recommended StrictMode defaults, with violations just being logged.
            final Method enableDefaults = sMode.getMethod("enableDefaults");
            enableDefaults.invoke(null);
        } catch (final Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("StrictMode not supported...");
            }
        }

        final Intent i = new Intent(this, LoadingActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(i);

        super.onCreate();
    }

    /**
     * Close the database helper. If this method is derived, you must ensure to call
     * <code>super.onDestroy()</code>.
     * 
     * @see android.app.Application#onTerminate()
     */
    @Override
    public final void onTerminate() {

        super.onTerminate();
        if (databaseHelper != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DBHELPER will request #close() on the database helper");
            }
            databaseHelper.close();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DBHELPER #close() on the database helper terminated");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DBHELPER database helper was not opened");
        }
    }

    /**
     * Gets a reference to the database helper.
     * 
     * @return a reference to the database helper
     */
    public final DatabaseHelper getDatabaseHelper() {

        if (databaseHelper == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DBHELPER initializing a database helper");
            }
            databaseHelper = new DatabaseHelper(getBaseContext());
        }
        return databaseHelper;
    }

    /**
     * Gets the itinerennes shared preferences.
     * 
     * @return a shared preferences
     */
    public final SharedPreferences getITRPreferences() {

        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences(ITRPrefs.PREFS_NAME, MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * Gets the exception handler.
     * 
     * @return the current exception handler
     */
    public final ExceptionHandler getExceptionHandler() {

        return exceptionHandler;
    }

    /**
     * Gets a reference to the line icon service.
     * 
     * @return a reference to the line icon service
     */
    public final LineIconService getLineIconService() {

        if (lineIconService == null) {
            lineIconService = new LineIconService(this, getDatabaseHelper());
        }
        return lineIconService;
    }

    /**
     * Gets a reference to the BookmarkService.
     * 
     * @return a reference to the {@link BookmarkService}
     */
    public final BookmarkService getBookmarksService() {

        if (bookmarksService == null) {
            bookmarksService = new BookmarkService(getDatabaseHelper());
        }
        return bookmarksService;
    }

    /**
     * Gets a reference to the AccessibilityService.
     * 
     * @return a reference to the {@link AccessibilityService}
     */
    public final AccessibilityService getAccessibilityService() {

        if (accessibilityService == null) {
            accessibilityService = new AccessibilityService(getDatabaseHelper());
        }
        return accessibilityService;
    }

    /**
     * Gets a reference to the MarkerDao.
     * 
     * @return a reference to the {@link MarkerDao}
     */
    public final MarkerDao getMarkerDao() {

        if (markerDao == null) {
            markerDao = new MarkerDao(getApplicationContext(), getDatabaseHelper());
        }
        return markerDao;
    }

    /**
     * Gets a reference to the HttpClient.
     * 
     * @return a reference to the {@link ProgressHttpClient}
     */
    public final HttpClient getHttpClient() {

        if (httpClient == null) {

            final String appVersion = VersionUtils.getCurrent(this);
            final String userAgent = String.format("ItineRennes/%s (Android/%s; SDK %s; %s; %s)",
                    appVersion, android.os.Build.VERSION.RELEASE, android.os.Build.VERSION.SDK_INT,
                    android.os.Build.MODEL, android.os.Build.DEVICE);

            final SimpleHttpClient c = new SimpleHttpClient();
            c.addDefaultHeader(HttpProtocolParams.USER_AGENT, userAgent);

            httpClient = c;
        }

        return httpClient;
    }

    /**
     * Gets a reference to the Keolis client.
     * 
     * @return a keolis client
     */
    public final KeolisClient getKeolisClient() {

        if (null == keolisClient) {
            keolisClient = new JsonKeolisClient(getHttpClient(), Conf.KEOLIS_API_URL,
                    Conf.KEOLIS_API_KEY);
        }
        return keolisClient;
    }

    /**
     * Gets a reference to the One Bus Away client.
     * 
     * @return a one bus away client
     */
    public final IOneBusAwayClient getOneBusAwayClient() {

        if (null == oneBusAwayClient) {
            oneBusAwayClient = new JsonOneBusAwayClient(getHttpClient(), Conf.ONEBUSAWAY_API_URL,
                    Conf.ONEBUSAWAY_API_KEY);
        }
        return oneBusAwayClient;
    }

    /**
     * Gets a reference to the Nominatim client.
     * 
     * @return a nominatim client
     */
    public final NominatimClient getNominatimClient() {

        if (null == nominatimClient) {
            final BoundingBox bounds = new BoundingBox();
            bounds.setWestE6(Conf.MAP_RENNES_LON - Conf.NOMINATIM_SEARCH_OFFSET);
            bounds.setEastE6(Conf.MAP_RENNES_LON + Conf.NOMINATIM_SEARCH_OFFSET);
            bounds.setNorthE6(Conf.MAP_RENNES_LAT + Conf.NOMINATIM_SEARCH_OFFSET);
            bounds.setSouthE6(Conf.MAP_RENNES_LAT - Conf.NOMINATIM_SEARCH_OFFSET);
            nominatimClient = new JsonNominatimClient(getHttpClient(), getResources().getString(
                    R.string.contact_mail), bounds, true, false);
        }
        return nominatimClient;
    }
}
