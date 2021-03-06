package fr.itinerennes.ui.activity;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.io.IOException;
import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Html;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

import fr.dudie.nominatim.model.Address;

import fr.itinerennes.Conf;
import fr.itinerennes.R;
import fr.itinerennes.commons.utils.SearchUtils;
import fr.itinerennes.commons.utils.StringUtils;
import fr.itinerennes.database.Columns.LocationColumns;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.database.Columns.NominatimColumns;
import fr.itinerennes.ui.adapter.WrapperAdapter;
import fr.itinerennes.utils.IOUtils;
import fr.itinerennes.utils.MapUtils;
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

    /** A reference to the textview displaying "no search results for query '%s'". */
    private TextView noResultsView;

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

        noResultsView = (TextView) findViewById(R.id.search_results_none);
        resultsList = (ListView) findViewById(R.id.search_results_list);

        // prepare adapter to show marker results
        String[] from = new String[] { MarkersColumns.TYPE, MarkersColumns.LABEL,
                MarkersColumns.CITY };
        int[] to = new int[] { R.id.search_result_marker_icon, R.id.search_result_marker_label,
                R.id.search_result_marker_city };
        markersAdapter = new SimpleCursorAdapter(this, R.layout.li_search_result_marker, null,
                from, to);

        // prepare adapter to show nominatim results, it is initially empty
        from = new String[] { NominatimColumns.DISPLAY_NAME };
        to = new int[] { R.id.search_result_nominatim_label };
        nominatimAdapter = new SimpleCursorAdapter(this, R.layout.li_search_result_nominatim, null,
                from, to);

        // wrap the marker and the nominatim adapters and set them to the list view
        resultsListAdapter = new WrapperAdapter(this, new Adapter[] { markersAdapter,
                nominatimAdapter });

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

                    if (item.getColumnIndex(MarkersColumns.TYPE) != -1
                            && item.getColumnIndex(MarkersColumns.LABEL) != -1) {
                        // item has a type and a label, it is a marker
                        final String markerId = item.getString(item.getColumnIndex(BaseColumns._ID));
                        final Cursor c = getApplicationContext().getMarkerDao()
                                .getMarkersWithSameLabel(markerId);

                        // calculate the barycenter to center the map on it
                        if (c != null && c.moveToFirst()) {

                            final GeoPoint barycentre = MapUtils.getBarycenter(c);

                            final String markerType = item.getString(item
                                    .getColumnIndex(MarkersColumns.TYPE));

                            c.close();

                            startActivity(MapActivity.IntentFactory.getCenterOnLocationIntent(
                                    getApplicationContext(), barycentre.getLatitudeE6(),
                                    barycentre.getLongitudeE6(), Conf.MAP_ZOOM_ON_LOCATION,
                                    markerType));

                        }

                    } else {
                        // item does not have a type and a label, so it is a simple location
                        final int latE6 = item.getInt(item.getColumnIndex(LocationColumns.LATITUDE));
                        final int lonE6 = item.getInt(item
                                .getColumnIndex(LocationColumns.LONGITUDE));

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(
                                    "preparing intent to display map centered on (lon=%s; lat=%s)",
                                    lonE6, latE6);
                        }

                        startActivity(MapActivity.IntentFactory.getCenterOnLocationIntent(
                                getApplicationContext(), latE6, lonE6, Conf.MAP_ZOOM_ON_LOCATION));
                    }

                }
            }
        });

        onNewIntent(getIntent());

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected void onNewIntent(final Intent intent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.start");
        }

        final String query = intent.getStringExtra(SearchManager.QUERY);

        // prepare a view binder used to bind views generated by listview adapters
        final ViewBinder viewBinder = new SearchResultsListItemViewBinder(this, query);
        markersAdapter.setViewBinder(viewBinder);
        nominatimAdapter.setViewBinder(viewBinder);

        // search for markers
        final Cursor cMarkers = getApplicationContext().getMarkerDao().searchMarkers(query);

        // search for the nominatim results in background
        final AsyncTask<Void, Void, Cursor> nominatimSearchTask = new AsyncTask<Void, Void, Cursor>() {

            /**
             * Set the nominatim adapter "loading" flag to display a loading message.
             * 
             * @see android.os.AsyncTask#onPreExecute()
             */
            @Override
            protected void onPreExecute() {

                resultsListAdapter.setLoading(nominatimAdapter, true);
            }

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
                    noResultsView.setText(getString(R.string.no_results, query));
                } else {
                    resultsList.setVisibility(View.VISIBLE);
                    noResultsView.setVisibility(View.GONE);
                }
            }
        };

        // update/clear the current list)
        markersAdapter.changeCursor(cMarkers);
        nominatimAdapter.changeCursor(null);

        // refresh the UI: make the result list visible and hide "no result" label
        resultsList.setVisibility(View.VISIBLE);
        noResultsView.setVisibility(View.GONE);

        // starts task to executes nominatim search
        nominatimSearchTask.execute((Void) null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.end");
        }
    }

    @Override
    protected void onDestroy() {

        IOUtils.close(markersAdapter.getCursor());
        IOUtils.close(nominatimAdapter.getCursor());
        super.onDestroy();
    }

    /**
     * Utility to bind views for the search result list view.
     * 
     * @author Jérémie Huchet
     */
    private static class SearchResultsListItemViewBinder implements ViewBinder {

        /** The context. */
        private final Context context;

        /** The query text to highlight. */
        private final String query;

        /**
         * Creates the view binder.
         * 
         * @param context
         *            the context
         * @param query
         *            the search query
         */
        public SearchResultsListItemViewBinder(final Context context, final String query) {

            this.context = context;
            this.query = query;
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

            switch (view.getId()) {
            case R.id.search_result_marker_icon:
                // want to bind the marker icon view
                final String type = cursor.getString(columnIndex);
                final int resId = ResourceResolver.getDrawableId(context,
                        String.format("ic_activity_title_%s", type), 0);
                ((ImageView) view).setImageResource(resId);
                handled = true;
                break;
            case R.id.search_result_marker_label:
            case R.id.search_result_nominatim_label:
                final String html = SearchUtils.highlight(cursor.getString(columnIndex), query,
                        "<b>", "</b>");
                ((TextView) view).setText(Html.fromHtml(html));
                handled = true;
                break;
            case R.id.search_result_marker_city:
                if (StringUtils.isBlank(cursor.getString(columnIndex))) {
                    view.setVisibility(View.GONE);
                }
                // let android handle binding
                handled = false;
                break;
            default:
                break;
            }
            return handled;
        }
    }
}
