package fr.itinerennes.ui.views.overlays;

import org.andnav.osm.events.MapListener;
import org.andnav.osm.events.ScrollEvent;
import org.andnav.osm.events.ZoomEvent;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ITRContext;

public class LocationOverlay extends MyLocationOverlay implements MapListener {

    private final ITRContext context;

    public LocationOverlay(Context ctx, OpenStreetMapView mapView) {

        super(ctx, mapView);
        this.context = (ITRContext) ctx;
    }

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(LocationOverlay.class);

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

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public boolean onScroll(ScrollEvent arg0) {

        final ToggleButton myLocationButton = (ToggleButton) context
                .findViewById(R.id.mylocation_button);
        myLocationButton.setChecked(false);
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.events.MapListener#onZoom(org.andnav.osm.events.ZoomEvent)
     */
    @Override
    public boolean onZoom(ZoomEvent arg0) {

        // TODO Auto-generated method stub
        return false;
    }

}
