package fr.itinerennes.ui.activity;

import java.io.IOException;
import java.util.List;

import org.osmdroid.util.GeoPoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.keolis.model.BikeStation;
import fr.itinerennes.keolis.model.SubwayStation;
import fr.itinerennes.model.Bookmark;
import fr.itinerennes.ui.adapter.BookmarksAdapter;
import fr.itinerennes.ui.views.overlays.MarkerOverlayItem;

/**
 * This activity displays bookmarks items the user starred.
 * 
 * @author Jérémie Huchet
 */
public class BookmarksActivity extends ItinerennesContext {

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

        final List<Bookmark> allBookmarks = getBookmarksService().getAllBookmarks();

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
                    getBookmarksService().setNotStarred(favType, favId);
                    Toast.makeText(BookmarksActivity.this,
                            getString(R.string.delete_bookmark_not_found, favLabel), 5000).show();
                    list.invalidate();
                } catch (final IOException e) {
                    // station is not found, remove it
                    getBookmarksService().setNotStarred(favType, favId);
                    Toast.makeText(BookmarksActivity.this,
                            getString(R.string.delete_bookmark_not_found, favLabel), 5000).show();
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
     * @throws IOException
     *             station not found
     */
    private GeoPoint findBookmarkLocation(final String type, final String id)
            throws GenericException, IOException {

        GeoPoint location = null;
        if (TypeConstants.TYPE_BUS.equals(type)) {
            final MarkerOverlayItem bus = getMarkerService().getMarker(id);
            if (bus != null) {
                location = bus.getLocation();
            }
        } else if (TypeConstants.TYPE_BIKE.equals(type)) {
            final BikeStation bike = getBikeService().getBikeStation(id);
            if (bike != null) {

                location = new GeoPoint(bike.getLatitude(), bike.getLongitude());
            }
        } else if (TypeConstants.TYPE_SUBWAY.equals(type)) {
            final SubwayStation subway = getSubwayService().getSubwayStation(id);
            if (subway != null) {
                location = new GeoPoint(subway.getLatitude(), subway.getLongitude());
            }
        }

        if (location == null) {
            throw new GenericException(ErrorCodeConstants.BOOKMARK_NOT_FOUND, String.format(
                    "bookmark location not found using type=%s and id=%s", type, id));
        }
        return location;
    }
}
