package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import fr.itinerennes.R;

/**
 * This activity uses the <code>bike_station.xml</code> layout and displays a window with
 * informations about a bike station.
 * 
 * @author Jérémie Huchet
 */
public class BikeStationActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BikeStationActivity.class);

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bike_station);

        final TextView name = (TextView) findViewById(R.station.name);
        name.setText("Nom de la station de vélo");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }
}
