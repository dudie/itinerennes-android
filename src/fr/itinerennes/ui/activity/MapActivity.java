package fr.itinerennes.ui.activity;

import org.osmdroid.util.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import fr.itinerennes.ITRPrefs;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.ui.views.ITRMapView;
import fr.itinerennes.ui.views.PreloadDialog;
import fr.itinerennes.ui.views.overlays.LocationOverlay;
import fr.itinerennes.ui.views.overlays.OverlayConstants;

/**
 * This is the main activity. Uses the <code>main_map.xml</code> layout and displays a menu bar on
 * top and a map view at center of the screen.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class MapActivity extends ITRContext implements OverlayConstants {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(MapActivity.class);

    /** Intent parameter name to pass the map zoom level to set. */
    public static final String INTENT_SET_MAP_ZOOM = String.format("%s.setMapZoom",
            MapActivity.class.getName());

    /** Intent parameter name to pass the map latidute to set. */
    public static final String INTENT_SET_MAP_LAT = String.format("%s.setMapLatitude",
            MapActivity.class.getName());

    /** Intent parameter name to pass the map longitude to set. */
    public static final String INTENT_SET_MAP_LON = String.format("%s.setMapLongitude",
            MapActivity.class.getName());

    /**
     * Intent parameter name to use to set the type of something to find in the overlay and to set
     * selected.
     */
    public static final String INTENT_SELECT_BOOKMARK_TYPE = String.format("%s.selectBookmarkType",
            MapActivity.class.getName());

    /**
     * Intent parameter name to use to set the identifier of something to find in the overlay and to
     * set selected.
     */
    public static final String INTENT_SELECT_BOOKMARK_ID = String.format("s.selectBookmarkId",
            MapActivity.class.getName());

    /** Duration of toast messages. */
    private static final int TOAST_DURATION = 300;

    /** The map view. */
    private ITRMapView map;

    /** The map box view. */
    private View mapBox;

    /** The my location overlay. */
    private LocationOverlay myLocation;

    /** Shared preferences. */
    private SharedPreferences sharedPreferences;

    /** Location manager. */
    private LocationManager locationManager;

    /**
     * Called when activity starts.
     * <p>
     * Displays the view.
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public final void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences(ITRPrefs.PREFS_NAME, MODE_PRIVATE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setContentView(R.layout.main_map);

        this.map = (ITRMapView) findViewById(R.id.map);
        this.mapBox = findViewById(R.id.map_box);

        this.map.getController().getMapOverlayHelper()
                .show(BUS_STATIONS | BIKE_STATIONS | SUBWAY_STATIONS | LOCATION);
        this.myLocation = map.getController().getMapOverlayHelper().getLocationOverlay();

        map.setMultiTouchControls(true);

        // if intent asks for a specific zoom level, its values overrides the ones
        // saved when the activity was paused
        final int zoomToRestore = sharedPreferences.getInt(ITRPrefs.MAP_ZOOM_LEVEL,
                ItineRennesConstants.CONFIG_DEFAULT_ZOOM);
        final int newZoom = getIntent().getIntExtra(INTENT_SET_MAP_ZOOM, zoomToRestore);
        map.getController().setZoom(newZoom);
        // same thing with the map center
        final int lonToRestore = sharedPreferences.getInt(ITRPrefs.MAP_CENTER_LAT,
                ItineRennesConstants.CONFIG_RENNES_LAT);
        final int latToRestore = sharedPreferences.getInt(ITRPrefs.MAP_CENTER_LON,
                ItineRennesConstants.CONFIG_RENNES_LON);
        final int newLat = getIntent().getIntExtra(INTENT_SET_MAP_LAT, lonToRestore);
        final int newLon = getIntent().getIntExtra(INTENT_SET_MAP_LON, latToRestore);
        map.getController().setCenter(new GeoPoint(newLat, newLon));

        // DEBUG
        // map.getOverlays().add(new DebugOverlay(getBaseContext()));

        // if first start of the application, open the preload dialog
        if (sharedPreferences.getBoolean(ITRPrefs.DISPLAY_CACHE_ADVICE, true)) {
            final PreloadDialog preload = new PreloadDialog(this);
            preload.show();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected final void onResume() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onResume.start");
        }

        // if a location provider is enabled and follow location is activated in preferences, the
        // follow location feature is enabled
        if (sharedPreferences.getBoolean(ITRPrefs.MAP_SHOW_LOCATION, true)
                && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            myLocation.enableFollowLocation();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onResume.end");
        }
        super.onResume();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected final void onPause() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPause.start");
        }

        // saving in preferences the state of the map (center, follow location and zoom)
        final SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(ITRPrefs.MAP_CENTER_LAT, map.getMapCenterLatitudeE6());
        edit.putInt(ITRPrefs.MAP_CENTER_LON, map.getMapCenterLongitudeE6());
        edit.putInt(ITRPrefs.MAP_ZOOM_LEVEL, map.getZoomLevel());
        edit.putBoolean(ITRPrefs.MAP_SHOW_LOCATION, myLocation.isMyLocationEnabled());
        edit.commit();

        myLocation.disableFollowLocation();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPause.end");
        }
        super.onPause();
    }

    /**
     * Creates the option menu with the following items.
     * <ul>
     * <li>about</li>
     * </ul>
     * <p>
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {

        // BOOKMARKS
        final MenuItem favourites = menu.add(Menu.NONE, MenuOptions.BOOKMARKS, Menu.NONE,
                R.string.menu_bookmarks);
        favourites.setIcon(android.R.drawable.btn_star);
        final Intent favActivityIntent = new Intent(this, BookmarksActivity.class);
        favourites.setIntent(favActivityIntent);

        // ABOUT
        final MenuItem about = menu.add(Menu.NONE, MenuOptions.ABOUT, Menu.NONE,
                R.string.menu_about);
        about.setIcon(android.R.drawable.ic_menu_help);
        about.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {

                showDialog(Dialogs.ABOUT);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Click method handler invoked when a click event is detected on the my location button.
     * 
     * @param button
     *            the button view on which the event was detected
     */
    public final void onMyLocationButtonClick(final View button) {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, R.string.location_service_disabled, TOAST_DURATION).show();
            ((ToggleButton) button).setChecked(false);
        } else {
            myLocation.toggleFollowLocation();
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected final Dialog onCreateDialog(final int id) {

        AlertDialog dialog;
        switch (id) {
        case Dialogs.SELECT_LAYERS:
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.select_layer).setCancelable(true)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int id) {

                            dialog.cancel();
                        }
                    });
            dialog = builder.create();
            break;
        case Dialogs.ABOUT:
            final AlertDialog.Builder aboutBuilder = new AlertDialog.Builder(this);
            aboutBuilder.setTitle(R.string.menu_about).setCancelable(true);
            final View aboutView = getLayoutInflater().inflate(R.layout.about, null);
            aboutBuilder.setView(aboutView);
            aboutBuilder.setIcon(android.R.drawable.ic_dialog_info);
            dialog = aboutBuilder.create();
            break;
        default:
            dialog = null;
        }
        return dialog;
    }

    /**
     * An event listener which will display the dialog box to ask the user which layers he wants to
     * show.
     * 
     * @author Jérémie Huchet
     */
    private class LayersClickListener implements OnClickListener {

        /**
         * Displays a dialog box with a list of displayable and selectable layers.
         * 
         * @param button
         *            the view that was clicked
         */
        @Override
        public void onClick(final View button) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("layers.button.click");
            }
            showDialog(Dialogs.SELECT_LAYERS);
        }
    }

    /**
     * Class containing menu constants.
     * 
     * @author Jérémie Huchet
     */
    private static final class MenuOptions {

        /** "About" menu option. */
        public static final int ABOUT = 0;

        /** "Favourites" menu option. */
        public static final int BOOKMARKS = 1;
    }

    /**
     * Class containing dialog boxes contants.
     * 
     * @author Jérémie Huchet
     */
    private static final class Dialogs {

        /** Dialog identifier for layers selection. */
        private static final int SELECT_LAYERS = 0;

        /** "About" dialog box. */
        public static final int ABOUT = 1;
    }

}
