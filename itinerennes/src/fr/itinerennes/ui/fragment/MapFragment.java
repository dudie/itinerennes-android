package fr.itinerennes.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.ViewById;
import org.osmdroid.util.GeoPoint;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import fr.itinerennes.Conf;
import fr.itinerennes.ITRPrefs;
import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.ui.views.ItinerennesMapView;
import fr.itinerennes.ui.views.overlays.ILayerSelector;
import fr.itinerennes.ui.views.overlays.LayerDescriptor;
import fr.itinerennes.ui.views.overlays.LocationOverlay;

/**
 * @author Jeremie Huchet
 */
@EFragment(R.layout.frag_map)
public class MapFragment extends Fragment {

    /** The my location overlay. */
    private LocationOverlay locationOverlay;

    @App
    ItineRennesApplication application;

    @SystemService
    LocationManager locationManager;

    @ViewById(R.id.frag_map_mapview)
    ItinerennesMapView map;

    @AfterViews
    void initializeLocationOverlay() {
        this.locationOverlay = map.getMyLocationOverlay();
        map.setMultiTouchControls(true);
    }

    @Override
    public void onResume() {

        super.onResume();

        final SharedPreferences sharedPreferences = application
                .getITRPreferences();
        final int zoomToRestore = sharedPreferences.getInt(
                ITRPrefs.MAP_ZOOM_LEVEL, Conf.MAP_DEFAULT_ZOOM);
        map.getController().setZoom(zoomToRestore);
        final int latToRestore = sharedPreferences.getInt(
                ITRPrefs.MAP_CENTER_LAT, Conf.MAP_RENNES_LAT);
        final int lonToRestore = sharedPreferences.getInt(
                ITRPrefs.MAP_CENTER_LON, Conf.MAP_RENNES_LON);
        map.getController().setCenter(new GeoPoint(latToRestore, lonToRestore));

        final SharedPreferences defaultSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(application);
        final String tileSource = defaultSharedPrefs.getString(
                ITRPrefs.MAP_TILE_PROVIDER, "Itinerennes");
        map.getController().setTileSource(tileSource);

        // if a location provider is enabled and follow location is activated in
        // preferences, the
        // follow location feature is enabled
        if (sharedPreferences.getBoolean(ITRPrefs.MAP_SHOW_LOCATION, true)
                && (locationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
            locationOverlay.enableFollowLocation();
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
    }

    public void onPause() {

        super.onPause();

        final SharedPreferences.Editor edit = application.getITRPreferences()
                .edit();

        // saving in preferences the state of the map (center, follow location
        // and zoom)
        saveMapCenterInPreferences(edit, map.getMapCenter().getLatitudeE6(),
                map.getMapCenter().getLongitudeE6(), map.getZoomLevel());

        // save current displayed overlays
        saveVisibleOverlaysInPreferences(edit);

        edit.commit();

        locationOverlay.disableMyLocation();
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
                locationOverlay.isMyLocationEnabled());
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

    public boolean isVisible(final String type) {
        return map.getStopOverlay().isVisible(type)
                || TypeConstants.TYPE_CAR_PARK.equals(type)
                && map.getParkOverlay().isVisible(type);
    }

    public void setVisibility(final String type, final boolean visible) {
        final List<ILayerSelector> layers = new ArrayList<ILayerSelector>();
        layers.add(map.getParkOverlay());
        layers.add(map.getStopOverlay());
        for (final ILayerSelector layer : layers) {
            for (final LayerDescriptor descriptor : layer
                    .getLayersDescriptors()) {
                if (descriptor.getType().equals(type)) {
                    if (visible) {
                        layer.show(type);
                    } else {
                        layer.hide(type);
                    }
                }
            }
        }
    }

    public void toggleVisibility(final String type) {
        setVisibility(type, !isVisible(type));
    }
}
