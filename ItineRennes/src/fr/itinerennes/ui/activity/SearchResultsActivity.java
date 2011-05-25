package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

import fr.itinerennes.R;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.ui.adapter.WrapperAdapter;
import fr.itinerennes.utils.ResourceResolver;

/**
 * Displays search results.
 * 
 * @author Jérémie Huchet
 */
public final class SearchResultsActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsActivity.class);

    // /**
    // * Initialized in {@link #onCreate(Bundle)}, a reference to the list view displaying search
    // * results.
    // */
    // private ListView resultsList;

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_results);

        onNewIntent(getIntent());
    }

    /**
     * Refreshes the search results list view.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected void onNewIntent(final Intent intent) {

        super.onNewIntent(intent);
        final String query = intent.getStringExtra(SearchManager.QUERY);

        // search for markers
        final Cursor cMarkers = getApplicationContext().getMarkerDao().searchMarkers(query);

        // prepare a view binder used to bind views generated by listview adapters
        final ViewBinder viewBinder = new SearchResultsListItemViewBinder(this);

        // prepare adapter to show marker results
        final String[] from = new String[] { MarkersColumns.TYPE, MarkersColumns.LABEL };
        final int[] to = new int[] { R.id.search_result_marker_icon,
                R.id.search_result_marker_label };
        final SimpleCursorAdapter markersAdapter = new SimpleCursorAdapter(this,
                R.layout.li_search_result_marker, cMarkers, from, to);
        markersAdapter.setViewBinder(viewBinder);

        // prepare adapter to show nominatim results, it is initially empty
        // final SimpleCursorAdapter nominatimAdapter = new SimpleCursorAdapter(this,
        // R.layout.li_search_result_, cols, from, to);
        // nominatimAdapter.setViewBinder(viewBinder);

        // wrap the marker and the nominatim adapters and set them to the list view
        final WrapperAdapter wrapper = new WrapperAdapter(
                new Adapter[] { markersAdapter /* nominatimAdapter */});

        final ListView resultsList = (ListView) findViewById(R.id.search_results_list);
        resultsList.setAdapter(wrapper);
    }

    /**
     * Utility to bind views for the search result list view.
     * 
     * @author Jérémie Huchet
     */
    private static class SearchResultsListItemViewBinder implements ViewBinder {

        /** The context. */
        private final Context context;

        /**
         * Creates the view binder.
         * 
         * @param context
         *            the context
         */
        public SearchResultsListItemViewBinder(final Context context) {

            this.context = context;
        }

        /**
         * {@inheritDoc}
         * 
         * @see android.widget.SimpleCursorAdapter.ViewBinder#setViewValue(android.view.View,
         *      android.database.Cursor, int)
         */
        @Override
        public boolean setViewValue(final View view, final Cursor cursor, final int columnIndex) {

            boolean handled = false;

            if (R.id.search_result_marker_icon == view.getId()) {
                // want to bind the marker icon view
                final String type = cursor.getString(columnIndex);
                final int resId = ResourceResolver.getDrawableId(context,
                        String.format("ic_activity_title_%s", type), 0);
                ((ImageView) view).setImageResource(resId);
                handled = true;
            }
            return handled;
        }
    }
}
