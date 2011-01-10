package fr.itinerennes.ui.adapter;

import java.text.DateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.business.facade.BusDepartureService;
import fr.itinerennes.business.facade.LineIconService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusDeparture;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.ui.activity.BusStationActivity;

/**
 * @author Jérémie Huchet
 */
public class BusTimeAdapter extends BaseAdapter {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BusTimeAdapter.class);

    /** The android context. */
    private final Context context;

    /** Bus time data. */
    private final List<BusDeparture> data;

    /** Line Icon Service. */
    private final LineIconService lineIconService;

    /** View to add to the list when data is loading. */
    private View pendingView = null;

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
     * @param iconService
     *            service to use to fetch keolis icons
     */
    public BusTimeAdapter(final Context c, final BusStation busStation,
            final List<BusDeparture> departures, final LineIconService iconService) {

        this.data = departures;
        this.context = c;
        this.station = busStation;
        this.lineIconService = iconService;
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

        if (position == data.size()) {
            if (pendingView == null) {
                pendingView = inflater.inflate(R.layout.bus_time_pending, null);
            }
            new AppendTask().execute();
            return (pendingView);
        }

        final View busTimeView = inflater.inflate(R.layout.bus_time, null);

        final ImageView departureLineIconeView = (ImageView) busTimeView
                .findViewById(R.station.bus_icon_line_departure);
        try {
            departureLineIconeView.setImageDrawable(lineIconService.getIcon(data.get(position)
                    .getRouteShortName()));
        } catch (final GenericException e) {
            LOGGER.error("Line icon for the route {} can not be fetched.", data.get(position)
                    .getRouteShortName());
        }

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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getView.end");
        }
        return busTimeView;
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

        data.addAll(departures);
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
    class AppendTask extends AsyncTask<Void, Void, List<BusDeparture>> {

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected List<BusDeparture> doInBackground(final Void... params) {

            final DatabaseHelper dbHelper = new DatabaseHelper(context);
            final BusDepartureService busDepartureService = new BusDepartureService(
                    dbHelper.getReadableDatabase());
            List<BusDeparture> departures = null;

            try {
                departures = busDepartureService.getStationDepartures(station.getId(),
                        data.get(data.size() - 1).getDepartureDate());
            } catch (final GenericException e) {
                LOGGER.debug(String.format(
                        "Can't fetch departures informations for station {} and date {} ",
                        station.getId(), data.get(data.size() - 1).getDepartureDate()), e);
            }

            return departures;
        }

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final List<BusDeparture> departures) {

            addDepartures(departures);
        }
    }

}
