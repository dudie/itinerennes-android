package fr.itinerennes.ui.activity;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.util.constants.OverlayConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import fr.itinerennes.Conf;
import fr.itinerennes.ITRPrefs;
import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.database.Columns;
import fr.itinerennes.database.MarkerDao;
import fr.itinerennes.ui.preferences.MainPreferenceActivity;
import fr.itinerennes.ui.views.ItinerennesMapView;
import fr.itinerennes.ui.views.overlays.ILayerSelector;
import fr.itinerennes.ui.views.overlays.LayerDescriptor;
import fr.itinerennes.ui.views.overlays.LocationOverlay;
import fr.itinerennes.ui.views.overlays.StopOverlayItem;
import fr.itinerennes.utils.MapUtils;

/**
 * This is the main activity. Uses the <code>main_map.xml</code> layout and
 * displays a menu bar on top and a map view at center of the screen.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class MapActivity extends ItineRennesActivity implements
        OverlayConstants {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MapActivity.class);

    /** The map view. */
    private ItinerennesMapView map;

    /** The my location overlay. */
    private LocationOverlay myLocation;

    /** Location manager. */
    private LocationManager locationManager;

    /**
     * Called when activity starts. Displays the view.
     * <p>
     * {@inheritDoc}
     * </p>
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        setContentView(R.layout.act_map);

        this.map = (ItinerennesMapView) findViewById(R.id.map);

        this.myLocation = map.getMyLocationOverlay();

        map.setMultiTouchControls(true);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected final void onResume() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onResume.start");
        }

        final SharedPreferences sharedPreferences = getApplicationContext()
                .getITRPreferences();
        final int zoomToRestore = sharedPreferences.getInt(
                ITRPrefs.MAP_ZOOM_LEVEL, Conf.MAP_DEFAULT_ZOOM);
        map.getController().setZoom(zoomToRestore);
        final int latToRestore = sharedPreferences.getInt(
                ITRPrefs.MAP_CENTER_LAT, Conf.MAP_RENNES_LAT);
        final int lonToRestore = sharedPreferences.getInt(
                ITRPrefs.MAP_CENTER_LON, Conf.MAP_RENNES_LON);
        map.getController().setCenter(new GeoPoint(latToRestore, lonToRestore));

        final SharedPreferences defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final String tileSource = defaultSharedPrefs.getString(ITRPrefs.MAP_TILE_PROVIDER, "Itinerennes");
        map.getController().setTileSource(tileSource);

        // if a location provider is enabled and follow location is activated in
        // preferences, the
        // follow location feature is enabled
        if (sharedPreferences.getBoolean(ITRPrefs.MAP_SHOW_LOCATION, true)
                && (locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            myLocation.enableFollowLocation();
        }

        if (!sharedPreferences.getBoolean(ITRPrefs.OVERLAY_BUS_ACTIVATED, true)) {
            map.getStopOverlay().hide(TypeConstants.TYPE_BUS);
        }
        if (!sharedPreferences
                .getBoolean(ITRPrefs.OVERLAY_BIKE_ACTIVATED, true)) {
            map.getStopOverlay().hide(TypeConstants.TYPE_BIKE);
        }
        if (!sharedPreferences.getBoolean(ITRPrefs.OVERLAY_SUBWAY_ACTIVATED,
                true)) {
            map.getStopOverlay().hide(TypeConstants.TYPE_SUBWAY);
        }
        if (!sharedPreferences
                .getBoolean(ITRPrefs.OVERLAY_PARK_ACTIVATED, true)) {
            map.getParkOverlay().hide(TypeConstants.TYPE_CAR_PARK);
        }

        super.onResume();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onResume.end");
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
        super.onPause();

        final SharedPreferences.Editor edit = getApplicationContext()
                .getITRPreferences().edit();

        // saving in preferences the state of the map (center, follow location
        // and zoom)
        saveMapCenterInPreferences(edit, map.getMapCenter().getLatitudeE6(),
                map.getMapCenter().getLongitudeE6(), map.getZoomLevel());

        // save current displayed overlays
        saveVisibleOverlaysInPreferences(edit);

        edit.commit();

        myLocation.disableMyLocation();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPause.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_layers:
            showDialog(Dialogs.SELECT_LAYERS);
            return true;
        case R.id.menu_bookmarks:
            startActivity(new Intent(this, BookmarksActivity.class));
            return true;
        case R.id.menu_preferences:
            startActivity(new Intent(this, MainPreferenceActivity.class));
            return true;
        case R.id.menu_search:
            onSearchRequested();
            return true;
        case R.id.menu_network_alerts:
            startActivity(new Intent(this, NetworkAlertsActivity.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Click method handler invoked when a click event is detected on the my
     * location button.
     * 
     * @param button
     *            the button view on which the event was detected
     */
    public final void onMyLocationButtonClick(final View button) {

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                && !locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, R.string.location_service_disabled,
                    Conf.TOAST_DURATION).show();
            ((ToggleButton) button).setChecked(false);
        } else {
            myLocation.toggleFollowLocation();
        }
    }

    /**
     * Click method handler invoked when a click event is detected on the layers
     * button.
     * 
     * @param button
     *            the button view on which the event was detected
     */
    public final void onLayersButtonClick(final View button) {

        showDialog(Dialogs.SELECT_LAYERS);
    }

    /**
     * Click method handler invoked when a click event is detected on the search
     * button.
     * 
     * @param button
     *            the button view on which the event was detected
     */
    public final void onSearchButtonClick(final View button) {

        onSearchRequested();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected final Dialog onCreateDialog(final int id) {

        AlertDialog dialog;
        switch (id) {
        case Dialogs.SELECT_LAYERS:

            final List<LayerDescriptor> allLabels = new ArrayList<LayerDescriptor>();

            final Iterator<Overlay> overlayIterator = map.getOverlayManager()
                    .iterator();
            while (overlayIterator.hasNext()) {
                final Overlay overlay = overlayIterator.next();
                if (overlay instanceof ILayerSelector) {
                    allLabels.addAll(((ILayerSelector) overlay)
                            .getLayersDescriptors());
                }
            }

            final String[] options = new String[allLabels.size()];
            final boolean[] selections = new boolean[allLabels.size()];

            final Iterator<LayerDescriptor> labelIterator = allLabels
                    .iterator();
            int i = 0;
            while (labelIterator.hasNext()) {
                final LayerDescriptor entry = labelIterator.next();
                options[i] = entry.getLabel();
                selections[i] = entry.isVisible();
                i++;
            }

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.select_layer);
            // TJHU faire une icone calque builder.setIcon(id);

            builder.setMultiChoiceItems(options, selections,
                    new DialogInterface.OnMultiChoiceClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog,
                                final int which, final boolean isChecked) {

                            if (isChecked) {
                                allLabels.get(which).getOverlay()
                                        .show(allLabels.get(which).getType());
                            } else {
                                allLabels.get(which).getOverlay()
                                        .hide(allLabels.get(which).getType());
                            }

                        }
                    });

            builder.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog,
                                final int which) {

                            map.postInvalidate();
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
     * Writes in preferences the map center and zoom level.
     * 
     * @param edit
     *            editor to use to write preferences
     * @param latitude
     *            latitude to write in preferences
     * @param longitude
     *            longitude to write in preferences
     * @param zoom
     *            zoom level to write in preferences
     */
    private void saveMapCenterInPreferences(final Editor edit,
            final int latitude, final int longitude, final int zoom) {

        edit.putInt(ITRPrefs.MAP_CENTER_LAT, (latitude != 0) ? latitude
                : Conf.MAP_RENNES_LAT);
        edit.putInt(ITRPrefs.MAP_CENTER_LON, (longitude != 0) ? longitude
                : Conf.MAP_RENNES_LAT);
        edit.putInt(ITRPrefs.MAP_ZOOM_LEVEL, (zoom != 0) ? zoom
                : Conf.MAP_DEFAULT_ZOOM);

        edit.putBoolean(ITRPrefs.MAP_SHOW_LOCATION,
                myLocation.isMyLocationEnabled());
    }

    /**
     * Writes in preferences the visible overlays.
     * 
     * @param edit
     *            editor to use to write preferences
     */
    private void saveVisibleOverlaysInPreferences(final Editor edit) {

        edit.putBoolean(ITRPrefs.OVERLAY_BUS_ACTIVATED, map.getStopOverlay()
                .isVisible(TypeConstants.TYPE_BUS));
        edit.putBoolean(ITRPrefs.OVERLAY_BIKE_ACTIVATED, map.getStopOverlay()
                .isVisible(TypeConstants.TYPE_BIKE));
        edit.putBoolean(ITRPrefs.OVERLAY_SUBWAY_ACTIVATED, map.getStopOverlay()
                .isVisible(TypeConstants.TYPE_SUBWAY));
        edit.putBoolean(ITRPrefs.OVERLAY_PARK_ACTIVATED, map.getParkOverlay()
                .isVisible(null));

    }

    /**
     * Handles actions to do when a suggestion or a search result is clicked.
     * 
     * @param id
     *            id of the clicked result
     * @param intent
     *            intent sent by the search framework when a user clicks on a
     *            suggestion
     */
    private void onSearchResultClick(final String id, final Intent intent) {

        // if the last path segment is "nominatim", so the user has clicked the
        // link to
        // search in nominatim
        if (id.equals(MarkerDao.NOMINATIM_INTENT_DATA_ID)) {
            final Intent i = new Intent(getApplicationContext(),
                    SearchResultsActivity.class);
            i.putExtra(SearchManager.QUERY,
                    intent.getStringExtra(SearchManager.QUERY));
            startActivity(i);
        } else {
            // we fetch from database all items having the same label than the
            // item clicked
            // because search suggestions show only one row when multiple stops
            // have the
            // same label

            final Cursor c = getApplicationContext().getMarkerDao()
                    .getMarkersWithSameLabel(id);

            // calculate the barycenter to center the map on it
            if (c != null && c.moveToFirst()) {
                // check if marker type is visible, and add it if not visible
                final String type = c.getString(c
                        .getColumnIndex(Columns.MarkersColumns.TYPE));

                final GeoPoint barycentre = MapUtils.getBarycenter(c);

                c.close();

                if (barycentre != null) {
                    onNewIntent(IntentFactory.getCenterOnLocationIntent(
                            getApplicationContext(),
                            barycentre.getLatitudeE6(),
                            barycentre.getLongitudeE6(),
                            Conf.MAP_ZOOM_ON_LOCATION, type));
                }

            }

        }
    }

    /**
     * Class containing dialog boxes contants.
     * 
     * @author Jérémie Huchet
     */
    private static final class Dialogs {

        /** Dialog identifier for layers selection. */
        private static final int SELECT_LAYERS = 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected final void onNewIntent(final Intent intent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.start");
        }

        setIntent(intent);

        myLocation.disableFollowLocation();

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // forward to SearchResultsActivity
            intent.setClass(getApplicationContext(),
                    SearchResultsActivity.class);
            startActivity(intent);
        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // center on the latitude and longitude sent in the intent

            final SharedPreferences.Editor edit = getApplicationContext()
                    .getITRPreferences().edit();

            if (intent.hasExtra(IntentFactory.INTENT_PARAM_SET_MAP_LAT)
                    && intent.hasExtra(IntentFactory.INTENT_PARAM_SET_MAP_LON)) {
                // center coordinates are send in the intent
                final int newZoom = intent.getIntExtra(
                        IntentFactory.INTENT_PARAM_SET_MAP_ZOOM,
                        map.getZoomLevel());
                final int newLat = intent.getIntExtra(IntentFactory.INTENT_PARAM_SET_MAP_LAT, map
                        .getMapCenter().getLatitudeE6());
                final int newLon = intent.getIntExtra(IntentFactory.INTENT_PARAM_SET_MAP_LON, map
                        .getMapCenter().getLongitudeE6());

                saveMapCenterInPreferences(edit, newLat, newLon, newZoom);

                if (intent.hasExtra(IntentFactory.INTENT_PARAM_MARKER)) {

                    map.getMapBoxController()
                            .show((StopOverlayItem) intent
                                    .getSerializableExtra(IntentFactory.INTENT_PARAM_MARKER));
                }

            }

            if (intent.hasExtra(IntentFactory.INTENT_PARAM_MARKER_TYPE)) {
                final String type = intent
                        .getStringExtra(IntentFactory.INTENT_PARAM_MARKER_TYPE);
                if (!map.getStopOverlay().isVisible(type)) {
                    map.getStopOverlay().show(type);
                    saveVisibleOverlaysInPreferences(edit);
                }
            }

            edit.commit();

        } else if (IntentFactory.INTENT_SEARCH_SUGGESTION.equals(intent
                .getAction())) {
            // intent sent by suggestion click

            if (intent.hasExtra(SearchManager.USER_QUERY)) {
                onSearchResultClick(intent.getData().getLastPathSegment(),
                        intent);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.end");
        }

    }

    /**
     * Simple Factory to get intents to send to {@link MapActivity}.
     * 
     * @author Olivier Boudet
     */
    public static class IntentFactory {

        /** Intent parameter name to pass the map zoom level to set. */
        public static final String INTENT_PARAM_SET_MAP_ZOOM = String.format(
                "%s.setMapZoom", MapActivity.class.getName());

        /** Intent parameter name to pass the map latitude to set. */
        public static final String INTENT_PARAM_SET_MAP_LAT = String.format(
                "%s.setMapLatitude", MapActivity.class.getName());

        /** Intent parameter name to pass the map longitude to set. */
        public static final String INTENT_PARAM_SET_MAP_LON = String.format(
                "%s.setMapLongitude", MapActivity.class.getName());

        /** Intent parameter name to pass a marker type. */
        public static final String INTENT_PARAM_MARKER_TYPE = String.format(
                "%s.markerType", MapActivity.class.getName());

        /** Intent parameter name to use to pass a marker. */
        public static final String INTENT_PARAM_MARKER = String.format(
                "%s.marker", MapActivity.class.getName());

        /** Intent name to use to manage search suggestion. */
        public static final String INTENT_SEARCH_SUGGESTION = String.format(
                "%s.SEARCH_SUGGESTION", MapActivity.class.getName());

        /**
         * Returns an intent to open the map centered on a location.
         * 
         * @param context
         *            the contexxt
         * @param latitude
         *            latitude on which center the map
         * @param longitude
         *            longitude on which center the map
         * @param zoom
         *            zoom level of the centered map
         * @return an intent
         */
        public static Intent getCenterOnLocationIntent(
                final ItineRennesApplication context, final int latitude,
                final int longitude, final int zoom) {

            final Intent i = new Intent(context, MapActivity.class);
            i.setAction(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(INTENT_PARAM_SET_MAP_ZOOM, zoom);
            i.putExtra(INTENT_PARAM_SET_MAP_LON, longitude);
            i.putExtra(INTENT_PARAM_SET_MAP_LAT, latitude);

            return i;
        }

        /**
         * Returns an intent to open a mapbox and center the map on the mapbox
         * location. The map is centered on the given location, so the mapbox
         * could be invisible if the location is not the good one.
         * 
         * @param context
         *            the context
         * @param marker
         *            the marker on which open the mapbox
         * @param zoom
         *            zoom level of the centered map
         * @return an intent
         */
        public static Intent getOpenMapBoxIntent(
                final ItineRennesApplication context,
                final StopOverlayItem marker, final int zoom) {

            final Intent i = getCenterOnLocationIntent(context, marker
                    .getLocation().getLatitudeE6(), marker.getLocation()
                    .getLongitudeE6(), zoom);

            i.putExtra(INTENT_PARAM_MARKER, marker);
            return i;
        }

        /**
         * Returns an intent to open the map centered on a location and activate
         * a marker type if not already activated (useful for center the map on
         * a marker after a search).
         * 
         * @param context
         *            the context
         * @param latitude
         *            latitude on which center the map
         * @param longitude
         *            longitude on which center the map
         * @param zoom
         *            zoom level of the centered map
         * @param markerType
         *            a type of markers layer to activate on the map
         * @return an intent
         */
        public static Intent getCenterOnLocationIntent(final ItineRennesApplication context,
                final int latitude, final int longitude, final int zoom, final String markerType) {

            final Intent i = getCenterOnLocationIntent(context, latitude,
                    longitude, zoom);

            i.putExtra(INTENT_PARAM_MARKER_TYPE, markerType);

            return i;
        }

    }

}
