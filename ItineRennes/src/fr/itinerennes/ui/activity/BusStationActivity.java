package fr.itinerennes.ui.activity;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusDeparture;
import fr.itinerennes.model.BusRoute;
import fr.itinerennes.ui.adapter.BusTimeAdapter;

/**
 * This activity uses the <code>bus_station.xml</code> layout and displays a window with
 * informations about a bus station.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class BusStationActivity extends ITRContext implements Runnable {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusStationActivity.class);

    /** Intent parameter name for the station identifier. */
    public static final String INTENT_STOP_ID = String.format("%s.stopId",
            BusStationActivity.class.getName());

    /** Intent parameter name for the station name. */
    public static final String INTENT_STOP_NAME = String.format("%s.stopName",
            BusStationActivity.class.getName());

    /** Message to send to handler in case of a successful download of informations. */
    private static final int MESSAGE_SUCCESS = 0;

    /** Message to send to handler in case of a failed download of informations. */
    private static final int MESSAGE_FAILURE = 1;

    /** Message to send to handler to increment the progress dialog. */
    private static final int MESSAGE_INCREMENT_PROGRESS = 3;

    /** Id for the progress dialog. */
    private static final int PROGRESS_DIALOG = 0;

    /** Id for the failure dialog. */
    private static final int FAILURE_DIALOG = 1;

    /** The intial size of the progress bar. */
    private static final int INITIAL_PROGRESS_MAX = 30;

    /** The identifier of the displayed station. */
    private String stopId;

    /** The name of the displayed station. */
    private String stopName;

    /** The progress dialog while the activity is loading. */
    private AlertDialog progressDialog;

    /** The progress bar displayed in the progress dialog. */
    private ProgressBar progressBar;

    /** The list of routes for this station. */
    private List<BusRoute> busRoutes;

    /** The list of route icon. */
    private final HashMap<String, Drawable> routesIcon = new HashMap<String, Drawable>();

    /** The list of departures for those routes. */
    private List<BusDeparture> departures;

    /** Handler for messages from thread which fetch information from the cache or the network. */
    private Handler handler;

    /**
     * Creates the activity.
     * <ul>
     * <li>Loads the main layout</li>
     * <li>Sets the station name</li>
     * <li>Set up a message handler to trigger actions when loading</li>
     * <li></li>
     * </ul>
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.bus_station);

        // retrieve intent parameters
        stopId = getIntent().getStringExtra(INTENT_STOP_ID);
        stopName = getIntent().getStringExtra(INTENT_STOP_NAME);

        /* Sets the station name. */
        final TextView name = (TextView) findViewById(R.station.name);
        name.setText(stopName);

        handler = new Handler() {

            @Override
            public void handleMessage(final Message msg) {

                switch (msg.what) {
                case MESSAGE_SUCCESS:
                    updateUI();
                    removeDialog(PROGRESS_DIALOG);
                    break;
                case MESSAGE_FAILURE:
                    updateUI();
                    removeDialog(PROGRESS_DIALOG);
                    showDialog(FAILURE_DIALOG);
                    break;
                case MESSAGE_INCREMENT_PROGRESS:
                    progressBar.incrementProgressBy(msg.arg1 > 0 ? msg.arg1 : 1);

                default:
                    break;
                }

            }
        };

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * When the activity is resumed, show a loading dialog box and start a thread updating the list
     * view's content.
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected final void onResume() {

        super.onResume();

        showDialog(PROGRESS_DIALOG);

        final Thread thread = new Thread(this);
        thread.start();

    }

    /**
     * Updates the content of the screen.
     */
    private void updateUI() {

        /* Displaying routes icons. */
        if (busRoutes != null) {
            final ViewGroup lineList = (ViewGroup) findViewById(R.id.line_icon_container);
            lineList.removeAllViews();
            for (final BusRoute busRoute : busRoutes) {
                final View imageContainer = getLayoutInflater().inflate(R.layout.line_icon, null);
                final ImageView lineIcon = (ImageView) imageContainer
                        .findViewById(R.station.bus_line_icon);
                lineIcon.setImageDrawable(routesIcon.get(busRoute.getShortName()));

                lineList.addView(imageContainer);

                LOGGER.debug("Showing icon for line {}.", busRoute.getShortName());
            }
        }

        /* Displaying departures dates. */
        if (departures != null) {
            final ListView listTimes = (ListView) findViewById(R.station.list_bus);
            listTimes.setAdapter(new BusTimeAdapter(this, stopId, departures, routesIcon));
            listTimes.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                        final int position, final long id) {

                    final Intent i = new Intent(getBaseContext(), BusRouteActivity.class);
                    final BusDeparture departure = (BusDeparture) parent.getAdapter().getItem(
                            position);
                    i.putExtra(BusRouteActivity.INTENT_STOP_ID, stopId);
                    i.putExtra(BusRouteActivity.INTENT_ROUTE_HEADSIGN,
                            departure.getSimpleHeadsign());
                    i.putExtra(BusRouteActivity.INTENT_ROUTE_SHORT_NAME,
                            departure.getRouteShortName());
                    i.putExtra(BusRouteActivity.INTENT_TRIP_ID, departure.getTripId());
                    startActivity(i);
                }
            });
        }
    }

    @Override
    public final void run() {

        int returnCode = MESSAGE_SUCCESS;

        // TJHU substring '1_' temporairement le temps de modifier les appels vers OTP
        final String stationId = getIntent().getExtras().getString(INTENT_STOP_ID)
                .replaceFirst("^1_", "");

        try {
            /* Fetching routes informations for this station from the cache or the network. */
            busRoutes = getBusRouteService().getStationRoutes(stationId);
            handler.sendEmptyMessage(MESSAGE_INCREMENT_PROGRESS);
        } catch (final GenericException e) {
            LOGGER.debug(
                    String.format("Can't load routes informations for the station %s.", stationId),
                    e);
            returnCode = MESSAGE_FAILURE;
        }

        try {
            /* Fetching departures informations from the network. */
            departures = getBusDepartureService().getStationDepartures(stationId);
            handler.sendEmptyMessage(MESSAGE_INCREMENT_PROGRESS);
        } catch (final GenericException e) {
            LOGGER.debug(String.format("Can't load departures informations for the station %s.",
                    stationId), e);
            returnCode = MESSAGE_FAILURE;
        }

        /* Fetching line icons. */
        if (busRoutes != null) {
            final int increment = (progressBar.getMax() - progressBar.getProgress())
                    / busRoutes.size();
            for (final BusRoute busRoute : busRoutes) {
                routesIcon.put(busRoute.getShortName(),
                        getLineIconService().getIconOrDefault(this, busRoute.getShortName()));
                handler.sendMessage(handler.obtainMessage(MESSAGE_INCREMENT_PROGRESS, increment, 0));
            }
        }

        handler.sendEmptyMessage(returnCode);
    }

    @Override
    protected final Dialog onCreateDialog(final int id) {

        switch (id) {
        case FAILURE_DIALOG:
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_network).setCancelable(true)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int id) {

                            dialog.cancel();
                        }
                    });
            return builder.create();
        case PROGRESS_DIALOG:
            final AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this);
            progressBuilder.setTitle(R.string.loading);
            final View progressView = getLayoutInflater().inflate(R.layout.progress_dialog, null);
            progressBuilder.setView(progressView);
            progressBuilder.setCancelable(false);
            progressDialog = progressBuilder.create();

            return progressDialog;
        default:
            return null;
        }
    }

    @Override
    protected final void onPrepareDialog(final int id, final Dialog dialog) {

        switch (id) {
        case PROGRESS_DIALOG:
            progressBar = (ProgressBar) progressDialog.findViewById(R.id.progress_bar);
            progressBar.setProgress(0);
            progressBar.setMax(INITIAL_PROGRESS_MAX);

        default:
            break;
        }
    }
}
