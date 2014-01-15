package fr.itinerennes.ui.activity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.DrawableRes;
import org.androidannotations.annotations.res.StringArrayRes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.ui.fragment.MapFragment;
import fr.itinerennes.ui.preferences.MainPreferenceActivity_;
import fr.itinerennes.ui.views.overlays.StopOverlayItem;

@EActivity(R.layout.act_home)
@OptionsMenu(R.menu.map_menu)
class HomeActivity extends ItineRennesActivity {

    @StringArrayRes(R.array.menu_main)
    String[] menuItems;

    @ViewById(R.id.act_home_drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.act_home_left_drawer)
    ListView drawerList;

    @FragmentById(R.id.act_home_frag_map)
    MapFragment mapFragment;

    @DrawableRes(R.drawable.ic_marker_bus)
    Drawable iconBus;

    @DrawableRes(R.drawable.ic_marker_bike)
    Drawable iconBike;

    @DrawableRes(R.drawable.ic_marker_subway)
    Drawable iconSubway;

    @DrawableRes(R.drawable.ic_marker_park)
    Drawable iconPark;

    @DrawableRes(R.drawable.btn_star_on_normal_holo_dark)
    Drawable iconBookmark;

    @DrawableRes(R.drawable.ic_action_warning)
    Drawable iconWarning;

    @DrawableRes(R.drawable.ic_action_settings)
    Drawable iconSettings;

    private ActionBarDrawerToggle drawerToggle;

    @AfterViews
    void setupView() {

        drawerList.setAdapter(new MenuAdapter(this, R.layout.act_home_li_menu,
                menuItems));

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, R.string.about, R.string.about_keolis);

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @OptionsItem
    void homeSelected() {
        if (drawerLayout.isDrawerOpen(drawerList)) {
            drawerLayout.closeDrawer(drawerList);
        } else {
            drawerLayout.openDrawer(drawerList);
        }
    }

    @OptionsItem(R.id.menu_my_location)
    void myLocation() {
        mapFragment.toggleLocationOverlay();
    }

    @ItemClick(R.id.act_home_left_drawer)
    void onMainMenuItemClick(final int itemId) {
        switch (itemId) {
        case MenuAdapter.MENU_ITEM_BUS_OVERLAY:
            mapFragment.toggleVisibility(TypeConstants.TYPE_BUS);
            break;
        case MenuAdapter.MENU_ITEM_BIKE_OVERLAY:
            mapFragment.toggleVisibility(TypeConstants.TYPE_BIKE);
            break;
        case MenuAdapter.MENU_ITEM_SUBWAY_OVERLAY:
            mapFragment.toggleVisibility(TypeConstants.TYPE_SUBWAY);
            break;
        case MenuAdapter.MENU_ITEM_PARK_OVERLAY:
            mapFragment.toggleVisibility(TypeConstants.TYPE_CAR_PARK);
            break;
        case MenuAdapter.MENU_ITEM_BOOKMARKS:
            startActivity(new Intent(this, BookmarksActivity.class));
            break;
        case MenuAdapter.MENU_ITEM_ALERTS:
            startActivity(new Intent(this, NetworkAlertsActivity_.class));
            break;
        case MenuAdapter.MENU_ITEM_PREFERENCES:
            startActivity(new Intent(this, MainPreferenceActivity_.class));
            break;
        default:
            break;
        }
        ((MenuAdapter) drawerList.getAdapter()).notifyDataSetChanged();
        drawerLayout.closeDrawer(drawerList);
    }

    private class MenuAdapter extends ArrayAdapter<String> {

        private static final int MENU_ITEM_BUS_OVERLAY = 0;
        private static final int MENU_ITEM_BIKE_OVERLAY = 1;
        private static final int MENU_ITEM_SUBWAY_OVERLAY = 2;
        private static final int MENU_ITEM_PARK_OVERLAY = 3;
        private static final int MENU_ITEM_BOOKMARKS = 4;
        private static final int MENU_ITEM_ALERTS = 5;
        private static final int MENU_ITEM_PREFERENCES = 6;

        public MenuAdapter(final Context context, final int resource,
                final String[] itemLabels) {
            super(context, resource, itemLabels);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO move this logic to XML drawables
            final TextView menuItem = (TextView) super.getView(position,
                    convertView, parent);
            final Drawable d;
            switch (position) {
            case MENU_ITEM_BUS_OVERLAY:
                d = toGreyScaleIfInvisible(TypeConstants.TYPE_BUS, iconBus);
                break;
            case MENU_ITEM_BIKE_OVERLAY:
                d = toGreyScaleIfInvisible(TypeConstants.TYPE_BIKE, iconBike);
                break;
            case MENU_ITEM_SUBWAY_OVERLAY:
                d = toGreyScaleIfInvisible(TypeConstants.TYPE_SUBWAY,
                        iconSubway);
                break;
            case MENU_ITEM_PARK_OVERLAY:
                d = toGreyScaleIfInvisible(TypeConstants.TYPE_CAR_PARK,
                        iconPark);
                break;
            case MENU_ITEM_BOOKMARKS:
                d = iconBookmark;
                break;
            case MENU_ITEM_ALERTS:
                d = iconWarning;
                break;
            case MENU_ITEM_PREFERENCES:
                d = iconSettings;
                break;
            default:
                d = null;
                break;
            }
            menuItem.setCompoundDrawablesWithIntrinsicBounds(d, null, null,
                    null);
            return menuItem;
        }

        private Drawable toGreyScaleIfInvisible(final String type,
                final Drawable icon) {
            if (!mapFragment.isVisible(type)) {
                final ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                icon.setColorFilter(new ColorMatrixColorFilter(matrix));
            } else {
                icon.setColorFilter(null);
            }
            return icon;
        }
    }

    /**
     * Simple Factory to get intents to send to {@link MapActivity}.
     * 
     * @author Olivier Boudet
     */
    public static class IntentFactory {

        /** Intent parameter name to pass the map zoom level to set. */
        public static final String INTENT_PARAM_SET_MAP_ZOOM = "mapZoom";

        /** Intent parameter name to pass the map latitude to set. */
        public static final String INTENT_PARAM_SET_MAP_LAT = "mapLatitude";

        /** Intent parameter name to pass the map longitude to set. */
        public static final String INTENT_PARAM_SET_MAP_LON = "mapLongitude";

        /** Intent parameter name to pass a marker type. */
        public static final String INTENT_PARAM_MARKER_TYPE = "markerType";

        /** Intent parameter name to use to pass a marker. */
        public static final String INTENT_PARAM_MARKER = "marker";

        /** Intent name to use to manage search suggestion. */
        public static final String INTENT_SEARCH_SUGGESTION = "fr.itinerennes.intent.SEARCH_SUGGESTION";

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

            final Intent i = new Intent(context, HomeActivity_.class);
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
