package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.business.facade.BikeService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BikeStation;

/**
 * This activity uses the <code>bike_station.xml</code> layout and displays a window with
 * informations about a bike station.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class BikeStationActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BikeStationActivity.class);

    /** The Bike Service. */
    private BikeService bikeService;

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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        final DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        bikeService = new BikeService(dbHelper.getWritableDatabase());

        BikeStation station;
        try {
            station = bikeService.getStation(getIntent().getExtras().getString("item"));
            final TextView name = (TextView) findViewById(R.station.name);
            name.setText(station.getName());
            LOGGER.debug("Bike stop title height = {}", name.getMeasuredHeight());

            // ITR-21 plural forms does not accept zero value.

            final TextView availBikesView = (TextView) findViewById(R.bike_station.available_bikes);
            final int availBikes = station.getAvailableBikes();
            availBikesView.setText(String.format("%s %s", availBikes, getResources()
                    .getQuantityString(R.plurals.label_bikes, availBikes)));

            final TextView availSlotsView = (TextView) findViewById(R.bike_station.available_slots);
            final int availSlots = station.getAvailableSlots();
            availSlotsView.setText(String.format("%s %s", availSlots, getResources()
                    .getQuantityString(R.plurals.label_slots, availSlots)));

            final ProgressBar status = (ProgressBar) findViewById(R.id.bikes_slots_status);
            status.setMax(availSlots + availBikes);
            status.setProgress(availBikes);
        } catch (final GenericException e) {
            LOGGER.debug("Can't load bike station information.", e);
        }
    }

    @Override
    protected void onPause() {

        bikeService.release();
        super.onPause();
    }
}
