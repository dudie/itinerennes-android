package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.business.http.oba.OneBusAwayService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.oba.Schedule;
import fr.itinerennes.ui.adapter.BusRouteStopsAdapter;

/**
 * This activity uses the <code>activity/bus_route.xml</code> layout and displays a window with stop
 * times for each stop of the a specific bus route.
 * 
 * @author Jérémie Huchet
 */
public class BusRouteActivity extends ITRContext {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BusRouteActivity.class);

    /**
     * Creates the main screen.
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route);

        final String stopName = getIntent().getExtras().getString("stopName");
        final String routeHeadsign = getIntent().getExtras().getString("routeHeadsign");
        final String routeShortName = getIntent().getExtras().getString("routeShortName");
        final String tripId = getIntent().getExtras().getString("tripId");

        Schedule schedule = null;
        try {
            schedule = new OneBusAwayService().getTripDetails(tripId);
        } catch (final GenericException e) {
            // TJHU Auto-generated catch block
            e.printStackTrace();
        }

        final ImageView routeIcon = (ImageView) findViewById(R.activity_bus_route.route_icon);
        try {
            routeIcon.setImageDrawable(getLineIconService().getIcon(routeShortName));
        } catch (final GenericException e) {
            // TJHU Auto-generated catch block
            e.printStackTrace();
        }

        final TextView routeName = (TextView) findViewById(R.activity_bus_route.route_name);
        routeName.setText(routeHeadsign);

        final ListView listRouteStops = (ListView) findViewById(R.activity_bus_route.list_route_stops);
        final BusRouteStopsAdapter routeStopsAdapter = new BusRouteStopsAdapter(this,
                schedule.getStopTimes());
        listRouteStops.setAdapter(routeStopsAdapter);
        listRouteStops.setSelectionFromTop(routeStopsAdapter.getIndexForStopNamed(stopName), 50);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }
}
