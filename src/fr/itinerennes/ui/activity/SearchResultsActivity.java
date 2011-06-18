package fr.itinerennes.ui.activity;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.database.Columns.LocationColumns;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.database.Columns.NominatimColumns;
import fr.itinerennes.nominatim.model.Address;
import fr.itinerennes.ui.adapter.WrapperAdapter;
import fr.itinerennes.utils.NominatimTranslator;
import fr.itinerennes.utils.ResourceResolver;

/**
 * Displays search results.
 * 
 * @author Jérémie Huchet
 */
public final class SearchResultsActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsActivity.class);

    /** A reference to the list view displaying search results. */
    private ListView resultsList;

    /** A reference to the view displaying "no search  results". */
    private View noResultsView;

    /** A reference to the textview displaying "no search results for query '%s'". */
    private TextView noResultsLabel;

    /** The results list adapter. */
    private WrapperAdapter resultsListAdapter;

    /** The results adapter for marker results. */
    private SimpleCursorAdapter markersAdapter;

    /** The results adapter for nominatim results. */
    private SimpleCursorAdapter nominatimAdapter;

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search_results);

        noResultsView = findViewById(R.id.search_results_none);
        noResultsLabel = (TextView) noResultsView.findViewById(R.id.search_results_no_results);
        resultsList = (ListView) findViewById(R.id.search_results_list);

        // prepare a view binder used to bind views generated by listview adapters
        final ViewBinder viewBinder = new SearchResultsListItemViewBinder(this);

        // prepare adapter to show marker results
        String[] from = new String[] { MarkersColumns.TYPE, MarkersColumns.LABEL };
        int[] to = new int[] { R.id.search_result_marker_icon, R.id.search_result_marker_label };
        markersAdapter = new SimpleCursorAdapter(this, R.layout.li_search_result_marker, null,
                from, to);
        markersAdapter.setViewBinder(viewBinder);

        // prepare adapter to show nominatim results, it is initially empty
        from = new String[] { NominatimColumns.DISPLAY_NAME };
        to = new int[] { R.id.search_result_nominatim_label };
        nominatimAdapter = new SimpleCursorAdapter(this, R.layout.li_search_result_nominatim, null,
                from, to);
        nominatimAdapter.setViewBinder(viewBinder);

        // wrap the marker and the nominatim adapters and set them to the list view
        resultsListAdapter = new WrapperAdapter(this, new Adapter[] { markersAdapter,
                nominatimAdapter });
        resultsListAdapter.setLoading(nominatimAdapter, true);

        resultsList.setAdapter(resultsListAdapter);

        resultsList.setOnItemClickListener(new OnItemClickListener() {

            /**
             * Triggered when a item of the result list is clicked.
             * <ol>
             * <li>If its a Marker result item, then open the MapActivity and center it on the
             * marker.</li>
             * <li>If its a Nominatim result item, then open the MapActivity and center it on the
             * marker.</li>
             * <li>If its a loading view, then do nothing.</li>
             * </ol>
             * 
             * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView,
             *      android.view.View, int, long)
             */
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("onItemClick - position=%s", position);
                }

                final Cursor item = (Cursor) resultsListAdapter.getItem(position);
                if (null != item) {
                    final int latE6 = item.getInt(item.getColumnIndex(LocationColumns.LATITUDE));
                    final int lonE6 = item.getInt(item.getColumnIndex(LocationColumns.LONGITUDE));

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(
                                "preparing intent to display map centered on (lon=%s; lat=%s)",
                                lonE6, latE6);
                    }

                    final Intent i = new Intent(getBaseContext(), MapActivity.class);
                    i.setAction(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(MapActivity.INTENT_SET_MAP_ZOOM, 17);
                    i.putExtra(MapActivity.INTENT_SET_MAP_LON, lonE6);
                    i.putExtra(MapActivity.INTENT_SET_MAP_LAT, latE6);
                    startActivity(i);
                }
            }
        });

        onNewIntent(getIntent());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.start");
        }
        super.onNewIntent(intent);

        final String query = intent.getStringExtra(SearchManager.QUERY);

        // search for markers
        final Cursor cMarkers = getApplicationContext().getMarkerDao().searchMarkers(query);
        markersAdapter.changeCursor(cMarkers);

        // search for the nominatim results in background
        final AsyncTask<Void, Void, Cursor> nominatimSearchTask = new AsyncTask<Void, Void, Cursor>() {

            /**
             * Executes a search through nominatim in background.
             * 
             * @see android.os.AsyncTask#doInBackground(Params[])
             */
            @Override
            protected Cursor doInBackground(final Void... params) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("doInBackground.start - query={}", query);
                }

                Cursor c = null;
                try {
                    final List<Address> results = getApplicationContext().getNominatimClient()
                            .search(String.valueOf(query));
                    c = NominatimTranslator.toCursor(results);
                } catch (final IOException e) {
                    // TJHU Handle Nominatim IO exception
                    getApplicationContext().getExceptionHandler().handleException(e);
                }
                if (null == c) {
                    c = NominatimTranslator.emptyCursor();
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("doInBackground.end - resultCount={}", c.getCount());
                }
                return c;
            }

            /**
             * <ul>
             * <li>removes the loading flag from the nominatim result list adapter</li>
             * <li>uptades its content (change the cursor containing results)</li>
             * <li>shows "no result found" if there is no marker and nominatim result</li>
             * </ul>
             * 
             * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
             */
            @Override
            protected void onPostExecute(final Cursor result) {

                resultsListAdapter.setLoading(nominatimAdapter, false);
                nominatimAdapter.changeCursor(result);

                if (resultsListAdapter.getCount() == 0) {
                    resultsList.setVisibility(View.GONE);
                    noResultsView.setVisibility(View.VISIBLE);
                    noResultsLabel.setText(getString(R.string.no_results, query));
                } else {
                    resultsList.setVisibility(View.VISIBLE);
                    noResultsView.setVisibility(View.GONE);
                }
            }
        };
        // starts task to executes nominatim search
        nominatimSearchTask.execute((Void) null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.end");
        }
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
