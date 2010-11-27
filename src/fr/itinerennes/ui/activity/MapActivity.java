package fr.itinerennes.ui.activity;

import java.util.ArrayList;
import java.util.List;

import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
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
import fr.itinerennes.beans.Station;
import fr.itinerennes.business.facade.BikeService;
import fr.itinerennes.business.service.KeolisService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.views.MapView;
import fr.itinerennes.ui.views.overlays.StationOverlay;
import fr.itinerennes.ui.views.overlays.StationOverlayItem;

/**
 * This is the main activity. Uses the <code>main_map.xml</code> layout and displays a menu bar on
 * top and a map view at center of the screen.
 * 
 * @author Jérémie Huchet
 */
public class MapActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(MapActivity.class);

    /** The map view. */
    private MapView map;

    /** The my location overlay. */
    private MyLocationOverlay myLocation;

    /** The station overlay */
    private StationOverlay<StationOverlayItem> stationOverlay;

    /** The keolis service */
    private KeolisService keolisService;

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

        /* Instantiation of various services (keolis, otp, geoserver) */

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

        try {
            /**
             * The gesture listener to be used on the station overlay.
             */
            final OpenStreetMapViewItemizedOverlay.OnItemGestureListener<StationOverlayItem> listener = new OpenStreetMapViewItemizedOverlay.OnItemGestureListener<StationOverlayItem>() {

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
                        case Station.TYPE_VELO:
                            inflater.inflate(R.layout.bike_station_box_layout, focusedBoxLayout);

                            final BikeStation bikeStation = BikeService.getStation(item
                                    .getStation().getId());

                            final TextView availables_slots = (TextView) focusedBoxLayout
                                    .findViewById(R.id.available_slots);
                            availables_slots
                                    .setText(String.valueOf(bikeStation.getAvailableSlots()));

                            final TextView availables_bikes = (TextView) focusedBoxLayout
                                    .findViewById(R.id.available_bikes);
                            availables_bikes
                                    .setText(String.valueOf(bikeStation.getAvailableBikes()));

                            break;

                        default:
                            break;
                        }
                    } catch (final GenericException e) {
                        LOGGER.error("Error while trying to fetch station informations.");
                    }
                    final TextView title = (TextView) focusedBoxLayout
                            .findViewById(R.id.station_name);
                    title.setText(item.getStation().getName());

                    focusedBoxLayout.setVisibility(View.VISIBLE);
                    stationOverlay.setFocused(true);
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
            stationOverlay = new StationOverlay<StationOverlayItem>(this.getBaseContext(),
                    getBikeStationOverlayItems(), listener);
            map.getOverlays().add(stationOverlay);

        } catch (final GenericException e) {
            // TOBO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Gets all bike stations from Keolis API and returns a list of station overlay items.
     * 
     * @return list of station overlay items
     * @throws GenericException
     */
    private List<StationOverlayItem> getBikeStationOverlayItems() throws GenericException {

        final List<BikeStation> bikeStations = BikeService.getAllStations();
        final List<StationOverlayItem> overlayItems = new ArrayList<StationOverlayItem>();

        for (final BikeStation station : bikeStations) {
            final StationOverlayItem item = new StationOverlayItem(station);
            item.setMarker(getResources().getDrawable(R.drawable.icon_velo));

            overlayItems.add(item);
        }
        return overlayItems;

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
