package fr.itinerennes.ui.activity;

import org.andnav.osm.events.MapListener;
import org.andnav.osm.events.ScrollEvent;
import org.andnav.osm.events.ZoomEvent;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.ui.views.MapView;
import fr.itinerennes.ui.views.MapViewController;

/**
 * This is the main activity. Uses the <code>main_map.xml</code> layout and displays a menu bar on
 * top and a map view at center of the screen.
 * 
 * @author Jérémie Huchet
 */
public class MapActivity extends Activity implements MapListener {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MapActivity.class);

    /** The map view. */
    private MapView map;

    /** The my location overlay. */
    private MyLocationOverlay myLocation;

    /** The map controller. */
    private MapViewController controller;

    /**
     * Called when activity starts.
     * <p>
     * Displays the view.
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_map);

        this.map = (MapView) findViewById(R.id.map);
        map.setMapListener(this);
        map.setBuiltInZoomControls(true);
        // map.setMultiTouchControls(true);

        this.myLocation = new MyLocationOverlay(this.getBaseContext(), map);
        myLocation.enableMyLocation();
        map.getOverlays().add(myLocation);

        final GeoPoint rennes = new GeoPoint(ItineRennesConstants.CONFIG_RENNES_LAT,
                ItineRennesConstants.CONFIG_RENNES_LON);
        map.getController().setZoom(ItineRennesConstants.CONFIG_DEFAULT_ZOOM);
        map.getController().setCenter(rennes);

        final Button myLocation = (Button) findViewById(R.id.button_myPosition);
        myLocation.setOnClickListener(new MyLocationClickListener());
    }

    /**
     * Called when the user scrolls the map. Deactivate the location following.
     * 
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public boolean onScroll(final ScrollEvent event) {

        return false;
    }

    /**
     * Called when the user zoom in or out the map.
     * 
     * @see org.andnav.osm.events.MapListener#onZoom(org.andnav.osm.events.ZoomEvent)
     */
    @Override
    public boolean onZoom(final ZoomEvent event) {

        return false;
    }

    /**
     * An event listener which will focus the map to the current location given by GPS and activate
     * location following.
     * 
     * @author Jérémie Huchet
     */
    private class MyLocationClickListener implements OnClickListener {

        /**
         * When triggered, centers the map to the current location and adapt zoom level to location
         * precision. Adds an overlay icon to the current place of the user and activates GPS
         * location following (the overlay icon will move and the map will stay centered on the
         * icon).
         * 
         * @param button
         *            the view that was clicked
         */
        @Override
        public void onClick(final View button) {

            myLocation.followLocation(true);
        }

    }
}
