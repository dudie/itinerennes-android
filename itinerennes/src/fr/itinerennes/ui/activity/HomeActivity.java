package fr.itinerennes.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringArrayRes;

import fr.itinerennes.R;
import fr.itinerennes.ui.preferences.MapFragment_;

@EActivity(R.layout.act_home)
class HomeActivity extends ItineRennesActivity {

    @StringArrayRes(R.array.menu_main)
    String[] menuItems;

    @ViewById(R.id.act_home_drawer_layout)
    DrawerLayout drawerLayout;

    @ViewById(R.id.act_home_left_drawer)
    ListView drawerList;

    @AfterViews
    void setupView() {

        // Set the adapter for the list view
        drawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.act_home_li_menu, menuItems));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Refresh")
        .setIcon(android.R.drawable.ic_delete)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

    return true;
    }
    
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        
        getSupportActionBar().setTitle("ItinéRennes");
    }

    @ItemClick(R.id.act_home_left_drawer)
    void onMainMenuItemClick(int itemId) {
        Toast.makeText(this, menuItems[itemId], 2000).show();

        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = new MapFragment_();

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.act_home_content_frame, fragment).commit();

        // close the drawer
        drawerLayout.closeDrawer(drawerList);
    }
}
