package fr.itinerennes.ui.adapter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.model.oba.StopTime;
import fr.itinerennes.ui.activity.BusStationActivity;

/**
 * @author Jérémie Huchet
 */
public class BusRouteStopsAdapter extends BaseAdapter {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusRouteStopsAdapter.class);

    /** The android context. */
    private final Context context;

    /** A reference to the layout inflater. */
    private final LayoutInflater inflater;

    /** The list of arrival times and departure times of the bus for each stop of the trip. */
    private final List<StopTime> arrivalAndDepartures;

    /**
     * Constructor.
     * 
     * @param context
     *            The android context
     * @param arrivalAndDepartures
     *            the list of arrival times and departure times of the bus for each stop of the trip
     */
    public BusRouteStopsAdapter(final Context context, final List<StopTime> arrivalAndDepartures) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.arrivalAndDepartures = arrivalAndDepartures;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {

        return arrivalAndDepartures.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final StopTime getItem(final int position) {

        return arrivalAndDepartures.get(position);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public final long getItemId(final int position) {

        return position;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {

        final View view = inflater.inflate(R.layout.trip_time, null);

        final StopTime stopTime = arrivalAndDepartures.get(position);

        final TextView stopName = (TextView) view.findViewById(R.trip_time.stop_name);
        stopName.setText(stopTime.getStop().getName());

        final TextView departureTime = (TextView) view.findViewById(R.trip_time.departure_time);
        departureTime.setText(DateUtils.formatDateTime(context, stopTime.getDepartureTime()
                .getTime(), DateUtils.FORMAT_24HOUR | DateUtils.FORMAT_SHOW_TIME));

        final TextView relativeTime = (TextView) view.findViewById(R.trip_time.relative_time);
        relativeTime.setText(DateUtils.getRelativeTimeSpanString(stopTime.getDepartureTime()
                .getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE));

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.trip_time.progress_bar);

        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                final Intent i = new Intent(view.getContext(), BusStationActivity.class);
                i.putExtra(BusStationActivity.INTENT_STOP_ID, stopTime.getStop().getId());
                i.putExtra(BusStationActivity.INTENT_STOP_NAME, stopTime.getStop().getName());
                view.getContext().startActivity(i);
            }
        });

        return view;
    }

    /**
     * Gets the index of the item representing a stop time at the given stop.
     * 
     * @param stopId
     *            the identifier of the stop you want the index
     * @return the position of the requested stop.
     */
    public final int getIndexForStopId(final String stopId) {

        final int length = arrivalAndDepartures.size();
        for (int i = 0; i < length; i++) {
            if (arrivalAndDepartures.get(i).getStop().getId().equalsIgnoreCase(stopId)) {
                return i;
            }
        }
        return 0;
    }
}
