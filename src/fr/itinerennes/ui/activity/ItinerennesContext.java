package fr.itinerennes.ui.activity;

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

import android.app.Activity;
import android.content.SharedPreferences;

import fr.itinerennes.ITRPrefs;
import fr.itinerennes.ItineRennes;
import fr.itinerennes.business.service.AccessibilityService;
import fr.itinerennes.business.service.BikeService;
import fr.itinerennes.business.service.BookmarkService;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.business.service.MarkersService;
import fr.itinerennes.business.service.SubwayService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.DefaultExceptionHandler;
import fr.itinerennes.exceptions.ExceptionHandler;
import fr.itinerennes.http.client.ProgressHttpClient;

/**
 * An abstract activity providing common functionalities such as automatic.
 * 
 * @author Jérémie Huchet
 */
public abstract class ItinerennesContext extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(ItinerennesContext.class);

    /** The itinerennes shared preferences. */
    private SharedPreferences sharedPreferences;

    /** The default exception handler. */
    private final ExceptionHandler exceptionHandler = new DefaultExceptionHandler(this);

    /** The marker service. */
    private MarkersService markerService;

    /** The bike service. */
    private BikeService bikeService;

    /** The subway service. */
    private SubwayService subwayService;

    /** The line icon service. */
    private LineIconService lineIconService;

    /** The bookmarks service. */
    private BookmarkService bookmarksService;

    /** The accessibility service. */
    private AccessibilityService accessibilityService;

    /** The Progress Http Client */
    private ProgressHttpClient httpClient;

    /**
     * Tries to dismiss a displayed dialog but catch the exception throws by the original
     * implementation if the dialog was not displayed.
     * 
     * @param id
     *            the identifier of the dialog to dismiss
     * @return true if a dialog has been dismissed
     * @see #dismissDialog(int)
     */
    public final boolean dismissDialogIfDisplayed(final int id) {

        boolean dismissed = false;
        try {
            super.dismissDialog(id);
            dismissed = true;
        } catch (final IllegalArgumentException e) {
            dismissed = false;
        }
        return dismissed;
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
     * Gets a reference to the database helper.
     * 
     * @return a reference to the database helper
     */
    public final DatabaseHelper getDatabaseHelper() {

        return ((ItineRennes) getApplicationContext()).getDatabaseHelper();
    }

    /**
     * Gets a reference to the bike service.
     * 
     * @return a reference to the bike service
     */
    public final BikeService getBikeService() {

        if (bikeService == null) {
            bikeService = new BikeService(this);
        }
        return bikeService;
    }

    /**
     * Gets a reference to the subway service.
     * 
     * @return a reference to the subway service
     */
    public final SubwayService getSubwayService() {

        if (subwayService == null) {
            subwayService = new SubwayService(this);
        }
        return subwayService;
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
    public final MarkersService getMarkerService() {

        if (markerService == null) {
            markerService = new MarkersService(getDatabaseHelper());
        }
        return markerService;
    }

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
        }

        return httpClient;
    }
}
