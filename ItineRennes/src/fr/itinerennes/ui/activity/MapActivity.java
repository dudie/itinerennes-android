package fr.itinerennes.ui.activity;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.beans.BikeStation;
import fr.itinerennes.beans.BoundingBox;
import fr.itinerennes.beans.Station;
import fr.itinerennes.business.facade.BikeService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.tasks.RefreshBikeOverlayTask;
import fr.itinerennes.ui.tasks.RefreshBusOverlayTask;
import fr.itinerennes.ui.views.MapView;
import fr.itinerennes.ui.views.overlays.StationOverlayItem;

/**
 * This is the main activity. Uses the <code>main_map.xml</code> layout and displays a menu bar on
 * top and a map view at center of the screen.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class MapActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(MapActivity.class);

    /** The map view. */
    private MapView map;

    /** The my location overlay. */
    private MyLocationOverlay myLocation;

    /** OnItemGestureListener. */
    private OnItemGestureListener<StationOverlayItem> onItemGestureListener;

    /**
     * Called when activity starts.
     * <p>
     * Displays the view.
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    public final void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_map);

        this.map = (MapView) findViewById(R.id.map);
        map.setMapListener(this.map);
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

        /**
         * The gesture listener to be used on the station overlay.
         */
        onItemGestureListener = new OpenStreetMapViewItemizedOverlay.OnItemGestureListener<StationOverlayItem>() {

            /**
             * Called when a single tap is intercepted on the overlay. Inflates the layout
             * corresponding to the item type and sets the focused layout box visible.
             * 
             * @param index
             *            index of the tapped up item in the overlay
             * @param item
             *            item tapped up
             * @return
             */
            @Override
            public boolean onItemSingleTapUp(final int index, final StationOverlayItem item) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("OnItemGestureListener.onItemSingleTapUp");
                }

                final LinearLayout focusedBoxLayout = (LinearLayout) findViewById(R.id.focused_box);
                final LayoutInflater inflater = LayoutInflater.from(getBaseContext());

                try {
                    switch (item.getStation().getType()) {
                    case Station.TYPE_BIKE:
                        inflater.inflate(R.layout.bike_station_box_layout, focusedBoxLayout);

                        final BikeStation bikeStation = BikeService.getStation(item.getStation()
                                .getId());

                        final TextView availablesSlots = (TextView) focusedBoxLayout
                                .findViewById(R.id.available_slots);
                        availablesSlots.setText(String.valueOf(bikeStation.getAvailableSlots()));

                        final TextView availablesBikes = (TextView) focusedBoxLayout
                                .findViewById(R.id.available_bikes);
                        availablesBikes.setText(String.valueOf(bikeStation.getAvailableBikes()));

                        break;
                    case Station.TYPE_BUS:
                        inflater.inflate(R.layout.bus_station_box_layout, focusedBoxLayout);

                        break;

                    default:
                        break;
                    }

                } catch (final GenericException e) {
                    LOGGER.error("Error while trying to fetch station informations.");

                }
                final TextView title = (TextView) focusedBoxLayout.findViewById(R.id.station_name);
                title.setText(item.getStation().getName());

                focusedBoxLayout.setVisibility(View.VISIBLE);
                map.setItemLayoutFocused(true);

                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final StationOverlayItem item) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("OnItemGestureListener.onItemLongPress");
                }

                return false;
            }

        };

    }

    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {

        super.onResume();
        if (hasFocus) {
            final BoundingBox bbox = new BoundingBox(this.map.getVisibleBoundingBoxE6());

            new RefreshBusOverlayTask(this.getBaseContext(), this.map, onItemGestureListener)
                    .execute(bbox);
            new RefreshBikeOverlayTask(this.getBaseContext(), this.map, onItemGestureListener)
                    .execute(bbox);
        }
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
