package fr.itinerennes.ui.activity;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.business.facade.BusDepartureService;
import fr.itinerennes.business.facade.BusRouteService;
import fr.itinerennes.business.facade.BusService;
import fr.itinerennes.business.facade.LineIconService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusDeparture;
import fr.itinerennes.model.BusRoute;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.ui.adapter.BusTimeAdapter;

/**
 * This activity uses the <code>bus_station.xml</code> layout and displays a window with
 * informations about a bus station.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class BusStationActivity extends Activity implements Runnable {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusStationActivity.class);

    /** The Bus Service. */
    private BusService busService;

    /** The Bus Route Service. */
    private BusRouteService busRouteService;

    /** The Departure Service. */
    private BusDepartureService busDepartureService;

    /** The LineIcon Service. */
    private LineIconService lineIconService;

    /** The progress dialog while the activity is loading. */
    private ProgressDialog progressDialog;

    /** The diplsayed station. */
    private BusStation station;

    /** The database helper. */
    private DatabaseHelper dbHelper;

    /** The list of routes for this station. */
    private List<BusRoute> busRoutes;

    /** The list of departures for those routes. */
    private List<BusDeparture> departures;

    /** Handler for messages from thread which fetch information from the cache or the network. */
    private Handler handler;

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
    private static final int INITIAL_PROGRESS_MAX = 4;

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
        setContentView(R.layout.bus_station);

        dbHelper = new DatabaseHelper(getBaseContext());
        busService = new BusService(dbHelper);
        busRouteService = new BusRouteService(dbHelper);
        busDepartureService = new BusDepartureService(dbHelper);
        lineIconService = new LineIconService(dbHelper);

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
                    progressDialog.incrementProgressBy(1);

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
     * {@inheritDoc}
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

    private void updateUI() {

        /* Displaying station name. */
        if (station != null) {
            final TextView name = (TextView) findViewById(R.station.name);
            name.setText(station.getName());
        }

        /* Displaying routes icons. */
        if (busRoutes != null) {
            final ViewGroup lineList = (ViewGroup) findViewById(R.station.line_icon_list);
            lineList.removeAllViews();
            if (busRoutes != null) {
                for (final BusRoute busRoute : busRoutes) {
                    final ImageView lineIcon = (ImageView) getLayoutInflater().inflate(
                            R.layout.line_icon, null);
                    try {
                        lineIcon.setImageDrawable(lineIconService.getIcon(busRoute.getShortName()));

                    } catch (final GenericException e) {
                        LOGGER.error(String.format(
                                "Line icon for the route %s can not be fetched.",
                                busRoute.getShortName()), e);
                    }
                    lineList.addView(lineIcon);
                    LOGGER.debug("Showing icon for line {}.", busRoute.getShortName());
                }
            }
        }

        /* Displaying departures dates. */
        if (departures != null) {
            final ListView listTimes = (ListView) findViewById(R.station.list_bus);
            listTimes.setAdapter(new BusTimeAdapter(getBaseContext(), station, departures,
                    lineIconService));
        }
    }

    @Override
    public final void run() {

        int returnCode = MESSAGE_SUCCESS;

        final String stationId = getIntent().getExtras().getString("item");

        try {
            /* Fetching station from the cache or the network. */
            station = busService.getStation(stationId);
        } catch (final GenericException e) {
            LOGGER.debug(
                    String.format("Can't load station informations for the station %s.", stationId),
                    e);
            returnCode = MESSAGE_FAILURE;
        }

        try {
            /* Fetching routes informations for this station from the cache or the network. */
            busRoutes = busRouteService.getStationRoutes(stationId);
            handler.sendEmptyMessage(MESSAGE_INCREMENT_PROGRESS);
        } catch (final GenericException e) {
            LOGGER.debug(
                    String.format("Can't load routes informations for the station %s.", stationId),
                    e);
            returnCode = MESSAGE_FAILURE;
        }

        if (busRoutes != null) {
            progressDialog.setMax(progressDialog.getMax() + busRoutes.size());
            for (final BusRoute busRoute : busRoutes) {
                try {
                    lineIconService.getIcon(busRoute.getShortName());
                    progressDialog.incrementProgressBy(1);
                } catch (final GenericException e) {
                    LOGGER.error(String.format("Line icon for the route %s can not be fetched.",
                            busRoute.getShortName()), e);
                }
            }
        }

        try {
            /* Fetching departures informations from the network. */

            departures = busDepartureService.getStationDepartures(stationId);
            handler.sendEmptyMessage(MESSAGE_INCREMENT_PROGRESS);
        } catch (final GenericException e) {
            LOGGER.debug(String.format("Can't load departures informations for the station %s.",
                    stationId), e);
            returnCode = MESSAGE_FAILURE;
        }

        handler.sendEmptyMessage(returnCode);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected final void onDestroy() {

        dbHelper.close();
        super.onDestroy();
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
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage(getBaseContext().getResources().getString(R.string.loading));
            progressDialog.setCancelable(false);
            return progressDialog;
        default:
            return null;
        }
    }

    @Override
    protected final void onPrepareDialog(int id, Dialog dialog) {

        switch (id) {
        case PROGRESS_DIALOG:
            ((ProgressDialog) dialog).setProgress(0);
            ((ProgressDialog) dialog).setMax(INITIAL_PROGRESS_MAX);

        default:
            break;
        }
    }
}
