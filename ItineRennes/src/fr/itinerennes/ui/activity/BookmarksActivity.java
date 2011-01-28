package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import fr.itinerennes.R;
import fr.itinerennes.database.Columns.BookmarksColumns;
import fr.itinerennes.ui.adapter.BookmarksAdapter;

/**
 * This activity displays bookmarks items the user starred.
 * 
 * @author Jérémie Huchet
 */
public class BookmarksActivity extends ITRContext implements BookmarksColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BookmarksActivity.class);

    /**
     * Loads the bookmarks and display them in a list view.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        // retrieve all bookmarks
        final Cursor c = getDatabaseHelper().getReadableDatabase().query(BOOKMARKS_TABLE_NAME,
                new String[] { "_id", LABEL, TYPE, ID }, null, null, null, null, null);

        // creates the list adapter
        final BookmarksAdapter favAdapter = new BookmarksAdapter(this, c);
        // set up the list view
        final ListView list = (ListView) findViewById(R.id.bookmark_list);
        list.setAdapter(favAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {

                Toast.makeText(BookmarksActivity.this,
                        list.getAdapter().getItem(position).toString(), 5000).show();
            }
        });
    }
}
