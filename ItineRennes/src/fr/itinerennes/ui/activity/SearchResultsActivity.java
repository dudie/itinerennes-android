package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import fr.itinerennes.ui.adapter.SearchResultsAdapter;

/**
 * Displays search results.
 * 
 * @author Jérémie Huchet
 */
public final class SearchResultsActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsActivity.class);

    /** Intent parameter name to pass the query string. */
    public static final String INTENT_QUERY = String.format("%s.query",
            SearchResultsActivity.class.getName());

    /**
     * Initialized in {@link #onCreate(Bundle)}, a reference to the list view displaying search
     * results.
     */
    private ListView resultsList;

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // setContentView(R.layout.act_search_results);
        // resultsList = findViewById(R.id.search_results_list);

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
        final SearchResultsAdapter adapter = new SearchResultsAdapter();
        resultsList.setAdapter(adapter);
    }
}
