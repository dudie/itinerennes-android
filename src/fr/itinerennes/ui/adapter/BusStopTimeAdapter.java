package fr.itinerennes.ui.adapter;

import java.util.Date;

import android.content.res.Resources;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fr.dudie.onebusaway.model.ScheduleStopTime;
import fr.dudie.onebusaway.model.StopSchedule;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.commons.utils.StringUtils;
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
    private StopSchedule data;

    /** Global instance of Layout inflater. */
    private final LayoutInflater inflater;

    /**
     * A trip identifier which must be highlighted in the schedule. May be null if there no trip to
     * highlight.
     */
    private String tripIdToHighlight;

    /**
     * Is the stop accessible ? Determines if the handistar icon should be displayed for accessible
     * routes.
     */
    private final boolean stopIsAccessible;

    /** A time used for date calculation to avoid multiple instantiations. */
    private static Time sThenTime;

    /**
     * Constructor.
     * 
     * @param context
     *            The android context
     * @param stopIsAccessible
     *            true if the stop of the given schedule is accessible
     */
    public BusStopTimeAdapter(final ItineRennesActivity context, final boolean stopIsAccessible) {

        this.context = context;
        this.lineIconService = context.getApplicationContext().getLineIconService();
        this.inflater = LayoutInflater.from(context);
        this.stopIsAccessible = stopIsAccessible;

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {

        if (data != null) {
            return data.getStopTimes().size();
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
    public final ScheduleStopTime getItem(final int position) {

        if (data != null) {
            return data.getStopTimes().get(position);
        } else
            return null;
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

        timeBeforeDepartureView.setText(getRelativeTimeSpanString(stopTime.getDepartureTime()
                .getTime(), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));

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
     * <li>else, displays directly the localized date</li>
     * </ul>
     * 
     * @param date
     *            a date to format
     * @return the date formatted
     */
    private String formatDepartureDate(final Date date) {

        final long now = System.currentTimeMillis();
        final boolean past = (now >= date.getTime());

        final int days = getNumberOfDaysPassed(date.getTime(), now);

        final String timeToDisplay = DateUtils.formatDateTime(context, date.getTime(),
                DateUtils.FORMAT_24HOUR | DateUtils.FORMAT_SHOW_TIME);

        switch (days) {
        case 0:
            return context.getResources().getString(R.string.date_today_at, timeToDisplay);

        case 1:
            if (past) {
                return context.getResources().getString(R.string.date_yesterday_at, timeToDisplay);
            } else {
                return context.getResources().getString(R.string.date_tomorrow_at, timeToDisplay);
            }
        default:
            return DateUtils.formatDateTime(context, date.getTime(), DateUtils.FORMAT_24HOUR
                    | DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE);
        }
    }

    /**
     * Returns a string describing 'time' as a time relative to 'now'.
     * <p>
     * Time spans in the past are formatted like "42 minutes ago". Time spans in the future are
     * formatted like "in 42 minutes".
     * <p>
     * Inspired by {@link DateUtils#getRelativeTimeSpanString(long, long, long)}
     * 
     * @param time
     *            the time to describe, in milliseconds
     * @param now
     *            the current time in milliseconds
     * @param minResolution
     *            the minimum timespan to report. For example, a time 3 seconds in the past will be
     *            reported as "0 minutes ago" if this is set to MINUTE_IN_MILLIS. Pass one of 0,
     *            MINUTE_IN_MILLIS, HOUR_IN_MILLIS, DAY_IN_MILLIS, WEEK_IN_MILLIS
     * @return a String describing the relative time
     */
    public final CharSequence getRelativeTimeSpanString(final long time, final long now,
            final long minResolution) {

        /*
         * This method is not in DateUtils because we need Application Resources, unavailable in
         * static methods (needs application context to get one).
         */
        final Resources r = context.getResources();

        final boolean past = (now >= time);
        final long duration = Math.abs(now - time);

        final int resId;
        final long count;
        if (duration < DateUtils.MINUTE_IN_MILLIS && minResolution < DateUtils.MINUTE_IN_MILLIS) {
            count = duration / DateUtils.SECOND_IN_MILLIS;
            if (past) {
                resId = R.plurals.num_seconds_ago;
            } else {
                resId = R.plurals.in_num_seconds;
            }
        } else if (duration < DateUtils.HOUR_IN_MILLIS && minResolution < DateUtils.HOUR_IN_MILLIS) {
            count = duration / DateUtils.MINUTE_IN_MILLIS;
            if (past) {
                resId = R.plurals.num_minutes_ago;
            } else {
                resId = R.plurals.in_num_minutes;
            }
        } else if (duration < DateUtils.DAY_IN_MILLIS && minResolution < DateUtils.DAY_IN_MILLIS) {
            count = duration / DateUtils.HOUR_IN_MILLIS;
            if (past) {
                resId = R.plurals.num_hours_ago;
            } else {
                resId = R.plurals.in_num_hours;
            }
        } else if (duration < DateUtils.WEEK_IN_MILLIS && minResolution < DateUtils.WEEK_IN_MILLIS) {
            count = getNumberOfDaysPassed(time, now);
            if (past) {
                resId = R.plurals.num_days_ago;
            } else {
                resId = R.plurals.in_num_days;
            }
        } else {
            // We know that we won't be showing the time, so it is safe to pass
            // in a null context.
            return DateUtils.formatDateRange(null, time, time, 0);
        }

        final String format = r.getQuantityString(resId, (int) count);
        return String.format(format, count);
    }

    /**
     * Returns the number of days passed between two dates.
     * 
     * @param date1
     *            first date
     * @param date2
     *            second date
     * @return number of days passed between to dates.
     */
    private static synchronized int getNumberOfDaysPassed(final long date1, final long date2) {

        if (sThenTime == null) {
            sThenTime = new Time();
        }
        sThenTime.set(date1);
        final int day1 = Time.getJulianDay(date1, sThenTime.gmtoff);
        sThenTime.set(date2);
        final int day2 = Time.getJulianDay(date2, sThenTime.gmtoff);
        return Math.abs(day2 - day1);
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

    /**
     * Sets the trip to highlight in the list.
     * 
     * @param tripId
     *            the trip id to highlight
     */
    public final void setTripIdToHighlight(final String tripId) {

        this.tripIdToHighlight = tripId;

    }

    /**
     * Sets the schedule data to display.
     * 
     * @param schedule
     *            the schedule data
     */
    public final void setData(final StopSchedule schedule) {

        this.data = schedule;
        notifyDataSetChanged();

    }

}
