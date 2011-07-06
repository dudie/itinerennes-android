package fr.itinerennes.ui.activity;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.database.Columns;
import fr.itinerennes.onebusaway.client.IOneBusAwayClient;
import fr.itinerennes.onebusaway.model.Route;
import fr.itinerennes.onebusaway.model.ScheduleStopTime;
import fr.itinerennes.onebusaway.model.StopSchedule;
import fr.itinerennes.ui.adapter.BusStopTimeAdapter;
import fr.itinerennes.ui.views.event.ToggleStarListener;

/**
 * This activity uses the <code>bus_station.xml</code> layout and displays a window with
 * informations about a bus station.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public final class BusStopActivity extends ItineRennesActivity implements Runnable {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(BusStopActivity.class);

    /** Intent parameter name for the station identifier. */
    public static final String INTENT_STOP_ID = String.format("%s.stopId",
            BusStopActivity.class.getName());

    /** Intent parameter name for the trip identifier where to scroll in the schedule list. */
    public static final String INTENT_FROM_TRIP_ID = String.format("%s.fromTripId",
            BusTripActivity.class.getName());

    /** Intent parameter name for the station name. */
    public static final String INTENT_STOP_NAME = String.format("%s.stopName",
            BusStopActivity.class.getName());

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

    /** Dialog displayed when bus station is loading from bus service. */
    private static final int DIALOG_LOAD_STATION = 2;

    /** The intial size of the progress bar. */
    private static final int INITIAL_PROGRESS_MAX = 30;

    /** Size of the gap between the top and the list and the selection. */
    private static final int SELECTION_FROM_TOP = 50;

    /** Duration of toast messages. */
    private static final int TOAST_DURATION = 5000;

    /** The identifier of the displayed station. */
    private String stopId;

    /** The name of the displayed station. */
    private String stopName;

    /** The progress dialog while the activity is loading. */
    private AlertDialog progressDialog;

    /** The progress bar displayed in the progress dialog. */
    private ProgressBar progressBar;

    /** The complete schedule for this station. */
    private StopSchedule schedule;

    /** The list of route icon. */
    private final HashMap<String, Drawable> routesIcon = new HashMap<String, Drawable>();

    /** Handler for messages from thread which fetch information from the cache or the network. */
    private Handler handler;

    /** flag indicating if this stop is accessible or not. */
    private boolean isAccessible = false;

    /** The OneBusAway client. */
    private IOneBusAwayClient obaClient;

    /**
     * Creates the activity.
     * <ul>
     * <li>Loads the main layout</li>
     * <li>Sets the station name</li>
     * <li>Set up a message handler to trigger actions when loading</li>
     * <li></li>
     * </ul>
     * <p>
     * {@inheritDoc}
     * </p>
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }

        super.onCreate(savedInstanceState);

        obaClient = getApplicationContext().getOneBusAwayClient();

        // retrieve intent parameters
        stopId = getIntent().getStringExtra(INTENT_STOP_ID);
        stopName = getIntent().getStringExtra(INTENT_STOP_NAME);

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
                    break;
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
    protected void onResume() {

        super.onResume();

        setContentView(R.layout.act_bus_stop);

        /* Sets the station name. */
        final TextView name = (TextView) findViewById(R.station.name);
        name.setText(stopName);

        /* Display handistar icon if necessary. */
        final ImageView handistar = (ImageView) findViewById(R.station.bus_activity_wheelchair_icon);
        isAccessible = getApplicationContext().getAccessibilityService().isAccessible(stopId,
                TypeConstants.TYPE_BUS);
        if (isAccessible) {
            handistar.setVisibility(View.VISIBLE);
        }

        /* Sets bookmarked icon. */
        final ToggleButton star = (ToggleButton) findViewById(R.station.toggle_bookmark);
        star.setChecked(getApplicationContext().getBookmarksService().isStarred(
                TypeConstants.TYPE_BUS, stopId));
        star.setOnCheckedChangeListener(new ToggleStarListener(this, TypeConstants.TYPE_BUS,
                stopId, stopName));

        showDialog(PROGRESS_DIALOG);

        final Thread thread = new Thread(this);
        thread.start();

    }

    /**
     * Updates the content of the screen.
     */
    private void updateUI() {

        if (schedule != null) {

            /* Displaying routes icons. */

            final ViewGroup lineList = (ViewGroup) findViewById(R.id.line_icon_container);
            lineList.removeAllViews();
            for (final Route busRoute : schedule.getStop().getRoutes()) {
                final View imageContainer = getLayoutInflater()
                        .inflate(R.layout.li_line_icon, null);
                final ImageView lineIcon = (ImageView) imageContainer
                        .findViewById(R.station.bus_line_icon);
                lineIcon.setImageDrawable(routesIcon.get(busRoute.getShortName()));

                lineList.addView(imageContainer);

                LOGGER.debug("Showing icon for line {}.", busRoute.getShortName());
            }

            /* Displaying departures dates. */
            // get, if available, the tripId of the previous BusTripActivity displayed
            final String tripId = getIntent().getExtras().getString(INTENT_FROM_TRIP_ID);
            final ListView listTimes = (ListView) findViewById(R.station.list_bus);
            listTimes.setEmptyView(findViewById(R.station.empty));
            final BusStopTimeAdapter adapter = new BusStopTimeAdapter(this, schedule, tripId,
                    isAccessible);
            listTimes.setAdapter(adapter);
            listTimes.setSelectionFromTop(adapter.getInitialIndex(), SELECTION_FROM_TOP);
            listTimes.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(final AdapterView<?> parent, final View view,
                        final int position, final long id) {

                    final Intent i = new Intent(getBaseContext(), BusTripActivity.class);
                    final ScheduleStopTime departure = (ScheduleStopTime) parent.getAdapter()
                            .getItem(position);
                    i.putExtra(BusTripActivity.INTENT_FROM_STOP_ID, stopId);
                    i.putExtra(BusTripActivity.INTENT_ROUTE_HEADSIGN, departure.getSimpleHeadsign());
                    i.putExtra(BusTripActivity.INTENT_ROUTE_SHORT_NAME, departure.getRoute()
                            .getShortName());
                    i.putExtra(BusTripActivity.INTENT_TRIP_ID, departure.getTripId());
                    i.putExtra(BusTripActivity.INTENT_ROUTE_ID, departure.getRoute().getId());
                    startActivity(i);
                }
            });
        }

    }

    @Override
    public void run() {

        int returnCode = MESSAGE_SUCCESS;

        try {
            /* Fetching stop informations for this station from the network. */
            schedule = obaClient.getScheduleForStop(stopId, new Date());

            handler.sendEmptyMessage(MESSAGE_INCREMENT_PROGRESS);
        } catch (final IOException e) {
            LOGGER.debug(String.format("Can't load informations for the station %s.", stopId), e);
            returnCode = MESSAGE_FAILURE;
        }

        /* Fetching line icons. */
        if (schedule != null) {
            final int increment = (progressBar.getMax() - progressBar.getProgress())
                    / schedule.getStop().getRoutes().size();
            for (final Route route : schedule.getStop().getRoutes()) {
                routesIcon.put(route.getShortName(), getApplicationContext().getLineIconService()
                        .getIconOrDefault(this, route.getShortName()));
                handler.sendMessage(handler.obtainMessage(MESSAGE_INCREMENT_PROGRESS, increment, 0));
            }
        }

        handler.sendEmptyMessage(returnCode);
    }

    @Override
    protected Dialog onCreateDialog(final int id) {

        Dialog d = null;
        switch (id) {
        case FAILURE_DIALOG:
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_network).setCancelable(true)
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int id) {

                            dialog.dismiss();
                            finish();
                        }
                    });
            d = builder.create();
            break;
        case PROGRESS_DIALOG:
            final AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this);
            progressBuilder.setTitle(R.string.loading);
            final View progressView = getLayoutInflater().inflate(R.layout.dial_progress, null);
            progressBuilder.setView(progressView);
            progressBuilder.setCancelable(true);
            progressBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(final DialogInterface dialog) {

                    finish();
                }
            });
            d = progressDialog = progressBuilder.create();
            break;

        case DIALOG_LOAD_STATION:
            final ProgressDialog loadStation = new ProgressDialog(this);
            loadStation.setTitle(R.string.loading);
            loadStation.setCancelable(false);
            d = loadStation;
        default:
            break;
        }
        return d;
    }

    @Override
    protected void onPrepareDialog(final int id, final Dialog dialog) {

        switch (id) {
        case PROGRESS_DIALOG:
            progressBar = (ProgressBar) progressDialog.findViewById(R.id.progress_bar);
            progressBar.setProgress(0);
            progressBar.setMax(INITIAL_PROGRESS_MAX);
        default:
            break;
        }
    }

    /**
     * Triggered when user clicks on the map button in the toolbar.
     * 
     * @param target
     *            should be an image view.
     */
    public final void onMapButtonClick(final View target) {

        final Cursor c = getApplicationContext().getMarkerDao().getMarker(stopId,
                TypeConstants.TYPE_BUS);
        if (c != null && c.moveToFirst()) {

            final int lat = c.getInt(c.getColumnIndex(Columns.MarkersColumns.LATITUDE));
            final int lon = c.getInt(c.getColumnIndex(Columns.MarkersColumns.LONGITUDE));

            c.close();

            final Intent i = new Intent(getApplicationContext(), MapActivity.class);
            i.setAction(Intent.ACTION_VIEW);
            i.putExtra(MapActivity.INTENT_SET_MAP_ZOOM, 17);
            i.putExtra(MapActivity.INTENT_SET_MAP_LON, lon);
            i.putExtra(MapActivity.INTENT_SET_MAP_LAT, lat);
            startActivity(i);

        } else {
            // TJHU gestion erreur qui ne doit pas arriver dans un cas normal
            Toast.makeText(getApplicationContext(),
                    getString(R.string.error_loading_bus_station_position, stopName),
                    TOAST_DURATION);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected void onNewIntent(final Intent intent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.start");
        }

        setIntent(intent);

        // retrieve intent parameters
        stopId = intent.getStringExtra(INTENT_STOP_ID);
        stopName = intent.getStringExtra(INTENT_STOP_NAME);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.end");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_to_map_menu, menu);

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
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}
