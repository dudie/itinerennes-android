package fr.itinerennes.ui.activity;

import org.osmdroid.util.GeoPoint;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.R;
import fr.itinerennes.database.Columns.BookmarksColumns;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BikeStation;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.model.SubwayStation;
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
        final Cursor c = getDatabaseHelper().getWritableDatabase().query(BOOKMARKS_TABLE_NAME,
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

                final SQLiteCursor c = (SQLiteCursor) list.getAdapter().getItem(position);
                final String favType = c.getString(2);
                final String favId = c.getString(3);
                final String favLabel = c.getString(1);

                try {
                    final GeoPoint location = findBookmarkLocation(favType, favId);
                    final Intent i = new Intent(BookmarksActivity.this, MapActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.putExtra(MapActivity.INTENT_SELECT_BOOKMARK_ID, favId);
                    i.putExtra(MapActivity.INTENT_SELECT_BOOKMARK_TYPE, favType);
                    i.putExtra(MapActivity.INTENT_SET_MAP_ZOOM, 17);
                    i.putExtra(MapActivity.INTENT_SET_MAP_LON, location.getLongitudeE6());
                    i.putExtra(MapActivity.INTENT_SET_MAP_LAT, location.getLatitudeE6());
                    BookmarksActivity.this.startActivity(i);
                } catch (final GenericException e) {
                    // bookmark is not found, remove it
                    Toast.makeText(BookmarksActivity.this,
                            getString(R.string.delete_bookmark_not_found, favLabel), 5000).show();
                    getBookmarksService().setNotStarred(favType, favId);
                    list.invalidate();
                }

            }
        });
    }

    /**
     * Finds the location of a bookmark using the resource type and identifier.
     * 
     * @param type
     *            the type of the resource
     * @param id
     *            the identifier of the resource
     * @return a geopoint
     * @throws GenericException
     *             the bookmark may be not found
     */
    private GeoPoint findBookmarkLocation(final String type, final String id)
            throws GenericException {

        GeoPoint location = null;
        if (BusStation.class.getName().equals(type)) {
            final BusStation bus = getBusService().getStation(id);
            if (bus != null) {
                location = bus.getGeoPoint();
            }
        } else if (BikeStation.class.getName().equals(type)) {
            final BikeStation bike = getBikeService().getStation(id);
            if (bike != null) {
                location = bike.getGeoPoint();
            }
        } else if (SubwayStation.class.getName().equals(type)) {
            final SubwayStation subway = getSubwayService().getStation(id);
            if (subway != null) {
                location = subway.getGeoPoint();
            }
        }

        if (location == null) {
            throw new GenericException(ErrorCodeConstants.BOOKMARK_NOT_FOUND, String.format(
                    "bookmark location not found using type=%s and id=%s", type, id));
        }
        return location;
    }
}
