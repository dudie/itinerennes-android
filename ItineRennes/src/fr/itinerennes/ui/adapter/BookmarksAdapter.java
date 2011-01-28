package fr.itinerennes.ui.adapter;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.database.Cursor;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import fr.itinerennes.R;
import fr.itinerennes.database.Columns.BookmarksColumns;
import fr.itinerennes.model.BikeStation;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.model.SubwayStation;

/**
 * @author Jérémie Huchet
 */
public class BookmarksAdapter extends SimpleCursorAdapter implements BookmarksColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BookmarksAdapter.class);

    /** A map containing icons to use for each type of bookmarks. */
    private static final Map<String, Integer> ICONS = new HashMap<String, Integer>();
    static {
        ICONS.put(BikeStation.class.getName(), R.drawable.bike_activity_title_icon);
        ICONS.put(BusStation.class.getName(), R.drawable.bus_activity_title_icon);
        ICONS.put(SubwayStation.class.getName(), R.drawable.subway_activity_title_icon);
    }

    /**
     * Creates the bookmarks adapter.
     * 
     * @param context
     *            the application context
     */
    public BookmarksAdapter(final Context context, final Cursor cursor) {

        super(context, R.layout.row_bookmark, cursor, new String[] { LABEL, TYPE }, new int[] {
                R.id.bookmark_label, R.id.bookmark_type_icon });
    }

    /**
     * Binds the given value from the cursor to the given image view. Uses {@link #ICONS} to find
     * the drawable identifier to set to the image view.
     * 
     * @param view
     *            ImageView to receive an image
     * @param type
     *            the value retrieved from the cursor
     * @see android.widget.SimpleCursorAdapter#setViewImage(android.widget.ImageView,
     *      java.lang.String)
     */
    @Override
    public final void setViewImage(final ImageView view, final String type) {

        final Integer iconResId = ICONS.get(type);
        if (null != iconResId) {
            view.setImageResource(iconResId);
        }
    }
}
