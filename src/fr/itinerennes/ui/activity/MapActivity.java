package fr.itinerennes.ui.activity;

import org.andnav.osm.events.DelayedMapListener;
import org.andnav.osm.util.GeoPoint;
import org.andnav.osm.views.overlay.MyLocationOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ZoomControls;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.business.facade.BikeService;
import fr.itinerennes.business.facade.BusService;
import fr.itinerennes.business.facade.StationProvider;
import fr.itinerennes.business.facade.SubwayService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BikeStation;
import fr.itinerennes.model.Station;
import fr.itinerennes.ui.views.MapQuestRenderer;
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

    /** Dialog identifier for layers selection. */
    private static final int DIALOG_SELECT_LAYERS = 0;

    /** Map listener event delayed listener constant value. */
    private static final long MAP_LISTNER_DELAY = 800;

    /** The map view. */
    private MapView map;

    /** The my location overlay. */
    private MyLocationOverlay myLocation;

    /** The database helper. */
    private DatabaseHelper dbHelper;

    /** The station providers. */
    private final StationProvider[] stationProviders = new StationProvider[3];

    /** The focused station layout. */
    private LinearLayout focusedBoxLayout;

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

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_map);

        this.map = (MapView) findViewById(R.id.map);

        map.setRenderer(new MapQuestRenderer());

        map.setMapListener(new DelayedMapListener(this.map, MAP_LISTNER_DELAY));

        // Zoom Buttons
        map.setBuiltInZoomControls(false);
        final ZoomControls zoomControls = (ZoomControls) findViewById(R.id.zoom_controls);
        zoomControls.setOnZoomInClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                map.getController().setZoom(map.getZoomLevel() + 1);
            }
        });
        zoomControls.setOnZoomOutClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                map.getController().setZoom(map.getZoomLevel() - 1);
            }
        });

        // center of the map
        if (savedInstanceState != null) {
            startZoomLevel = savedInstanceState.getInt("ZoomLevel");
            startMapCenter = (GeoPoint) savedInstanceState.getSerializable("startCenter");
        } else {
            final int latitude = ItineRennesConstants.CONFIG_RENNES_LAT;
            final int longitude = ItineRennesConstants.CONFIG_RENNES_LON;
            startZoomLevel = ItineRennesConstants.CONFIG_DEFAULT_ZOOM;
            startMapCenter = new GeoPoint(latitude, longitude);
        }

        // map.setMultiTouchControls(true);

        // TJHU remapper ce code sur un nouveau bouton mylocation
        // this.myLocation = new MyLocationOverlay(this.getBaseContext(), map);
        // myLocation.enableMyLocation();
        // map.getOverlays().add(myLocation);
        // final ImageView myLocationButton = (ImageView) findViewById(R.id.button_myPosition);
        // myLocationButton.setOnClickListener(new MyLocationClickListener());

        // DEBUG
        // map.getOverlays().add(new DebugOverlay(getBaseContext()));

        dbHelper = new DatabaseHelper(getBaseContext());
        stationProviders[Station.TYPE_BIKE] = new BikeService(dbHelper);
        stationProviders[Station.TYPE_BUS] = new BusService(dbHelper);
        stationProviders[Station.TYPE_SUBWAY] = new SubwayService(dbHelper);
        this.map.setStationProviders(stationProviders);

        /**
         * The gesture listener to be used on the station overlay.
         */
        this.map.setOnItemGestureListener(new OpenStreetMapViewItemizedOverlay.OnItemGestureListener<StationOverlayItem>() {

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

                if (focusedBoxLayout == null) {
                    focusedBoxLayout = (LinearLayout) findViewById(R.id.focused_box);
                    focusedBoxLayout.setOnClickListener(new OnStationBoxClickListener());
                }

                final ShowStationBoxTask showStationBoxTask = new ShowStationBoxTask(item
                        .getStation().getType());

                final LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                inflater.inflate(R.layout.station_box, focusedBoxLayout);

                showStationBoxTask.execute(stationProviders[item.getStation().getType()], item
                        .getStation().getId());

                final TextView title = (TextView) focusedBoxLayout.findViewById(R.id.station_name);
                title.setText(item.getStation().getName());

                final ImageView icon = (ImageView) focusedBoxLayout.findViewById(R.id.station_icon);
                icon.setImageDrawable(getBaseContext().getResources().getDrawable(
                        item.getStation().getIconDrawableId()));

                focusedBoxLayout.setVisibility(View.VISIBLE);

                focusedBoxLayout.setTag(R.id.selectedStation, item);

                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final StationOverlayItem item) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("OnItemGestureListener.onItemLongPress");
                }

                return false;
            }

        });

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
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected final void onDestroy() {

        dbHelper.close();
        super.onDestroy();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onWindowFocusChanged(boolean)
     */
    @Override
    public void onWindowFocusChanged(final boolean hasFocus) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onWindowFocusChanged");
        }

        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            map.getController().setZoom(startZoomLevel);
            map.getController().setCenter(startMapCenter);
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

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("myLocation.button.click");
            }
            myLocation.followLocation(true);
        }

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

    /**
     * An event listener which will trigger click event on the focused station box and open the
     * station activity.
     * 
     * @author Olivier Boudet
     */
    private class OnStationBoxClickListener implements OnClickListener {

        /**
         * @param view
         *            the view that was clicked
         */
        @Override
        public void onClick(final View view) {

            final StationOverlayItem stationItem = (StationOverlayItem) view
                    .getTag(R.id.selectedStation);

            Intent myIntent = null;

            if (stationItem.getStation().getType() == Station.TYPE_BIKE) {
                // BikeStationActivity development is temporarily stopped
                // myIntent = new Intent(MapActivity.this, BikeStationActivity.class);
            } else if (stationItem.getStation().getType() == Station.TYPE_BUS) {
                myIntent = new Intent(MapActivity.this, BusStationActivity.class);
            } else if (stationItem.getStation().getType() == Station.TYPE_SUBWAY) {
                // SubwayStationActivity development is temporarily stopped
                // myIntent = new Intent(MapActivity.this, SubwayStationActivity.class);
            }

            myIntent.putExtra("item", stationItem.getStation().getId());

            startActivity(myIntent);
        }
    }

    public final void fillStationBox(final Station station) {

        if (station != null) {
            // save previous padding because changing the background erase it
            final int top = focusedBoxLayout.getPaddingTop();
            final int left = focusedBoxLayout.getPaddingLeft();
            final int right = focusedBoxLayout.getPaddingRight();
            final int bottom = focusedBoxLayout.getPaddingBottom();
            // restore default background
            focusedBoxLayout.setBackgroundResource(R.drawable.map_box_background);

            switch (station.getType()) {
            case Station.TYPE_BIKE:

                final BikeStation bikeStation = (BikeStation) station;
                final TextView availablesSlots = (TextView) focusedBoxLayout
                        .findViewById(R.id.available_slots);
                availablesSlots.setText(String.valueOf(bikeStation.getAvailableSlots()));

                final TextView availablesBikes = (TextView) focusedBoxLayout
                        .findViewById(R.id.available_bikes);
                availablesBikes.setText(String.valueOf(bikeStation.getAvailableBikes()));

                final ProgressBar gauge = (ProgressBar) focusedBoxLayout
                        .findViewById(R.id.bike_station_gauge);
                gauge.setMax(bikeStation.getAvailableBikes() + bikeStation.getAvailableSlots());
                gauge.setIndeterminate(false);
                gauge.setProgress(bikeStation.getAvailableBikes());
                gauge.setSecondaryProgress(bikeStation.getAvailableBikes()
                        + bikeStation.getAvailableSlots());
                break;
            case Station.TYPE_BUS:
                focusedBoxLayout.setBackgroundResource(R.drawable.map_box_background_right_arrow);
                break;
            case Station.TYPE_SUBWAY:
                break;
            default:
                break;
            }

            // restore previsous padding
            focusedBoxLayout.setPadding(left, top, right, bottom);
        }
    }

    /**
     * A class derivating from ASyncTask to fetch informations of a station and show it in the box
     * at the top of the map.
     * 
     * @author Olivier Boudet
     */
    private class ShowStationBoxTask extends AsyncTask<Object, Void, Station> {

        /** The type of the station. */
        private final int type;

        /**
         * Creates the task to fill the station box.
         * 
         * @param stationType
         *            the type of the station
         */
        public ShowStationBoxTask(final int stationType) {

            this.type = stationType;
        }

        @Override
        protected void onPreExecute() {

            if (Station.TYPE_BIKE == type) {
                focusedBoxLayout.findViewById(R.id.bike_station_box).setVisibility(View.VISIBLE);
            } else if (focusedBoxLayout.findViewById(R.id.station_progressbar) != null) {
                focusedBoxLayout.findViewById(R.id.station_progressbar).setVisibility(View.VISIBLE);
            }
        }

        /**
         * Fetch in background the stations.
         * 
         * @param params
         *            The first object in this array in the station provider to use. The second is
         *            the id of the station.
         * @return the station
         */
        @Override
        protected final Station doInBackground(final Object... params) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("doInBackground.start");
            }

            Station station = null;
            try {
                station = ((StationProvider<Station>) params[0]).getStation((String) params[1]);
            } catch (final GenericException e) {
                LOGGER.error("error while trying to fetch station.", e);
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("doInBackground.end - {} station", station);
            }
            return station;
        }

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final Station station) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onPostExecute.start");
            }

            fillStationBox(station);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onPostExecute.end");
            }

            if (focusedBoxLayout.findViewById(R.id.station_progressbar) != null) {
                focusedBoxLayout.findViewById(R.id.station_progressbar).setVisibility(View.GONE);
            }
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    public final void onSaveInstanceState(final Bundle savedInstanceState) {

        startMapCenter = new GeoPoint(map.getMapCenterLatitudeE6(), map.getMapCenterLongitudeE6());
        savedInstanceState.putSerializable("startCenter", startMapCenter);
        savedInstanceState.putInt("ZoomLevel", map.getZoomLevel());

        super.onSaveInstanceState(savedInstanceState);
    }

}
