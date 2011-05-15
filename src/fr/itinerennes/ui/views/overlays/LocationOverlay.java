package fr.itinerennes.ui.views.overlays;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.ToggleButton;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.ItinerennesMapView;

public class LocationOverlay extends MyLocationOverlay {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(LocationOverlay.class);

    private final ItineRennesActivity context;

    /** The map on which this location overlay is attached. */
    private final ItinerennesMapView map;

    /** Delay for desactivate follow location on map move event. */
    private static final long MAP_MOVE_DELAY = 500;

    public LocationOverlay(final Context ctx, final MapView mapView) {

        super(ctx, mapView);
        this.context = (ItineRennesActivity) ctx;
        this.map = (ItinerennesMapView) mapView;
    }

    /**
     * Toggle FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    public void toggleFollowLocation() {

        if (isFollowLocationEnabled()) {
            disableFollowLocation();
        } else {
            enableFollowLocation();
        }
    }

    /**
     * Disable FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    @Override
    public final void disableFollowLocation() {

        super.disableFollowLocation();
        disableMyLocation();
        ((ToggleButton) this.context.findViewById(R.id.mylocation_button)).setChecked(false);

    }

    /**
     * Enable FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    @Override
    public void enableFollowLocation() {

        enableMyLocation();
        super.enableFollowLocation();
        ((ToggleButton) this.context.findViewById(R.id.mylocation_button)).setChecked(true);
        map.getController().setZoom(ItineRennesConstants.CONFIG_DEFAULT_ZOOM);

    }

    @Override
    public boolean onTouchEvent(final MotionEvent event, final MapView mapView) {

        if (event.getAction() == MotionEvent.ACTION_MOVE
                && (SystemClock.uptimeMillis() - event.getDownTime() >= MAP_MOVE_DELAY)) {

            if (isFollowLocationEnabled()) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Disabling follow location because of a touch event on the map");
                }
                disableFollowLocation();
            }
        }
        return false;
    }

}
