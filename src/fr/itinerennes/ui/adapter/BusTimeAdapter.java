package fr.itinerennes.ui.adapter;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * @author Jérémie Huchet
 */
public class BusTimeAdapter implements ListAdapter {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BusTimeAdapter.class);

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
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getCount.end");
        }
        return 50;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(final int position) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getItem.start");
        }
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getItem.end");
        }
        return null;
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
        // TJHU Auto-generated method stub

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getItemId.end");
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

        TextView v = (TextView) convertView;
        if (null == v) {
            v = new TextView(parent.getContext());
        }
        v.setText(String.valueOf(position));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getView.end");
        }
        return v;
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
