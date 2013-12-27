package fr.itinerennes.ui.activity;

import android.content.Context;
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

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.DrawableRes;
import com.googlecode.androidannotations.annotations.res.StringArrayRes;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.ui.fragment.MapFragment;

@EActivity(R.layout.act_home)
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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // favoris
        // alertes
        menu.add("Search")
                .setIcon(com.actionbarsherlock.R.drawable.abs__ic_search)
                .setActionView(R.layout.act_home_collapsible_search)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_ALWAYS
                                | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
        menu.add("preferences").setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            if (drawerLayout.isDrawerOpen(drawerList)) {
                drawerLayout.closeDrawer(drawerList);
            } else {
                drawerLayout.openDrawer(drawerList);
            }
            return true;
        } else {
            return false;
        }
    }

    @ItemClick(R.id.act_home_left_drawer)
    void onMainMenuItemClick(final int itemId) {
        switch (itemId) {
        case 0:
            mapFragment.toggleVisibility(TypeConstants.TYPE_BUS);
            break;
        case 1:
            mapFragment.toggleVisibility(TypeConstants.TYPE_BIKE);
            break;
        case 2:
            mapFragment.toggleVisibility(TypeConstants.TYPE_SUBWAY);
            break;
        case 3:
            mapFragment.toggleVisibility(TypeConstants.TYPE_CAR_PARK);
            break;
        default:
            break;
        }
        ((MenuAdapter) drawerList.getAdapter()).notifyDataSetChanged();
        drawerLayout.closeDrawer(drawerList);
    }

    private class MenuAdapter extends ArrayAdapter<String> {

        public MenuAdapter(final Context context, final int resource,
                final String[] itemLabels) {
            super(context, resource, itemLabels);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final TextView menuItem = (TextView) super.getView(position,
                    convertView, parent);
            final Drawable d;
            switch (position) {
            case 0:
                d = toGreyScaleIfInvisible(TypeConstants.TYPE_BUS, iconBus);
                break;
            case 1:
                d = toGreyScaleIfInvisible(TypeConstants.TYPE_BIKE, iconBike);
                break;
            case 2:
                d = toGreyScaleIfInvisible(TypeConstants.TYPE_SUBWAY,
                        iconSubway);
                break;
            case 3:
                d = toGreyScaleIfInvisible(TypeConstants.TYPE_CAR_PARK,
                        iconPark);
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
}
