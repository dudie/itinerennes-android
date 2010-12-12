package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
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

        final TextView availBikes = (TextView) findViewById(R.bike_station.available_bikes);
        availBikes.setText("2 vélos");

        final TextView availSlots = (TextView) findViewById(R.bike_station.available_slots);
        availSlots.setText("3 places");

        final ProgressBar status = (ProgressBar) findViewById(R.id.bikes_slots_status);
        status.setMax(5);
        status.setProgress(2);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }
}
