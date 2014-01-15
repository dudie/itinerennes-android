package fr.itinerennes.ui.preferences;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

import fr.itinerennes.R;

/**
 * Main preference activity.
 * 
 * @author Jérémie Huchet
 */
@EActivity
class MainPreferenceActivity extends SherlockPreferenceActivity implements
        OnSharedPreferenceChangeListener {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MainPreferenceActivity.class);

    @AfterInject
    void actionBarDisplayHomeAsUp() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OptionsItem
    void home() {
        finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_main);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(
            final SharedPreferences sharedPreferences, final String key) {

        LOGGER.info("preference {} changed", key);
    }
}
