package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.oba.Route;
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
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(BusRouteActivity.class);

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

    /** Intent parameter name for the route identifier. */
    public static final String INTENT_ROUTE_ID = String.format("%s.routeId",
            BusRouteActivity.class.getName());

    /** Constant identifying the "loading" dialog. */
    private static final int DIALOG_LOADING = 0;

    /** Constant identifying the "failure" dialog. */
    private static final int DIALOG_FAILURE = 1;

    /** Handler message specifying the trip schedule download has started. */
    private static final int MSG_DOWNLOAD_START = 0;

    /**
     * Handler message specifying the trip schedule is available and the list adapter should be set
     * up.
     */
    private static final int MSG_SET_ADAPTER = 1;

    /** Handler message specifying the trip schedule download finished. */
    private static final int MSG_DOWNLOAD_END = 3;

    /** Handler message specifying the trip schedule download failed. */
    private static final int MSG_DOWNLOAD_ERROR = -1;

    /** The view where to display the route icon. */
    private ImageView routeIcon;

    /** The view where to display the route headsign. */
    private TextView routeName;

    /** The list view where to display the trip schedule. */
    private ListView listRouteStops;

    /** The progress bar of the dialog box. */
    private ProgressBar progressBar;

    /** flag indicating if this route is accessible or not. */
    private boolean isAccessible;

    /** The handler to handle progress messages in the UI thread. */
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {

            switch (msg.what) {
            case MSG_DOWNLOAD_START:
                progressBar.setProgress(0);
                progressBar.setMax(2);
                break;
            case MSG_SET_ADAPTER:
                progressBar.setProgress(1);
                onReceiveTripSchedule((TripSchedule) msg.obj);
                break;
            case MSG_DOWNLOAD_END:
                progressBar.setProgress(2);
                dismissDialogIfDisplayed(DIALOG_LOADING);
                break;
            case MSG_DOWNLOAD_ERROR:
                dismissDialogIfDisplayed(DIALOG_LOADING);
                showDialog(DIALOG_FAILURE);
                break;

            default:
                break;
            }
        }
    };

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected final void onNewIntent(final Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);
    }

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

        routeIcon = (ImageView) findViewById(R.activity_bus_route.route_icon);
        routeName = (TextView) findViewById(R.activity_bus_route.route_name);
        listRouteStops = (ListView) findViewById(R.activity_bus_route.list_route_stops);

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
    protected final void onResume() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onResume.start");
        }

        super.onResume();

        final String routeHeadsign = getIntent().getExtras().getString(INTENT_ROUTE_HEADSIGN);
        final String routeShortName = getIntent().getExtras().getString(INTENT_ROUTE_SHORT_NAME);
        final String tripId = getIntent().getExtras().getString(INTENT_TRIP_ID);
        final String routeId = getIntent().getExtras().getString(INTENT_ROUTE_ID);

        routeIcon.setImageDrawable(getLineIconService().getIconOrDefault(this, routeShortName));
        routeName.setText(routeHeadsign);

        /* Display handistar icon if necessary. */
        final ImageView handistar = (ImageView) findViewById(R.activity_bus_route.wheelchair_icon);
        isAccessible = getAccessibilityService().isAccessible(routeId, Route.class.getName());
        if (isAccessible) {
            handistar.setVisibility(View.VISIBLE);
        }

        listRouteStops.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {

                final TripStopTime stopTime = (TripStopTime) parent.getItemAtPosition(position);
                final Intent i = new Intent(view.getContext(), BusStationActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra(BusStationActivity.INTENT_STOP_ID, stopTime.getStop().getId());
                i.putExtra(BusStationActivity.INTENT_STOP_NAME, stopTime.getStop().getName());
                view.getContext().startActivity(i);
            }
        });

        showDialog(DIALOG_LOADING);
        new ScheduleDownloader(tripId, handler).start();

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
    protected final Dialog onCreateDialog(final int id) {

        Dialog d = null;
        switch (id) {
        // the loading dialog
        case DIALOG_LOADING:
            final AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this);
            progressBuilder.setTitle(R.string.loading);
            final View progressView = getLayoutInflater().inflate(R.layout.progress_dialog, null);
            progressBar = (ProgressBar) progressView.findViewById(R.id.progress_bar);
            progressBuilder.setView(progressView);
            progressBuilder.setCancelable(true);
            progressBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(final DialogInterface dialog) {

                    finish();
                }
            });
            d = progressBuilder.create();
            break;

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
     * Called by the {@link #handler} when the trip schedule has been retrieved.
     * 
     * @param schedule
     *            the retrieved trip schedule.
     */
    private void onReceiveTripSchedule(final TripSchedule schedule) {

        final String stopId = getIntent().getExtras().getString(INTENT_STOP_ID);

        final BusRouteStopsAdapter routeStopsAdapter = new BusRouteStopsAdapter(this, stopId,
                schedule.getStopTimes(), isAccessible);
        listRouteStops.setAdapter(routeStopsAdapter);
        final int idx = routeStopsAdapter.getIndexForStopId(stopId);
        listRouteStops.setSelectionFromTop(idx, 50);
    }

    /**
     * Thread intended to request a TripSchedule.
     * 
     * @author Jérémie Huchet
     */
    private class ScheduleDownloader extends Thread {

        /** The trip id. */
        private final String tripId;

        /** The handler to notify progress. */
        private final Handler handler;

        /**
         * Creates the trip schedule requested.
         * 
         * @param tripId
         *            the trip id the retrieve
         * @param handler
         *            the handler to notify
         */
        public ScheduleDownloader(final String tripId, final Handler handler) {

            this.tripId = tripId;
            this.handler = handler;
        }

        /**
         * Retrieve the trip schedule and notifies the handler.
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {

            handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_START));
            try {
                final TripSchedule schedule = getOneBusAwayService().getTripDetails(tripId);
                handler.sendMessage(handler.obtainMessage(MSG_SET_ADAPTER, schedule));
                handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_END));
            } catch (final GenericException e) {
                handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_ERROR));
            }
        }
    }
}
