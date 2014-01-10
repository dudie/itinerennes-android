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
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import fr.itinerennes.Conf;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.api.client.model.Route;
import fr.itinerennes.api.client.model.ScheduleStopTime;
import fr.itinerennes.api.client.model.StopSchedule;
import fr.itinerennes.business.service.ItineRennesApi;
import fr.itinerennes.commons.utils.DateUtils;
import fr.itinerennes.ui.adapter.BusStopTimeAdapter;
import fr.itinerennes.ui.views.LineImageView;
import fr.itinerennes.ui.views.event.ToggleStarListener;
import fr.itinerennes.ui.views.overlays.StopOverlayItem;

/**
 * This activity uses the <code>bus_station.xml</code> layout and displays a
 * window with informations about a bus station.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
@EActivity(R.layout.act_bus_stop)
@OptionsMenu(R.menu.act_stop_menu)
class BusStopActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BusStopActivity.class);

    /** Intent parameter name for the station identifier. */
    public static final String INTENT_STOP_ID = "stopId";

    /**
     * Intent parameter name for the trip identifier where to scroll in the
     * schedule list.
     */
    public static final String INTENT_FROM_TRIP_ID = "fromTripId";

    /** Intent parameter name for the station name. */
    public static final String INTENT_STOP_NAME = "stopName";

    /** Id for the failure dialog. */
    private static final int FAILURE_DIALOG = 1;

    /** Size of the gap between the top and the list and the selection. */
    private static final int SELECTION_FROM_TOP = 50;

    /** Duration of toast messages. */
    private static final int TOAST_DURATION = 5000;

    @Bean
    ItineRennesApi itrApi;

    /** The identifier of the displayed station. */
    @Extra
    String stopId;

    /** The name of the displayed station. */
    @Extra
    String stopName;

    /** flag indicating if this stop is accessible or not. */
    private boolean isAccessible = false;

    /** Adapter for the departures list view. */
    private BusStopTimeAdapter adapter;

    /** Date used to fetch schedule. */
    private Date scheduleDate;

    @ViewById(R.id.act_bus_stop_name)
    TextView stopNameTextView;

    @ViewById(R.id.act_bus_stop_wheelchair_icon)
    ImageView handistarIcon;

    @ViewById(R.id.act_bus_stop_toggle_bookmark)
    ToggleButton bookmarkButton;

    /** The list view showing departures. */
    @ViewById(R.id.act_bus_stop_list_bus)
    ListView listTimes;

    /** Empty departure view. */
    @ViewById(R.id.act_bus_stop_no_departure)
    TextView noDeparture;

    @AfterViews
    void setupStopName() {
        stopNameTextView.setText(stopName);
    }

    @AfterViews
    void setupToggleBookmarkButton() {
        bookmarkButton.setChecked(getApplicationContext().getBookmarksService()
                .isStarred(TypeConstants.TYPE_BUS, stopId));
        bookmarkButton.setOnCheckedChangeListener(new ToggleStarListener(this,
                TypeConstants.TYPE_BUS, stopId, stopName));
    }

    @AfterViews
    void setupAccessibilityAndScheduleTimes() {
        isAccessible = getApplicationContext().getAccessibilityService()
                .isAccessible(stopId, TypeConstants.TYPE_BUS);
        if (isAccessible) {
            handistarIcon.setVisibility(View.VISIBLE);
        }

        // initialize the adapter
        adapter = new BusStopTimeAdapter(this, isAccessible);
        listTimes.setEmptyView(noDeparture);
        listTimes.setAdapter(adapter);

        prepareThenLoadSchedule(new Date());
    }

    @ItemClick(R.id.act_bus_stop_list_bus)
    void onStopDepartureClick(final ScheduleStopTime departure) {

        final Intent i = new Intent(getBaseContext(), BusTripActivity_.class);
        i.putExtra(BusTripActivity.INTENT_FROM_STOP_ID, stopId);
        i.putExtra(BusTripActivity.INTENT_ROUTE_HEADSIGN,
                departure.getSimpleHeadsign());
        i.putExtra(BusTripActivity.INTENT_ROUTE_SHORT_NAME, departure
                .getRoute().getShortName());
        i.putExtra(BusTripActivity.INTENT_TRIP_ID, departure.getTripId());
        i.putExtra(BusTripActivity.INTENT_ROUTE_ID, departure.getRoute()
                .getId());
        startActivity(i);
    }

    @UiThread
    void prepareThenLoadSchedule(final Date date) {
        /* Hide progress bar and show list view. */
        findViewById(R.id.misc_view_is_loading).setVisibility(View.VISIBLE);
        listTimes.setVisibility(View.GONE);
        loadSchedule(date);
    }

    @Background
    void loadSchedule(final Date date) {
        try {
            /* Fetching stop informations for this station from the network. */
            scheduleDate = date;
            updateScheduleDate(itrApi.getScheduleForStop(stopId, date));

        } catch (final IOException e) {
            LOGGER.debug(String.format(
                    "Can't load informations for the station %s.", stopId), e);
            showDialog(FAILURE_DIALOG);
        }
    }

    @UiThread
    void updateScheduleDate(final StopSchedule schedule) {
        boolean expectedDay = scheduleDate.equals(schedule.getDate());

        if (expectedDay) {
            LOGGER.debug("received schedule for {} and refreshing UI with it",
                    schedule.getDate());

            /* Hide progress bar and show list view. */
            findViewById(R.id.misc_view_is_loading).setVisibility(View.GONE);
            listTimes.setVisibility(View.VISIBLE);

            /* Displaying routes icons. */

            final ViewGroup lineList = (ViewGroup) findViewById(R.id.line_icon_container);
            lineList.removeAllViews();
            for (final Route busRoute : schedule.getRoutes()) {

                final LineImageView lineIcon = new LineImageView(
                        BusStopActivity.this);
                lineIcon.setLine(busRoute.getShortName());
                lineIcon.fitToHeight(24);
                lineIcon.setPadding(2, 0, 2, 0);
                lineList.addView(lineIcon);
            }

            /* Displaying departures dates. */
            // get, if available, the tripId of the previous BusTripActivity
            // displayed
            final String tripId = getIntent().getExtras().getString(
                    INTENT_FROM_TRIP_ID);

            adapter.setTripIdToHighlight(tripId);
            adapter.setStopSchedule(schedule);

            listTimes.setSelectionFromTop(adapter.getInitialIndex(),
                    SELECTION_FROM_TOP);
        } else {
            LOGGER.debug("expecting a schedule for {} but got {}", expectedDay, schedule.getDate());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.activity.ItineRennesActivity#onResume()
     */
    @Override
    protected void onResume() {

        super.onResume();

        /*
         * When the user gets back to the activity, the time may have changed
         * since the last time he used it. So we need to update the UI to gray
         * deprecated bus times.
         */
        if (adapter != null) {
            adapter.notifyDataSetInvalidated();
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
        case FAILURE_DIALOG:
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_network)
                    .setCancelable(true)
                    .setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(
                                        final DialogInterface dialog,
                                        final int id) {

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
     * Triggered when user clicks on the map button in the toolbar.
     * 
     * @param target
     *            should be an image view.
     */
    @Click(R.id.act_bus_stop_map_button)
    public void onMapButtonClick(final View target) {

        final Cursor c = getApplicationContext().getMarkerDao().getMarker(
                stopId, TypeConstants.TYPE_BUS);
        if (c != null && c.moveToFirst()) {

            final StopOverlayItem marker = getApplicationContext()
                    .getMarkerDao().getMarkerOverlayItem(c);

            c.close();

            startActivity(MapActivity.IntentFactory.getOpenMapBoxIntent(
                    getApplicationContext(), marker, Conf.MAP_ZOOM_ON_LOCATION));

        } else {
            // TJHU gestion erreur qui ne doit pas arriver dans un cas normal
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.error_loading_bus_station_position,
                            stopName), TOAST_DURATION).show();;
        }
    }

    @Click(R.id.menu_back_to_map)
    void onClickMapButton(final View button) {

        final Intent i = new Intent(getApplicationContext(), MapActivity.class);
        i.setAction(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @Click(R.id.menu_previous_day)
    void onClickPreviousDay(final View button) {
        final Calendar c = Calendar.getInstance();
        c.setTime(scheduleDate);
        prepareThenLoadSchedule(DateUtils.addDays(c.getTime(), -1));
    }

    @Click(R.id.menu_next_day)
    void onClickNextDay(final View button) {
        final Calendar c = Calendar.getInstance();
        c.setTime(scheduleDate);
        prepareThenLoadSchedule(DateUtils.addDays(c.getTime(), 1));
    }
}
