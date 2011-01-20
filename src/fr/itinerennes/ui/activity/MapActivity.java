package fr.itinerennes.ui.activity;

import org.andnav.osm.util.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.ui.views.MapBoxView;
import fr.itinerennes.ui.views.MapView;
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

    private static final int TOAST_DURATION = 300;

    /** The map view. */
    private MapView map;

    /** The my location overlay. */
    private LocationOverlay myLocation;

    /** The GeoPoint on which center the map when showing the activity. */
    private GeoPoint startMapCenter;

    /** The zoom level of the map when showing the activity. */
    private int startZoomLevel;

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

        setContentView(R.layout.main_map);

        this.map = (MapView) findViewById(R.id.map);

        this.map.getController().getMapOverlayHelper()
                .show(BUS_STATIONS | BIKE_STATIONS | SUBWAY_STATIONS | LOCATION);
        this.myLocation = map.getController().getMapOverlayHelper().getLocationOverlay();

        map.setMultiTouchControls(true);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // location provider not enabled. Centering in rennes
            startMapCenter = new GeoPoint(ItineRennesConstants.CONFIG_RENNES_LAT,
                    ItineRennesConstants.CONFIG_RENNES_LON);
            startZoomLevel = ItineRennesConstants.CONFIG_DEFAULT_ZOOM;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Location service is not enabled. Centering in Rennes.");
            }
        }

        // DEBUG
        // map.getOverlays().add(new DebugOverlay(getBaseContext()));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onWindowFocusChanged(boolean)
     */
    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onWindowFocusChanged.start - hasFocus={}", hasFocus);
        }

        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            if (startMapCenter != null) {

                map.getController().setZoom(startZoomLevel);
                map.getController().setCenter(startMapCenter);
            } else {
                myLocation.enableFollowLocation();
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onWindowFocusChanged.end");
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

        if (myLocation.isLocationFollowEnabled()) {
            myLocation.disableFollowLocation();
            startMapCenter = null;
        } else {
            startMapCenter = new GeoPoint(map.getMapCenterLatitudeE6(),
                    map.getMapCenterLongitudeE6());
            startZoomLevel = map.getZoomLevel();
        }

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

        final MenuItem about = menu.add(Menu.NONE, MenuOptions.ABOUT, Menu.NONE, R.string.about);
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
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public final void onSaveInstanceState(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSaveInstanceState.start");
        }

        if (myLocation.isLocationFollowEnabled()) {
            savedInstanceState.putBoolean("followLocation", myLocation.isLocationFollowEnabled());
        } else {
            savedInstanceState.putSerializable("startCenter",
                    new GeoPoint(map.getMapCenterLatitudeE6(), map.getMapCenterLongitudeE6()));
            savedInstanceState.putInt("ZoomLevel", map.getZoomLevel());
        }
        super.onSaveInstanceState(savedInstanceState);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSaveInstanceState.end");
        }
    }

    @Override
    protected void onRestoreInstanceState(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onRestoreInstanceState.start");
        }

        // center of the map
        if (savedInstanceState != null) {

            if (!savedInstanceState.getBoolean("followLocation")) {

                startZoomLevel = savedInstanceState.getInt("ZoomLevel");
                startMapCenter = (GeoPoint) savedInstanceState.getSerializable("startCenter");

            }

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onRestoreInstanceState.end");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Click method handler invoked when a click event is detected on the map box view.
     * 
     * @param boxView
     *            the map box view on which the event was detected
     */
    public final void onMapBoxClickHandler(final View boxView) {

        final Intent intent = ((MapBoxView) boxView).getOnClickIntent();
        if (null != intent) {
            startActivity(intent);
        }
    }

    /**
     * Click method handler invoked when a click event is detected on the my location button.
     * 
     * @param button
     *            the button view on which the event was detected
     */
    public final void onMyLocationButtonClick(final View button) {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
    protected Dialog onCreateDialog(final int id) {

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
            aboutBuilder.setTitle(R.string.about).setCancelable(true);
            final View aboutView = getLayoutInflater().inflate(R.layout.about, null);
            aboutBuilder.setView(aboutView);
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
