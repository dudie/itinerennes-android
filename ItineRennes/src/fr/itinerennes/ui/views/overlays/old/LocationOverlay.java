package fr.itinerennes.ui.views.overlays.old;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.ToggleButton;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.ui.activity.ItinerennesContext;
import fr.itinerennes.ui.views.ItinerennesMapView;

public class LocationOverlay extends MyLocationOverlay {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(LocationOverlay.class);

    private final ItinerennesContext context;

    /** Toggle Button to toogle the follow location feature. */
    private final ToggleButton myLocationButton;

    /** The map on which this location overlay is attached. */
    private final ItinerennesMapView map;

    /** Delay for desactivate follow location on map move event. */
    private static final long MAP_MOVE_DELAY = 500;

    public LocationOverlay(final Context ctx, final MapView mapView, final ToggleButton button) {

        super(ctx, mapView);
        this.context = (ItinerennesContext) ctx;
        this.myLocationButton = button;
        this.map = (ItinerennesMapView) mapView;
    }

    /**
     * Toggle FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    public void toggleFollowLocation() {

        if (isLocationFollowEnabled()) {
            disableFollowLocation();
        } else {
            enableFollowLocation();
        }
    }

    /**
     * Disable FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    public void disableFollowLocation() {

        followLocation(false);
        disableMyLocation();
        myLocationButton.setChecked(false);

    }

    /**
     * Enable FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    public void enableFollowLocation() {

        enableMyLocation();
        followLocation(true);
        myLocationButton.setChecked(true);
        map.getController().setZoom(ItineRennesConstants.CONFIG_DEFAULT_ZOOM);

    }

    @Override
    public boolean onTouchEvent(final MotionEvent event, final MapView mapView) {

        if (event.getAction() == MotionEvent.ACTION_MOVE
                && (SystemClock.uptimeMillis() - event.getDownTime() >= MAP_MOVE_DELAY)) {

            final LocationOverlay overlay = ((ItinerennesMapView) mapView).getController()
                    .getMapOverlayHelper().getLocationOverlay();
            if (overlay.isLocationFollowEnabled()) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Disabling follow location because of a touch event on the map");
                }
                overlay.disableFollowLocation();
            }
        }
        return false;
    }

}
