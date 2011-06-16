package fr.itinerennes.ui.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;

import fr.itinerennes.R;

/**
 * An adapter to wrap other adapters and displays multiple.
 * 
 * @author Jérémie Huchet
 */
public final class WrapperAdapter extends BaseAdapter {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(WrapperAdapter.class);

    /** The layout inflater. */
    private final LayoutInflater inflater;

    /** The wrapped adapters. */
    private final Adapter[] adapters;

    /**
     * A boolean value for each {@link #adapters} to determine whether or not a<em>loading</em> view
     * should be rendered.
     */
    private final boolean[] isLoading;

    /**
     * Creates a wrapper adapter.
     * 
     * @param context
     *            the context
     * @param adapters
     *            the adapters to wrap
     */
    public WrapperAdapter(final Context context, final Adapter[] adapters) {

        this.inflater = LayoutInflater.from(context);
        this.adapters = adapters;
        this.isLoading = new boolean[this.adapters.length];
        for (int i = 0; i < this.adapters.length; i++) {
            this.adapters[i].registerDataSetObserver(new WrappedAdapterDataSetObserver());
            this.isLoading[i] = false;
        }
    }

    /**
     * Observes wrapped datasets and dispatch events to the view.
     * 
     * @author Jérémie Huchet
     */
    private final class WrappedAdapterDataSetObserver extends DataSetObserver {

        @Override
        public void onChanged() {

            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {

            notifyDataSetInvalidated();
        }
    }

    /**
     * Set whether of not a <em>loading view</em> should be rendered at the bottom of the given
     * adapter.
     * 
     * @param adapter
     *            an adapter wrapped into this wrapper adapter
     * @param isLoading
     *            <code>true</code> if a <em>loading view</em> should be rendered
     */
    public void setLoading(final Adapter adapter, final boolean isLoading) {

        final int i = indexOf(adapters, adapter);
        this.isLoading[i] = isLoading;
        notifyDataSetChanged();
    }

    /**
     * Gets the index of the given adapter in the given array of adapters.
     * 
     * @param adaptersList
     *            an array of adapters
     * @param adapter
     *            the adapter you search the index
     * @return the index of the adapter or <code>-1</code> if the array doesn't contain it
     */
    public static int indexOf(final Adapter[] adaptersList, final Adapter adapter) {

        int index = -1;

        if (null != adapter) {
            for (int i = 0; index == -1 && i < adaptersList.length; i++) {
                if (adapter.equals(adaptersList[i])) {
                    index = i;
                }
            }
        }

        return index;
    }

    /**
     * Gets the total count of items adapted by all the wrapped adapters.
     * 
     * @return the item count
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {

        int count = 0;
        for (int i = 0; i < adapters.length; i++) {
            // cumul the adapters count
            count += adapters[i].getCount();

            // add one item if a loading view has to be rendered
            if (isLoading[i]) {
                count++;
            }
        }
        return count;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(final int position) {

        Object item = null;

        int minPosition = 0;
        int maxPosition = 0;

        for (int i = 0; item == null && i < adapters.length; i++) {

            minPosition += maxPosition;
            maxPosition += adapters[i].getCount();

            if (position < maxPosition) {
                item = adapters[i].getItem(position - minPosition);
            } else if (isLoading[i] && position == maxPosition++) {
                break;
            }
        }
        return item;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(final int position) {

        return position;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.BaseAdapter#getViewTypeCount()
     */
    @Override
    public int getViewTypeCount() {

        // +1 for the loading view
        return adapters.length + 1;
    }

    /**
     * <strong>We do not reuse the convertView parameter because view may be rendered anywhere in
     * the listview.</strong>
     * <p>
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View view = null;

        int minPosition = 0;
        int maxPosition = 0;

        for (int i = 0; view == null && i < adapters.length; i++) {

            minPosition += maxPosition;
            maxPosition += adapters[i].getCount();

            if (adapters[i].getCount() > 0 && position < maxPosition) {
                view = adapters[i].getView(position - minPosition, null, parent);
            } else if (isLoading[i] && position == maxPosition++) {
                view = inflater.inflate(R.layout.li_loading, parent, false);
            }
        }

        return view;
    }
}
