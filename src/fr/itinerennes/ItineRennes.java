package fr.itinerennes;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.Application;
import android.content.SharedPreferences;

import fr.itinerennes.business.service.AccessibilityService;
import fr.itinerennes.business.service.BookmarkService;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.business.service.MarkerOverlayService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.DefaultExceptionHandler;
import fr.itinerennes.exceptions.ExceptionHandler;
import fr.itinerennes.http.client.ProgressHttpClient;
import fr.itinerennes.keolis.client.JsonKeolisClient;
import fr.itinerennes.keolis.client.KeolisClient;
import fr.itinerennes.onebusaway.client.IOneBusAwayClient;
import fr.itinerennes.onebusaway.client.JsonOneBusAwayClient;

/**
 * @author Jérémie Huchet
 */
@ReportsCrashes(formKey = "dHVwTGY1ZTBOcEQ5SzdXT2dEN2diY3c6MQ")
public class ItineRennes extends Application {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(ItineRennes.class);

    /** The database helper. */
    private DatabaseHelper databaseHelper;

    /** The itinerennes shared preferences. */
    private SharedPreferences sharedPreferences;

    /** The default exception handler. */
    private final ExceptionHandler exceptionHandler = new DefaultExceptionHandler(this);

    /** The marker service. */
    private MarkerOverlayService markerService;

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

    /** The Progress Http Client. */
    private ProgressHttpClient httpClient;

    /**
     * Inits ACRA.
     * 
     * @see android.app.Application#onCreate()
     */
    @Override
    public final void onCreate() {

        // The following line triggers the initialization of ACRA
        ACRA.init(this);

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
     * Gets a reference to the MarkerService.
     * 
     * @return a reference to the {@link MarkerService}
     */
    public final MarkerOverlayService getMarkerService() {

        if (markerService == null) {
            markerService = new MarkerOverlayService(this, getDatabaseHelper());
        }
        return markerService;
    }

    /**
     * Gets a reference to the HttpClient.
     * 
     * @return a reference to the {@link ProgressHttpClient}
     */
    public final ProgressHttpClient getHttpClient() {

        if (httpClient == null) {
            final SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", new PlainSocketFactory(), 80));

            final HttpParams params = new BasicHttpParams();

            ConnManagerParams.setMaxTotalConnections(params, 5);
            HttpConnectionParams.setConnectionTimeout(params, 60000);

            final ThreadSafeClientConnManager connexionManager = new ThreadSafeClientConnManager(
                    params, registry);
            httpClient = new ProgressHttpClient(connexionManager, null);

            final String appVersion = this.getString(R.string.version_number);
            final String userAgent = String.format("Android/%s (SDK %s; %s) %s ItineRennes %s",
                    android.os.Build.DISPLAY, android.os.Build.VERSION.SDK_INT,
                    android.os.Build.CPU_ABI, android.os.Build.MODEL, appVersion);
            httpClient.setDefaultUserAgent(userAgent);
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
            keolisClient = new JsonKeolisClient(getHttpClient(),
                    ItineRennesConstants.KEOLIS_API_URL, ItineRennesConstants.KEOLIS_API_KEY);
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
            oneBusAwayClient = new JsonOneBusAwayClient(getHttpClient(),
                    ItineRennesConstants.OBA_API_URL, ItineRennesConstants.OBA_API_KEY);
        }
        return oneBusAwayClient;
    }
}
