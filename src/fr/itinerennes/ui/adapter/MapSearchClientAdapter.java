package fr.itinerennes.ui.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import fr.itinerennes.search.client.SearchClient;
import fr.itinerennes.search.model.AddressResultDetails;
import fr.itinerennes.search.model.SearchResult;

/**
 * An adapter to autocomplete search results for the map activity.
 * 
 * @author Jérémie Huchet
 */
public class MapSearchClientAdapter extends BaseAdapter implements Filterable {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MapSearchClientAdapter.class);

    /** The activity context. */
    private final Context context;

    /** The search client to user to find the results. */
    private final SearchClient searchClient;

    /** The list of results for the current typed constraint. */
    private List<SearchResult> searchResults = new ArrayList<SearchResult>(0);

    /**
     * Creates the search client autocomplete adapter.
     * 
     * @param context
     *            the context
     * @param searchClient
     *            the search client
     */
    public MapSearchClientAdapter(final Context context, final SearchClient searchClient) {

        this.context = context;
        this.searchClient = searchClient;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {

        return searchResults.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final Object getItem(final int position) {

        return searchResults.get(position);
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

        final TextView view = new TextView(context);
        view.setText(((AddressResultDetails) searchResults.get(position)).getDisplayName());
        return view;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Filterable#getFilter()
     */
    @Override
    public final Filter getFilter() {

        return new Filter() {

            /** A map caching the history results. */
            private final Map<String, List<SearchResult>> cache = new HashMap<String, List<SearchResult>>();

            /**
             * {@inheritDoc}
             * 
             * @see android.widget.Filter#performFiltering(java.lang.CharSequence)
             */
            @Override
            protected final FilterResults performFiltering(final CharSequence constraint) {

                LOGGER.debug("FILTER CONSTRAINT = " + constraint);

                final String query = String.valueOf(constraint).toLowerCase();
                List<SearchResult> searchRes = null;

                // do not start a search query for a input string lower than 3 characters
                if (query != null && query.length() >= 3) {

                    searchRes = cache.get(query);

                    if (searchRes == null) {
                        LOGGER.debug("making new search");
                        try {
                            searchRes = searchClient.search(query);
                        } catch (final IOException e) {
                            // TJHU Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        LOGGER.debug("using cached result list");
                    }
                }

                // if there was an error, we reinitialize the result list
                if (searchRes == null) {
                    searchRes = new ArrayList<SearchResult>(0);
                }

                // cache the new result if needed
                if (!cache.containsKey(query)) {
                    LOGGER.debug("caching new search result list ({} results)", searchRes.size());
                    cache.put(query, searchRes);
                }

                // clear the search history cache (if the user uses backspace, then the previous
                // result needs to be removed to free memory)
                final Iterator<String> i = cache.keySet().iterator();
                while (i.hasNext()) {
                    final String histQ = i.next();
                    if (!query.startsWith(histQ)) {
                        LOGGER.debug("remove obsolete cached result list for query={}", histQ);
                        i.remove();
                    }
                }

                final FilterResults results = new FilterResults();
                results.values = searchRes;
                results.count = searchRes.size();

                return results;
            }

            /**
             * {@inheritDoc}
             * 
             * @see android.widget.Filter#publishResults(java.lang.CharSequence,
             *      android.widget.Filter.FilterResults)
             */
            @SuppressWarnings("unchecked")
            @Override
            protected final void publishResults(final CharSequence constraint,
                    final FilterResults results) {

                searchResults = (List<SearchResult>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

        };
    }
}
