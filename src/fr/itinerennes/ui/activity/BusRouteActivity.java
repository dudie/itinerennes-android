package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.oba.TripSchedule;
import fr.itinerennes.model.oba.TripStopTime;
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
     * Intent parameter name for the stop identifier where to scroll in the list of displayed trip
     * departures.
     */
    public static final String INTENT_STOP_ID = String.format("%s.stopId",
            BusRouteActivity.class.getName());

    /** Intent parameter name for the route headsign. */
    public static final String INTENT_ROUTE_HEADSIGN = String.format("%s.routeHeadsign",
            BusRouteActivity.class.getName());

    /** Intent parameter name for the route short name. */
    public static final String INTENT_ROUTE_SHORT_NAME = String.format("%s.routeShortName",
            BusRouteActivity.class.getName());

    /** Intent parameter name for the trip identifier. */
    public static final String INTENT_TRIP_ID = String.format("%s.tripId",
            BusRouteActivity.class.getName());

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

        final String stopId = getIntent().getExtras().getString(INTENT_STOP_ID);
        final String routeHeadsign = getIntent().getExtras().getString(INTENT_ROUTE_HEADSIGN);
        final String routeShortName = getIntent().getExtras().getString(INTENT_ROUTE_SHORT_NAME);
        final String tripId = getIntent().getExtras().getString(INTENT_TRIP_ID);

        TripSchedule schedule = null;
        try {
            schedule = getOneBusAwayService().getTripDetails(tripId);
        } catch (final GenericException e) {
            // TJHU Auto-generated catch block
            e.printStackTrace();
        }

        final ImageView routeIcon = (ImageView) findViewById(R.activity_bus_route.route_icon);
        routeIcon.setImageDrawable(getLineIconService().getIconOrDefault(this, routeShortName));

        final TextView routeName = (TextView) findViewById(R.activity_bus_route.route_name);
        routeName.setText(routeHeadsign);

        final ListView listRouteStops = (ListView) findViewById(R.activity_bus_route.list_route_stops);
        final BusRouteStopsAdapter routeStopsAdapter = new BusRouteStopsAdapter(this,
                schedule.getStopTimes());
        listRouteStops.setAdapter(routeStopsAdapter);
        listRouteStops.setSelectionFromTop(routeStopsAdapter.getIndexForStopId(stopId), 50);
        listRouteStops.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {

                final TripStopTime stopTime = routeStopsAdapter.getItem(position);
                final Intent i = new Intent(view.getContext(), BusStationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(BusStationActivity.INTENT_STOP_ID, stopTime.getStop().getId());
                i.putExtra(BusStationActivity.INTENT_STOP_NAME, stopTime.getStop().getName());
                view.getContext().startActivity(i);
            }
        });

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }
}
