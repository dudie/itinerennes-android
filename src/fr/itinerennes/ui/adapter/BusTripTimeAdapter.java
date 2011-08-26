package fr.itinerennes.ui.adapter;

import java.util.Date;
import java.util.List;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.dudie.onebusaway.model.TripStopTime;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.ui.activity.ItineRennesActivity;

/**
 * @author Jérémie Huchet
 */
public class BusTripTimeAdapter extends BaseAdapter {

    /** The itinérennes context. */
    private final ItineRennesActivity context;

    /** A reference to the layout inflater. */
    private final LayoutInflater inflater;

    /** The initial stop identifier on which set a marker. */
    private String initialStopId;

    /** The list of arrival times and departure times of the bus for each stop of the trip. */
    private List<TripStopTime> arrivalAndDepartures;

    /**
     * Is the route accessible ? Determines if the handistar icon should be displayed for accessible
     * stops in the list.
     */
    private final boolean routeIsAccessible;

    /**
     * Constructor.
     * 
     * @param context
     *            The android context
     * @param routeIsAccessible
     *            accessibility flag for the route
     */
    public BusTripTimeAdapter(final ItineRennesActivity context, final boolean routeIsAccessible) {

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.routeIsAccessible = routeIsAccessible;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {

        if (arrivalAndDepartures != null) {
            return arrivalAndDepartures.size();
        } else {
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final TripStopTime getItem(final int position) {

        if (arrivalAndDepartures != null) {
            return arrivalAndDepartures.get(position);
        } else {
            return null;
        }
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

        final View view = inflater.inflate(R.layout.li_trip_time_bus, null);

        final TripStopTime stopTime = arrivalAndDepartures.get(position);

        final TextView stopName = (TextView) view.findViewById(R.trip_time.stop_name);
        stopName.setText(stopTime.getStop().getName());

        final TextView departureTime = (TextView) view.findViewById(R.trip_time.departure_time);
        departureTime.setText(DateUtils.formatDateTime(context, stopTime.getDepartureTime()
                .getTime(), DateUtils.FORMAT_24HOUR | DateUtils.FORMAT_SHOW_TIME));

        final TextView relativeTime = (TextView) view.findViewById(R.trip_time.relative_time);
        relativeTime.setText(DateUtils.getRelativeTimeSpanString(stopTime.getDepartureTime()
                .getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE));

        final boolean isInPast = stopTime.getDepartureTime().before(new Date());
        final boolean hasToBeHighlighted = initialStopId.equals(stopTime.getStop().getId());

        // if the current view represents a stop where the bus is already passed
        // sets the font color to grey
        if (isInPast) {
            if (!hasToBeHighlighted) {
                stopName.setTextAppearance(context, R.style.text_grey_20_bold);
                departureTime.setTextAppearance(context, R.style.text_grey_20_bold);
                relativeTime.setTextAppearance(context, R.style.text_grey_20);
            } else {
                stopName.setTextAppearance(context, R.style.text_grey_40_bold);
                departureTime.setTextAppearance(context, R.style.text_grey_40_bold);
                relativeTime.setTextAppearance(context, R.style.text_grey_40);
            }
        }

        // If the current view is the view on which a marker is needed
        // sets a blue drawable on the left
        if (hasToBeHighlighted) {
            ((LinearLayout) view).setBackgroundResource(R.drawable.bgx_li_emphasis);
        }

        if (routeIsAccessible) {
            /* Display handistar icon if current stop and current route are accessible. */
            final ImageView handistar = (ImageView) view.findViewById(R.trip_time.stop_wheelchair);
            if (context.getApplicationContext().getAccessibilityService()
                    .isAccessible(stopTime.getStop().getId(), TypeConstants.TYPE_BUS)) {
                handistar.setVisibility(View.VISIBLE);
            }
        }

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

    /**
     * Sets the initialStopId.
     * 
     * @param initialStopId
     *            the initialStopId to set
     */
    public void setInitialStopId(final String initialStopId) {

        this.initialStopId = initialStopId;
    }

    /**
     * Sets the arrivalAndDepartures.
     * 
     * @param arrivalAndDepartures
     *            the arrivalAndDepartures to set
     */
    public void setArrivalAndDepartures(final List<TripStopTime> arrivalAndDepartures) {

        this.arrivalAndDepartures = arrivalAndDepartures;
    }

}