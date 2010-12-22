package fr.itinerennes.ui.adapter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.database.DataSetObserver;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.model.BusDeparture;

/**
 * @author Jérémie Huchet
 */
public class BusTimeAdapter implements ListAdapter {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BusTimeAdapter.class);

    /** The android context. */
    private final Context context;

    /** Bus time data. */
    private final List<BusDeparture> data;

    public BusTimeAdapter(final Context context, final List<BusDeparture> departures) {

        this.data = departures;
        this.context = context;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#registerDataSetObserver(android.database.DataSetObserver)
     */
    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("registerDataSetObserver.start");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("registerDataSetObserver.end");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#unregisterDataSetObserver(android.database.DataSetObserver)
     */
    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unregisterDataSetObserver.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("unregisterDataSetObserver.end");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getCount.start");
        }

        return data.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public BusDeparture getItem(final int position) {

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
    public long getItemId(final int position) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getItemId.start");
        }
        return position;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#hasStableIds()
     */
    @Override
    public boolean hasStableIds() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("hasStableIds.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("hasStableIds.end");
        }
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getView.start");
        }

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View busTimeView = inflater.inflate(R.layout.bus_time, null);

        final ImageView departureLineIconeView = (ImageView) busTimeView
                .findViewById(R.station.bus_icon_line_departure);
        departureLineIconeView.setImageDrawable(context.getResources().getDrawable(
                R.drawable.tmp_lm1));

        final TextView departureHeadsignView = (TextView) busTimeView
                .findViewById(R.station.bus_headsign_departure);
        departureHeadsignView.setText(data.get(position).getHeadsign());

        final TextView departureDateView = (TextView) busTimeView
                .findViewById(R.station.bus_date_departure);
        departureDateView.setText(data.get(position).getDepartureDate().toLocaleString());

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
     * @see android.widget.Adapter#getItemViewType(int)
     */
    @Override
    public int getItemViewType(final int position) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getItemViewType.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getItemViewType.end");
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getViewTypeCount()
     */
    @Override
    public int getViewTypeCount() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getViewTypeCount.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getViewTypeCount.end");
        }
        return 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#isEmpty()
     */
    @Override
    public boolean isEmpty() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isEmpty.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isEmpty.end");
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.ListAdapter#areAllItemsEnabled()
     */
    @Override
    public boolean areAllItemsEnabled() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("areAllItemsEnabled.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("areAllItemsEnabled.end");
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.ListAdapter#isEnabled(int)
     */
    @Override
    public boolean isEnabled(final int position) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isEnabled.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isEnabled.end");
        }
        return false;
    }
}
