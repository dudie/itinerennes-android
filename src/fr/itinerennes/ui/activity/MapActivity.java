package fr.itinerennes.ui.activity;

import org.andnav.osm.util.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.ui.views.MapBoxView;
import fr.itinerennes.ui.views.MapView;
import fr.itinerennes.ui.views.overlays.LocationOverlay;
import fr.itinerennes.ui.views.overlays.MapOverlayHelper;
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

    /** Dialog identifier for layers selection. */
    private static final int DIALOG_SELECT_LAYERS = 0;

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

        final MapOverlayHelper overlayHelper = new MapOverlayHelper(this, map);
        overlayHelper.show(BUS_STATIONS | BIKE_STATIONS | SUBWAY_STATIONS);

        // center of the map
        if (savedInstanceState != null) {
            startZoomLevel = savedInstanceState.getInt("ZoomLevel");
            startMapCenter = (GeoPoint) savedInstanceState.getSerializable("startCenter");

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("restoring map zoom : {}", startZoomLevel);
                LOGGER.debug("restoring map center : LON={} LAT={}",
                        startMapCenter.getLongitudeE6(), startMapCenter.getLatitudeE6());
            }
        } else {
            final int latitude = ItineRennesConstants.CONFIG_RENNES_LAT;
            final int longitude = ItineRennesConstants.CONFIG_RENNES_LON;
            startZoomLevel = ItineRennesConstants.CONFIG_DEFAULT_ZOOM;
            startMapCenter = new GeoPoint(latitude, longitude);
        }
        map.getController().setZoom(startZoomLevel);
        map.getController().setCenter(startMapCenter);

        // map.setMultiTouchControls(true);

        myLocation = new LocationOverlay(this, map);
        map.getOverlays().add(myLocation);

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
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("restoring map zoom : {}", startZoomLevel);
                LOGGER.debug("restoring map center : LON={} LAT={}",
                        startMapCenter.getLongitudeE6(), startMapCenter.getLatitudeE6());
            }
            map.getController().setZoom(startZoomLevel);
            map.getController().setCenter(startMapCenter);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onWindowFocusChanged.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {

        myLocation.toggleFollowLocation();

        super.onResume();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {

        if (myLocation.isLocationFollowEnabled()) {
            myLocation.toggleFollowLocation();
        }
        super.onPause();
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

        startMapCenter = new GeoPoint(map.getMapCenterLatitudeE6(), map.getMapCenterLongitudeE6());
        savedInstanceState.putSerializable("startCenter", startMapCenter);
        savedInstanceState.putInt("ZoomLevel", map.getZoomLevel());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("saving map zoom : {}", map.getZoomLevel());
            LOGGER.debug("saving map center : LON={} LAT={}", startMapCenter.getLongitudeE6(),
                    startMapCenter.getLatitudeE6());
        }

        super.onSaveInstanceState(savedInstanceState);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSaveInstanceState.end");
        }
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

        myLocation.toggleFollowLocation();

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
        case DIALOG_SELECT_LAYERS:
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
            showDialog(DIALOG_SELECT_LAYERS);
        }

    }

}
