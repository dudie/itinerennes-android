package fr.itinerennes.ui.adapter;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.business.service.BusDepartureService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusDeparture;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.ui.activity.BusStationActivity;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.tasks.SafeAsyncTask;

/**
 * @author Jérémie Huchet
 */
public class BusTimeAdapter extends BaseAdapter {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BusTimeAdapter.class);

    /** The android context. */
    private final ITRContext context;

    /** Bus time data. */
    private final List<BusDeparture> data;

    /** Map containing icons for routes. */
    private final HashMap<String, Drawable> routesIcons;

    /** View to add to the list when data is loading. */
    private View pendingView = null;

    /**
     * Flag to indicate if the list should be extended again. Needed when the departures service
     * returns no more results, for example when requesting departures for the next day. TOBO to
     * delete when the issue ITR-19 will be fixed.
     */
    private boolean continueAppending = true;

    /** Global instance of Layout inflater. */
    private LayoutInflater inflater = null;

    /** Bus station displayed in the activity. */
    private final BusStation station;

    /**
     * Constructor.
     * 
     * @param c
     *            The android context
     * @param busStation
     *            Station to display
     * @param departures
     *            departures to display in the list
     * @param routesIcons
     *            list of routes icons
     */
    public BusTimeAdapter(final ITRContext c, final BusStation busStation,
            final List<BusDeparture> departures, final HashMap<String, Drawable> routesIcons) {

        this.data = departures;
        this.context = c;
        this.station = busStation;
        this.routesIcons = routesIcons;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getCount.start");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getCount.end");
        }
        if (!continueAppending) {
            // return the real size of data list
            return data.size();
        }
        // return one more line to show the pending view
        return data.size() + 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final BusDeparture getItem(final int position) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getItem.start");
        }
        return data.get(position);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public final long getItemId(final int position) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getItemId.start");
        }
        return position;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getView.start");
        }

        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }

        View view = null;

        if (position == data.size()) {
            if (pendingView == null) {
                pendingView = inflater.inflate(R.layout.bus_time_pending, null);
            }

            new AppendDeparturesTask(context).execute();
            view = pendingView;

        } else {

            final View busTimeView = inflater.inflate(R.layout.bus_time, null);

            final ImageView departureLineIconeView = (ImageView) busTimeView
                    .findViewById(R.station.bus_icon_line_departure);
            departureLineIconeView.setImageDrawable(routesIcons.get(data.get(position)
                    .getRouteShortName()));

            final TextView departureHeadsignView = (TextView) busTimeView
                    .findViewById(R.station.bus_headsign_departure);
            departureHeadsignView.setText(data.get(position).getHeadsign());

            final TextView departureDateView = (TextView) busTimeView
                    .findViewById(R.station.bus_date_departure);
            departureDateView.setText(DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
                    DateFormat.SHORT).format(data.get(position).getDepartureDate()));

            final TextView timeBeforeDepartureView = (TextView) busTimeView
                    .findViewById(R.station.bus_time_before_departure);

            timeBeforeDepartureView.setText(DateUtils.getRelativeTimeSpanString(data.get(position)
                    .getDepartureDate().getTime(), System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE));

            view = busTimeView;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getView.end");
        }
        return view;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getViewTypeCount()
     */
    @Override
    public final int getViewTypeCount() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getViewTypeCount.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getViewTypeCount.end");
        }

        return (super.getViewTypeCount() + 1);
    }

    /**
     * Takes a list of @link {@link BusDeparture} and add them to the ListView.
     * 
     * @param departures
     *            departures to append to the ListView
     */
    public final void addDepartures(final List<BusDeparture> departures) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addDepartures.start");
        }
        if (departures != null) {

            data.addAll(departures);
        } else {
            continueAppending = false;
        }
        notifyDataSetChanged();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("addDepartures.end");
        }
    }

    /**
     * Class used to fetch departures in background and append them to the ListView on the @link
     * {@link BusStationActivity}.
     * 
     * @author Olivier Boudet
     */
    class AppendDeparturesTask extends SafeAsyncTask<Void, Void, List<BusDeparture>> {

        /**
         * Creates the task which will display departures information.
         * 
         * @param context
         *            the itinerennes application context
         */
        public AppendDeparturesTask(final ITRContext context) {

            super(context);
        }

        /**
         * Load next departures informations in background.
         * <p>
         * {@inheritDoc}
         * </p>
         * 
         * @throws GenericException
         * @see fr.itinerennes.ui.tasks.SafeAsyncTask#doInBackgroundSafely(Params[])
         */
        @Override
        protected List<BusDeparture> doInBackgroundSafely(final Void... params)
                throws GenericException {

            final DatabaseHelper dbHelper = context.getDatabaseHelper();
            final BusDepartureService busDepartureService = new BusDepartureService(dbHelper);
            List<BusDeparture> departures = null;

            departures = busDepartureService.getStationDepartures(station.getId(),
                    data.get(data.size() - 1).getDepartureDate());

            return departures;
        }

        /**
         * Appends fetched departures to the list and notify the adapter that data changed.
         * <p>
         * {@inheritDoc}
         * </p>
         * 
         * @see fr.itinerennes.ui.tasks.SafeAsyncTask#onCustomPostExecute(java.lang.Object)
         */
        @Override
        protected void onCustomPostExecute(final List<BusDeparture> departures) {

            addDepartures(departures);

        }
    }

}
