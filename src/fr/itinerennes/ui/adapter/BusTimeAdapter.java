package fr.itinerennes.ui.adapter;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.model.oba.Route;
import fr.itinerennes.model.oba.ScheduleStopTime;
import fr.itinerennes.model.oba.StopSchedule;
import fr.itinerennes.ui.activity.ITRContext;

/**
 * @author Jérémie Huchet
 */
public class BusTimeAdapter extends BaseAdapter {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(BusTimeAdapter.class);

    /** The android context. */
    private final ITRContext context;

    /** Stop complete schedule. */
    private final StopSchedule data;

    /** Map containing icons for routes. */
    private final HashMap<String, Drawable> routesIcons;

    /** Global instance of Layout inflater. */
    private LayoutInflater inflater = null;

    /**
     * Is the stop accessible ? Determines if the handistar icon should be displayed for accessible
     * routes.
     */
    private final boolean stopIsAccessible;

    /**
     * Constructor.
     * 
     * @param c
     *            The android context
     * @param schedule
     *            departures to display in the list
     * @param routesIcons
     *            list of routes icons
     * @param stopIsAccessible
     */
    public BusTimeAdapter(final ITRContext c, final StopSchedule schedule,
            final HashMap<String, Drawable> routesIcons, final boolean stopIsAccessible) {

        this.data = schedule;
        this.context = c;
        this.routesIcons = routesIcons;
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

        final View busTimeView = inflater.inflate(R.layout.bus_time, null);
        final ScheduleStopTime stopTime = data.getStopTimes().get(position);

        final ImageView departureLineIconeView = (ImageView) busTimeView
                .findViewById(R.station.bus_departure_line_icon);
        departureLineIconeView
                .setImageDrawable(routesIcons.get(stopTime.getRoute().getShortName()));

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
            if (context.getAccessibilityService().isAccessible(
                    data.getStopTimes().get(position).getRoute().getId(), Route.class.getName())) {
                handistar.setVisibility(View.VISIBLE);
            }
        }

        // TJHU affichage du marqueur pour montrer l'entrée initiale
        // If the current view is the view on which a marker is needed
        // sets a blue drawable on the left
        // if (initialStopId.equals(stopTime.getStop().getId())) {
        // ((ImageView) view.findViewById(R.trip_time.listview_separator))
        // .setImageResource(R.drawable.listview_separator);
        // }

        // if the current view represents a stop where the bus is already passed
        // sets the font color to grey
        if (stopTime.getDepartureTime().before(new Date())) {
            departureHeadsignView.setTextAppearance(context, R.style.text_grey_bold);
            departureDateView.setTextAppearance(context, R.style.text_grey);
            timeBeforeDepartureView.setTextAppearance(context, R.style.text_grey_bold);
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
        final int days = fr.itinerennes.utils.DateUtils.getDayCount(date.getTime()
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

}
