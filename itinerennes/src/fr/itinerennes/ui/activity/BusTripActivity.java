package fr.itinerennes.ui.activity;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.api.client.ItineRennesApiClient;
import fr.itinerennes.api.client.model.TripSchedule;
import fr.itinerennes.api.client.model.TripStopTime;
import fr.itinerennes.ui.adapter.BusTripTimeAdapter;

/**
 * This activity uses the <code>activity/bus_route.xml</code> layout and displays a window with stop
 * times for each stop of the a specific bus route.
 * 
 * @author Jérémie Huchet
 */
public final class BusTripActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BusTripActivity.class);

    /**
     * Intent parameter name for the stop identifier where to scroll in the list of displayed trip
     * departures.
     */
    public static final String INTENT_FROM_STOP_ID = String.format("%s.fromStopId",
            BusTripActivity.class.getName());

    /** Intent parameter name for the route headsign. */
    public static final String INTENT_ROUTE_HEADSIGN = String.format("%s.routeHeadsign",
            BusTripActivity.class.getName());

    /** Intent parameter name for the route short name. */
    public static final String INTENT_ROUTE_SHORT_NAME = String.format("%s.routeShortName",
            BusTripActivity.class.getName());

    /** Intent parameter name for the trip identifier. */
    public static final String INTENT_TRIP_ID = String.format("%s.tripId",
            BusTripActivity.class.getName());

    /** Intent parameter name for the route identifier. */
    public static final String INTENT_ROUTE_ID = String.format("%s.routeId",
            BusTripActivity.class.getName());

    /** Constant identifying the "failure" dialog. */
    private static final int DIALOG_FAILURE = 0;

    /** The view where to display the route icon. */
    private ImageView routeIcon;

    /** The view where to display the route headsign. */
    private TextView routeName;

    /** The list view where to display the trip schedule. */
    private ListView listRouteStops;

    /** flag indicating if this route is accessible or not. */
    private boolean isAccessible;

    /** Adapter for the schedule list view. */
    private BusTripTimeAdapter adapter;

    /**
     * If an update of the content displayed is requested, the task which result is expected is
     * referenced by this variable. It's typically the last requested started.
     */
    private AsyncTask<String, Void, TripSchedule> scheduleDownloaderTask;

    /**
     * Creates the main screen. {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_bus_trip);

        routeIcon = (ImageView) findViewById(R.activity_bus_route.route_icon);
        routeName = (TextView) findViewById(R.activity_bus_route.route_name);
        listRouteStops = (ListView) findViewById(R.activity_bus_route.list_route_stops);

        adapter = new BusTripTimeAdapter(this, isAccessible);
        listRouteStops.setAdapter(adapter);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (scheduleDownloaderTask != null
                && !scheduleDownloaderTask.getStatus().equals(Status.FINISHED)) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onDestroy - cancelling running refresh task.");
            }

            scheduleDownloaderTask.cancel(true);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onResume.start");
        }

        super.onResume();

        final String routeHeadsign = getIntent().getExtras().getString(INTENT_ROUTE_HEADSIGN);
        final String routeShortName = getIntent().getExtras().getString(INTENT_ROUTE_SHORT_NAME);
        final String tripId = getIntent().getExtras().getString(INTENT_TRIP_ID);
        final String routeId = getIntent().getExtras().getString(INTENT_ROUTE_ID);

        routeIcon.setImageDrawable(getApplicationContext().getLineIconService().getIconOrDefault(
                this, routeShortName));
        routeName.setText(routeHeadsign);

        /* Display handistar icon if necessary. */
        final ImageView handistar = (ImageView) findViewById(R.activity_bus_route.wheelchair_icon);
        isAccessible = getApplicationContext().getAccessibilityService().isAccessible(routeId,
                TypeConstants.TYPE_BUS_ROUTE);
        if (isAccessible) {
            handistar.setVisibility(View.VISIBLE);
        }

        listRouteStops.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {

                final TripStopTime stopTime = (TripStopTime) parent.getItemAtPosition(position);
                final Intent i = new Intent(view.getContext(), BusStopActivity_.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(BusStopActivity.INTENT_STOP_ID, stopTime.getStop().getId());
                i.putExtra(BusStopActivity.INTENT_STOP_NAME, stopTime.getStop().getName());
                i.putExtra(BusStopActivity.INTENT_FROM_TRIP_ID, tripId);
                view.getContext().startActivity(i);
            }
        });

        scheduleDownloaderTask = new ScheduleDownloader();
        scheduleDownloaderTask.execute(tripId);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onResume.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(final int id) {

        Dialog d = null;
        switch (id) {

        // the failure dialog
        case DIALOG_FAILURE:
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_network);
            builder.setCancelable(true);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, final int id) {

                    dialog.dismiss();
                    finish();
                }
            });
            d = builder.create();
            break;
        default:
            break;
        }

        return d;
    }

    /**
     * AsyncTask intended to request a TripSchedule.
     * 
     * @author Olivier Boudet
     */
    private class ScheduleDownloader extends AsyncTask<String, Void, TripSchedule> {

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {

            /* Hide progress bar and show list view. */
            findViewById(R.activity_bus_route.progress_bar).setVisibility(View.VISIBLE);
            findViewById(R.activity_bus_route.list_route_stops).setVisibility(View.GONE);
        };

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected TripSchedule doInBackground(final String... params) {

            try {

                final ItineRennesApiClient obaClient = getApplicationContext().getItineRennesApiClient();

                return obaClient.getTripDetails(params[0]);

            } catch (final IOException e) {
                LOGGER.debug(String.format("Can't load informations for the trip %s.", params[0]),
                        e);
            }
            return null;
        }

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final TripSchedule schedule) {

            if (schedule != null) {

                /* Hide progress bar and show list view. */
                findViewById(R.activity_bus_route.progress_bar).setVisibility(View.GONE);
                findViewById(R.activity_bus_route.list_route_stops).setVisibility(View.VISIBLE);

                final String stopId = getIntent().getExtras().getString(INTENT_FROM_STOP_ID);

                adapter.setInitialStopId(stopId);
                adapter.setArrivalAndDepartures(schedule.getStopTimes());

                listRouteStops.setSelectionFromTop(adapter.getIndexForStopId(stopId), 50);
            } else {
                showDialog(DIALOG_FAILURE);
            }
        };
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.act_trip_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_back_to_map:

            final Intent i = new Intent(getApplicationContext(), MapActivity.class);
            i.setAction(Intent.ACTION_VIEW);
            startActivity(i);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
