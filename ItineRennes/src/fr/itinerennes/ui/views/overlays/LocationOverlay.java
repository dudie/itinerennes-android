package fr.itinerennes.ui.views.overlays;

import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ITRContext;

public class LocationOverlay extends MyLocationOverlay {

    private final ITRContext context;

    public LocationOverlay(Context ctx, OpenStreetMapView mapView) {

        super(ctx, mapView);
        this.context = (ITRContext) ctx;
    }

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(LocationOverlay.class);

    @Override
    public boolean onTouchEvent(MotionEvent event, OpenStreetMapView mapView) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final ToggleButton myLocationButton = (ToggleButton) context
                    .findViewById(R.id.mylocation_button);
            myLocationButton.setChecked(false);
        }
        return super.onTouchEvent(event, mapView);
    }

    /**
     * Toggle FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    public void toggleFollowLocation() {

        if (isLocationFollowEnabled()) {
            followLocation(false);
            disableMyLocation();
        } else {
            enableMyLocation();
            followLocation(true);
        }
    }

}
