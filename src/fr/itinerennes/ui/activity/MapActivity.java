package fr.itinerennes.ui.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.osmdroid.util.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import fr.itinerennes.ITRPrefs;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.ui.views.ITRMapView;
import fr.itinerennes.ui.views.overlays.LocationOverlay;
import fr.itinerennes.ui.views.overlays.MapOverlayHelper;
import fr.itinerennes.ui.views.overlays.OverlayConstants;
import fr.itinerennes.ui.views.overlays.SelectableOverlay;

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

    /** Activity request code for preload. */
    public static final int ACTIVITY_REQUEST_PRELOAD = 0;

    /** Duration of toast messages. */
    private static final int TOAST_DURATION = 300;

    /** The map view. */
    private ITRMapView map;

    /** The map box view. */
    private View mapBox;

    /** The my location overlay. */
    private LocationOverlay myLocation;

    /** Location manager. */
    private LocationManager locationManager;

    /**
     * Called when activity starts. Displays the view.
     * <p>
     * {@inheritDoc}
     * </p>
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public final void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);

        final SharedPreferences sharedPreferences = getITRPreferences();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setContentView(R.layout.main_map);

        this.map = (ITRMapView) findViewById(R.id.map);
        this.mapBox = findViewById(R.id.map_box);

        this.myLocation = map.getController().getMapOverlayHelper().getLocationOverlay();

        map.setMultiTouchControls(true);

        final int zoomToRestore = sharedPreferences.getInt(ITRPrefs.MAP_ZOOM_LEVEL,
                ItineRennesConstants.CONFIG_DEFAULT_ZOOM);
        map.getController().setZoom(zoomToRestore);
        final int latToRestore = sharedPreferences.getInt(ITRPrefs.MAP_CENTER_LAT,
                ItineRennesConstants.CONFIG_RENNES_LAT);
        final int lonToRestore = sharedPreferences.getInt(ITRPrefs.MAP_CENTER_LON,
                ItineRennesConstants.CONFIG_RENNES_LON);
        map.getController().setCenter(new GeoPoint(latToRestore, lonToRestore));

        // if first start of the application, open the preload dialog
        if (sharedPreferences.getBoolean(ITRPrefs.DISPLAY_CACHE_ADVICE, true)) {
            final Intent i = new Intent(this, PreloadActivity.class);
            startActivityForResult(i, ACTIVITY_REQUEST_PRELOAD);
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

        final SharedPreferences sharedPreferences = getITRPreferences();
        // if a location provider is enabled and follow location is activated in preferences, the
        // follow location feature is enabled
        if (sharedPreferences.getBoolean(ITRPrefs.MAP_SHOW_LOCATION, true)
                && (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            myLocation.enableFollowLocation();
        }

        // restore previous displayed overlays (only if not displaying preload dialog)
        if (!sharedPreferences.getBoolean(ITRPrefs.DISPLAY_CACHE_ADVICE, true)) {
            final MapOverlayHelper moh = map.getController().getMapOverlayHelper();
            if (sharedPreferences.getBoolean(ITRPrefs.OVERLAY_BUS_ACTIVATED, true)) {
                moh.getBusStationOverlay().setEnabled(true);
            }
            if (sharedPreferences.getBoolean(ITRPrefs.OVERLAY_BIKE_ACTIVATED, true)) {
                moh.getBikeStationOverlay().setEnabled(true);
            }
            if (sharedPreferences.getBoolean(ITRPrefs.OVERLAY_SUBWAY_ACTIVATED, true)) {
                moh.getSubwayStationOverlay().setEnabled(true);
            }
        }
        super.onResume();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onResume.end");
        }
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
        super.onPause();

        // saving in preferences the state of the map (center, follow location and zoom)
        final SharedPreferences.Editor edit = getITRPreferences().edit();
        edit.putInt(ITRPrefs.MAP_CENTER_LAT, map.getMapCenterLatitudeE6());
        edit.putInt(ITRPrefs.MAP_CENTER_LON, map.getMapCenterLongitudeE6());
        edit.putInt(ITRPrefs.MAP_ZOOM_LEVEL, map.getZoomLevel());
        edit.putBoolean(ITRPrefs.MAP_SHOW_LOCATION, myLocation.isMyLocationEnabled());

        // save current displayed overlays
        final MapOverlayHelper moh = map.getController().getMapOverlayHelper();
        edit.putBoolean(ITRPrefs.OVERLAY_BUS_ACTIVATED, moh.getBusStationOverlay().isEnabled());
        edit.putBoolean(ITRPrefs.OVERLAY_BIKE_ACTIVATED, moh.getBikeStationOverlay().isEnabled());
        edit.putBoolean(ITRPrefs.OVERLAY_SUBWAY_ACTIVATED, moh.getSubwayStationOverlay()
                .isEnabled());
        // valid modifications
        edit.commit();

        myLocation.disableFollowLocation();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPause.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected final void onNewIntent(final Intent intent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.start");
        }
        super.onNewIntent(intent);

        // if intent asks for a specific zoom level, its values overrides the ones
        // saved when the activity was paused
        final int newZoom = intent.getIntExtra(INTENT_SET_MAP_ZOOM, map.getZoomLevel());
        // same thing with the map center
        final int newLat = intent.getIntExtra(INTENT_SET_MAP_LAT, map.getMapCenterLatitudeE6());
        final int newLon = intent.getIntExtra(INTENT_SET_MAP_LON, map.getMapCenterLongitudeE6());
        if (LOGGER.isDebugEnabled()) {
            if (map.getZoomLevel() != newZoom) {
                LOGGER.debug("intent requested a new zoom level : old={}, new={}",
                        map.getZoomLevel(), newZoom);
            }
            if (map.getMapCenterLatitudeE6() != newLat) {
                LOGGER.debug("intent requested a new latitude : old={}, new={}",
                        map.getMapCenterLatitudeE6(), newLat);
            }
            if (map.getMapCenterLongitudeE6() != newLon) {
                LOGGER.debug("intent requested a new longitude : old={}, new={}",
                        map.getMapCenterLongitudeE6(), newLon);
            }
        }
        map.getController().setZoom(newZoom);
        map.getController().setCenter(new GeoPoint(newLat, newLon));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected final void onActivityResult(final int requestCode, final int resultCode,
            final Intent data) {

        switch (requestCode) {
        case ACTIVITY_REQUEST_PRELOAD:
            if (RESULT_CANCELED == resultCode) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Preload canceled");
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Preload done");
                }
                final Editor edit = getITRPreferences().edit();
                edit.putBoolean(ITRPrefs.DISPLAY_CACHE_ADVICE, false);
                edit.commit();

                // active overlays when receive prelod result
                final MapOverlayHelper moh = map.getController().getMapOverlayHelper();
                moh.getSubwayStationOverlay().setEnabled(true);
                moh.getBusStationOverlay().setEnabled(true);
                moh.getBikeStationOverlay().setEnabled(true);
            }
            break;

        default:
            break;
        }
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

        // LAYERS
        final MenuItem layers = menu.add(Menu.NONE, MenuOptions.LAYERS, Menu.NONE,
                R.string.menu_layers);
        layers.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(final MenuItem item) {

                showDialog(Dialogs.SELECT_LAYERS);
                return true;
            }
        });

        // BOOKMARKS
        final MenuItem favourites = menu.add(Menu.NONE, MenuOptions.BOOKMARKS, Menu.NONE,
                R.string.menu_bookmarks);
        favourites.setIcon(android.R.drawable.btn_star);
        final Intent favActivityIntent = new Intent(this, BookmarksActivity.class);
        favourites.setIntent(favActivityIntent);

        // RE-PRELOAD
        final MenuItem rePreload = menu.add(Menu.NONE, MenuOptions.PRELOAD, Menu.NONE,
                R.string.menu_preload);
        // favourites.setIcon(android.R.drawable.btn_star);
        final Intent rePreloadIntent = new Intent(this, PreloadActivity.class);
        rePreload.setIntent(rePreloadIntent);

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
            final MapOverlayHelper overlayHelper = map.getController().getMapOverlayHelper();
            final List<SelectableOverlay<?>> allOverlays = overlayHelper.getToggleableOVerlays();
            final Map<String, SelectableOverlay<?>> namesToOverlays = new HashMap<String, SelectableOverlay<?>>();
            final String[] options = new String[allOverlays.size()];
            final boolean[] selections = new boolean[allOverlays.size()];

            for (int i = 0; i < options.length; i++) {
                options[i] = allOverlays.get(i).getLocalizedName();
                selections[i] = allOverlays.get(i).isEnabled();
                namesToOverlays.put(options[i], allOverlays.get(i));
            }

            final Map<String, Boolean> results = new HashMap<String, Boolean>();

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.select_layer);
            // TJHU faire une icone calque builder.setIcon(id);
            builder.setMultiChoiceItems(options, selections,
                    new DialogInterface.OnMultiChoiceClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which,
                                final boolean isChecked) {

                            results.put(options[which], isChecked);
                        }
                    });
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, final int which) {

                    for (final Entry<String, Boolean> userInput : results.entrySet()) {
                        namesToOverlays.get(userInput.getKey()).setEnabled(userInput.getValue());
                    }
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
     * Class containing menu constants.
     * 
     * @author Jérémie Huchet
     */
    private static final class MenuOptions {

        /** "About" menu option. */
        private static final int ABOUT = 0;

        /** "Favourites" menu option. */
        private static final int BOOKMARKS = 1;

        /** "Layers" menu option. */
        private static final int LAYERS = 2;

        /** "Re-Preload" menu option. */
        private static final int PRELOAD = 3;
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
        private static final int ABOUT = 1;
    }

}
