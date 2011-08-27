package fr.itinerennes.ui.activity;

import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.R;
import fr.itinerennes.business.service.BookmarkService;
import fr.itinerennes.database.MarkerDao;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Bookmark;
import fr.itinerennes.ui.adapter.BookmarksAdapter;
import fr.itinerennes.ui.views.overlays.MarkerOverlayItem;

/**
 * This activity displays bookmarked items the user starred.
 * 
 * @author Jérémie Huchet
 */
public class BookmarksActivity extends ItineRennesActivity {

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
        setContentView(R.layout.act_bookmarks);

        final BookmarkService bookmarksService = getApplicationContext().getBookmarksService();
        final List<Bookmark> allBookmarks = bookmarksService.getAllBookmarks();

        // creates the list adapter
        final BookmarksAdapter favAdapter = new BookmarksAdapter(this, allBookmarks);
        // set up the list view
        final ListView list = (ListView) findViewById(R.id.bookmark_list);
        list.setAdapter(favAdapter);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {

                final Bookmark bm = favAdapter.getItem(position);
                final String favType = bm.getType();
                final String favId = bm.getId();
                final String favLabel = bm.getLabel();

                try {
                    final MarkerOverlayItem item = findBookmarkedMarkerItem(favType, favId);

                    BookmarksActivity.this.startActivity(MapActivity.IntentFactory
                            .getOpenMapBoxIntent(getApplicationContext(), item, 17));
                } catch (final GenericException e) {
                    // bookmark is not found, remove it
                    bookmarksService.setNotStarred(favType, favId);
                    Toast.makeText(BookmarksActivity.this,
                            getString(R.string.delete_bookmark_not_found, favLabel), 5000).show();
                    onCreate(null);
                }

            }
        });
    }

    /**
     * Finds the MarkerOverlayItem of a bookmark using the resource type and identifier.
     * 
     * @param type
     *            the type of the resource
     * @param id
     *            the identifier of the resource
     * @return a {@link MarkerOverlayItem}
     * @throws GenericException
     *             the bookmarked item may be not found
     */
    private MarkerOverlayItem findBookmarkedMarkerItem(final String type, final String id)
            throws GenericException {

        MarkerOverlayItem item = null;

        final MarkerDao markerDao = getApplicationContext().getMarkerDao();
        final Cursor c = markerDao.getMarker(id, type);
        if (c != null && c.moveToFirst()) {
            item = markerDao.getMarkerOverlayItem(c);

            c.close();
        }

        if (item == null) {
            throw new GenericException(ErrorCodeConstants.BOOKMARK_NOT_FOUND, String.format(
                    "bookmark location not found using type=%s and id=%s", type, id));
        }
        return item;
    }
}
