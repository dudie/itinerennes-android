package fr.itinerennes.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;

import fr.itinerennes.R;
import fr.itinerennes.database.Columns.MarkersColumns;

/**
 * Adapter used to show a bike station list with a checkbox to select one or more stations.
 * 
 * @author Olivier Boudet
 */
public class BikeStopListAdapter extends CursorAdapter {

    /** Layout inflater. */
    private final LayoutInflater inflater;

    /** The list of bike station id which was selected. */
    private final List<String> selectedIds = new ArrayList<String>();

    /**
     * Constructor.
     * 
     * @param context
     *            The context where the ListView associated with this SimpleListItemFactory is
     *            running
     * @param c
     *            the cursor to use
     */
    public BikeStopListAdapter(final Context context, final Cursor c) {

        super(context, c);

        this.inflater = LayoutInflater.from(context);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.CursorAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public final View getView(final int position, View convertView, final ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.li_bike_selection, null);

            holder = new ViewHolder();
            holder.checkbox = (CheckBox) convertView
                    .findViewById(R.vw_widget_configuration_line.checkbox);
            holder.checkbox.setOnCheckedChangeListener(onCheckedChangeListener);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        getCursor().moveToPosition(position);
        final String stationId = getCursor().getString(
                getCursor().getColumnIndex(MarkersColumns.ID));

        holder.checkbox.setText(getCursor().getString(
                getCursor().getColumnIndex(MarkersColumns.LABEL)));
        holder.checkbox.setTag(stationId);

        if (selectedIds.contains(stationId)) {
            holder.checkbox.setChecked(true);
        } else {
            holder.checkbox.setChecked(false);
        }

        return convertView;
    }

    /**
     * View holder containing views for displaying a bike station in the list.
     * 
     * @author Olivier Boudet
     */
    private static class ViewHolder {

        /** The checkbox. */
        CheckBox checkbox;
    }

    /** Listener to manage the checked status changes. */
    private final OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

            if (isChecked) {
                addSelectedId((String) buttonView.getTag());
            } else {
                removeSelectedId((String) buttonView.getTag());
            }

        }

    };

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {

        return getCursor().getCount();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final Object getItem(final int position) {

        getCursor().moveToPosition(position);
        return getCursor();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public final long getItemId(final int position) {

        getCursor().moveToPosition(position);
        return getCursor().getLong(getCursor().getColumnIndex(MarkersColumns._ID));
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.CursorAdapter#newView(android.content.Context, android.database.Cursor,
     *      android.view.ViewGroup)
     */
    @Override
    public final View newView(final Context context, final Cursor cursor, final ViewGroup parent) {

        // not used here
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.CursorAdapter#bindView(android.view.View, android.content.Context,
     *      android.database.Cursor)
     */
    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        // not used here

    }

    /**
     * Marks a bike station as selected in the list.
     * 
     * @param id
     *            id of the bike station
     */
    public final void addSelectedId(final String id) {

        selectedIds.add(id);
    }

    /**
     * Marks a bike station as unselected in the list.
     * 
     * @param id
     *            id of the bike station
     */
    public final void removeSelectedId(final String id) {

        selectedIds.remove(id);
    }

    /**
     * Gets the list of selected bike stations.
     * 
     * @return the list of selected ids
     */
    public final List<String> getSelectedIds() {

        return selectedIds;
    }
}
