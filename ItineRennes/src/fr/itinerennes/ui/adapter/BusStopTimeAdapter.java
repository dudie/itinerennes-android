package fr.itinerennes.ui.adapter;

import java.util.Calendar;
import java.util.Date;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.commons.utils.StringUtils;
import fr.itinerennes.onebusaway.model.ScheduleStopTime;
import fr.itinerennes.onebusaway.model.StopSchedule;
import fr.itinerennes.ui.activity.ItineRennesActivity;

/**
 * @author Jérémie Huchet
 */
public class BusStopTimeAdapter extends BaseAdapter {

    /** The android context. */
    private final ItineRennesActivity context;

    /** The line icon service. */
    private final LineIconService lineIconService;

    /** Stop complete schedule. */
    private final StopSchedule data;

    /** Global instance of Layout inflater. */
    private final LayoutInflater inflater;

    /**
     * A trip identifier which must be highlighted in the schedule. May be null if there no trip to
     * highlight.
     */
    private final String tripIdToHighlight;

    /**
     * Is the stop accessible ? Determines if the handistar icon should be displayed for accessible
     * routes.
     */
    private final boolean stopIsAccessible;

    /**
     * Constructor.
     * 
     * @param context
     *            The android context
     * @param schedule
     *            departures to display in the list
     * @param tripIdToHighlight
     *            a trip id to highlight in the schedule, or null to not highlight any trip
     * @param stopIsAccessible
     *            true if the stop of the given schedule is accessible
     */
    public BusStopTimeAdapter(final ItineRennesActivity context, final StopSchedule schedule,
            final String tripIdToHighlight, final boolean stopIsAccessible) {

        this.data = schedule;
        this.context = context;
        this.lineIconService = context.getApplicationContext().getLineIconService();
        this.inflater = LayoutInflater.from(context);
        this.tripIdToHighlight = tripIdToHighlight;
        this.stopIsAccessible = stopIsAccessible;

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {

        return data.getStopTimes().size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final ScheduleStopTime getItem(final int position) {

        return data.getStopTimes().get(position);
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

        final View busTimeView = inflater.inflate(R.layout.li_stop_time_bus, null);
        final ScheduleStopTime stopTime = data.getStopTimes().get(position);

        final ImageView departureLineIconeView = (ImageView) busTimeView
                .findViewById(R.station.bus_departure_line_icon);
        departureLineIconeView.setImageDrawable(lineIconService.getIconOrDefault(context, stopTime
                .getRoute().getShortName()));

        final TextView departureHeadsignView = (TextView) busTimeView
                .findViewById(R.station.bus_departure_headsign);
        departureHeadsignView.setText(stopTime.getSimpleHeadsign());

        final TextView departureDateView = (TextView) busTimeView
                .findViewById(R.station.bus_departure_date);
        departureDateView.setText(formatDepartureDate(stopTime.getDepartureTime()));

        final TextView timeBeforeDepartureView = (TextView) busTimeView
                .findViewById(R.station.bus_departure_time_before);

        timeBeforeDepartureView.setText(DateUtils.getRelativeTimeSpanString(stopTime
                .getDepartureTime().getTime(), System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE));

        if (stopIsAccessible) {
            // TOBO est ce que c'est nécessaire de mettre dans une map les attributs déjà récupérés
            // pour éviter de faire toujours les mêmes requêtes lorsque l'utilisateur scroll ?

            /* Display handistar icon if current stop and current route are accessible. */
            final ImageView handistar = (ImageView) busTimeView
                    .findViewById(R.station.bus_departure_wheelchair);
            if (context
                    .getApplicationContext()
                    .getAccessibilityService()
                    .isAccessible(data.getStopTimes().get(position).getRoute().getId(),
                            TypeConstants.TYPE_BUS_ROUTE)) {
                handistar.setVisibility(View.VISIBLE);
            }
        }

        final boolean isInPast = stopTime.getDepartureTime().before(new Date());
        final boolean hasToBeHighlighted = null != tripIdToHighlight
                && tripIdToHighlight.equals(stopTime.getTripId());

        // if the current view represents a stop where the bus is already passed
        // sets the font color to grey
        if (isInPast) {
            if (!hasToBeHighlighted) {
                departureHeadsignView.setTextAppearance(context, R.style.text_grey_20_bold);
                departureDateView.setTextAppearance(context, R.style.text_grey_20);
                timeBeforeDepartureView.setTextAppearance(context, R.style.text_grey_20_bold);
            } else {
                departureHeadsignView.setTextAppearance(context, R.style.text_grey_40_bold);
                departureDateView.setTextAppearance(context, R.style.text_grey_40);
                timeBeforeDepartureView.setTextAppearance(context, R.style.text_grey_40_bold);
            }
        }

        if (hasToBeHighlighted) {
            busTimeView.setBackgroundResource(R.drawable.bgx_li_emphasis);
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

        return (super.getViewTypeCount() + 1);
    }

    /**
     * Formats the given departure date.
     * <ul>
     * <li>if today, displays : 10h05</li>
     * <li>if tomorrow, displays : tomorrow at 10h05</li>
     * <li>if after, displays : in * days at 10h05</li>
     * </ul>
     * 
     * @param date
     *            a date to format
     * @return the date formatted
     */
    private String formatDepartureDate(final Date date) {

        final Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        final int days = fr.itinerennes.commons.utils.DateUtils.getDayCount(date.getTime()
                - c.getTimeInMillis());
        final String time = DateUtils.formatDateTime(context, date.getTime(),
                DateUtils.FORMAT_24HOUR | DateUtils.FORMAT_SHOW_TIME);
        switch (days) {
        case 0:
            return context.getResources().getString(R.string.date_today_at, time);
        case 1:
            return context.getResources().getString(R.string.date_tomorrow_at, time);
        default:
            return context.getResources().getString(R.string.date_in_x_days_at, days, time);
        }
    }

    /**
     * Gets the index of the list corresponding to the current time.
     * 
     * @return index of the view for the current time
     */
    public final int getIndexForNow() {

        final Date now = new Date();
        final int length = data.getStopTimes().size();

        for (int i = 0; i < length; i++) {
            if (data.getStopTimes().get(i).getDepartureTime().compareTo(now) > 0) {
                return i;
            }
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.BaseAdapter#isEmpty()
     */
    @Override
    public final boolean isEmpty() {

        return getCount() == 0;
    }

    /**
     * Gets the index of the item on which the list should be scrolled at first display.
     * 
     * @return the position of element where the list should be scrolled at first display
     */
    public final int getInitialIndex() {

        int index = -1;

        if (StringUtils.isBlank(tripIdToHighlight)) {
            index = getIndexForNow();
        } else {
            final int length = data.getStopTimes().size();
            for (int i = 0; index == -1 && i < length; i++) {
                if (data.getStopTimes().get(i).getTripId().equalsIgnoreCase(tripIdToHighlight)) {
                    index = i;
                }
            }
        }

        if (index == -1) {
            index = 0;
        }

        return index;
    }

}
