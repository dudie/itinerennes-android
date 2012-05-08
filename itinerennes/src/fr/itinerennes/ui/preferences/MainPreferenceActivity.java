package fr.itinerennes.ui.preferences;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import fr.itinerennes.R;

/**
 * Main preference activity.
 * 
 * @author Jérémie Huchet
 */
public final class MainPreferenceActivity extends PreferenceActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MainPreferenceActivity.class);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_main);
    }
}
