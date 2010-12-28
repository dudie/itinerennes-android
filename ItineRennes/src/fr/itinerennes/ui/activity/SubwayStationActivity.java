package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.business.facade.SubwayService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.SubwayStation;

/**
 * This activity uses the <code>subway_station.xml</code> layout and displays a window with
 * informations about a subway station.
 * 
 * @author Jérémie Huchet
 */
public class SubwayStationActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(SubwayStationActivity.class);

    /** The Subway Service. */
    private SubwayService subwayService;

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
        setContentView(R.layout.subway_station);

        final DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
        subwayService = new SubwayService(dbHelper.getReadableDatabase());

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
    protected void onResume() {

        super.onResume();

        SubwayStation station;
        try {
            station = subwayService.getStation(getIntent().getExtras().getString("item"));

            final TextView name = (TextView) findViewById(R.station.name);
            name.setText(station.getName());
        } catch (final GenericException e) {
            LOGGER.debug("Can't load subway station information.", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {

        subwayService.release();
        super.onDestroy();
    }
}
