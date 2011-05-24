package fr.itinerennes.business.service;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.database.Columns;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.database.DatabaseHelper;

/**
 * Fetch markers from the database.
 * 
 * @author Olivier Boudet
 */
public class MarkerService implements MarkersColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerService.class);

    /** The itinerennes context. */
    private final ItineRennesApplication context;

    /** The database helper. */
    private final DatabaseHelper dbHelper;

    /**
     * Constructor.
     * 
     * @param context
     *            the itinerennes context
     * @param dbHelper
     *            The Database Helper
     */
    public MarkerService(final Context context) {

        this.context = (ItineRennesApplication) context;
        dbHelper = ((ItineRennesApplication) context).getDatabaseHelper();

    }

    /**
     * Fetch markers contained in the given bounding box from the database.
     * 
     * @param bbox
     *            the bounding box in which fetch markers
     * @param types
     *            the types of markers to retrieve
     * @return the list of Marker
     */
    public final Cursor getMarkers(final BoundingBoxE6 bbox, final List<String> types) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarkers.start - bbox={}", bbox);
        }

        if (types.size() == 0) {
            // no visible marker type is selected, we return nothing at all
            return null;
        }

        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(String.format("%s m left join %s b on m.%s=b.%s", MARKERS_TABLE_NAME,
                Columns.BookmarksColumns.BOOKMARKS_TABLE_NAME, ID, Columns.BookmarksColumns.ID));

        final String[] columns = new String[] { String.format("m.%s", ID),
                String.format("m.%s", TYPE), String.format("m.%s", LABEL), LONGITUDE, LATITUDE,
                String.format("b.%s is not null", ID) };

        final String where = String.format("%s >= ? AND %s <= ? AND %s >= ? AND %s <= ?",
                LONGITUDE, LONGITUDE, LATITUDE, LATITUDE);

        // filter on visible types
        for (int i = 0; i < types.size(); i++) {
            builder.appendWhere(String.format(" %s m.%s = '%s'", (i > 0) ? "OR" : "", TYPE,
                    types.get(i)));
        }

        final String[] selectionArgs = new String[] { String.valueOf(bbox.getLonWestE6()),
                String.valueOf(bbox.getLonEastE6()), String.valueOf(bbox.getLatSouthE6()),
                String.valueOf(bbox.getLatNorthE6()) };

        final Cursor c = builder.query(dbHelper.getReadableDatabase(), columns, where,
                selectionArgs, null, null, "m.TYPE ASC");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarkers.end - count={}", c.getCount());
        }
        return c;
    }

    /**
     * Fetch a single MarkerOverlayItem from the database based on an unique android id.
     * 
     * @param id
     *            unique id of the marker
     * @return the Marker
     */
    public final Cursor getMarker(final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarker.start - id={}", id);
        }

        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(String.format("%s m left join %s b on m.%s=b.%s", MARKERS_TABLE_NAME,
                Columns.BookmarksColumns.BOOKMARKS_TABLE_NAME, ID, Columns.BookmarksColumns.ID));

        final String[] columns = new String[] { String.format("m.%s", ID),
                String.format("m.%s", TYPE), String.format("m.%s", LABEL), LONGITUDE, LATITUDE,
                String.format("b.%s is not null", ID) };

        final String where = String.format("m.%s = ?", BaseColumns._ID);

        final String[] selectionArgs = new String[] { id };

        final Cursor c = builder.query(dbHelper.getReadableDatabase(), columns, where,
                selectionArgs, null, null, null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarkers.end - count={}", c.getCount());
        }
        return c;
    }

    /**
     * Fetch a single MarkerOverlayItem from the database based on a stop id and his type.
     * 
     * @param id
     *            id of the marker
     * @param type
     *            type of the marker
     * @return the Marker
     */
    public final Cursor getMarker(final String id, final String type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarker.start - id={}, type={}", id, type);
        }

        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(String.format("%s m left join %s b on m.%s=b.%s", MARKERS_TABLE_NAME,
                Columns.BookmarksColumns.BOOKMARKS_TABLE_NAME, ID, Columns.BookmarksColumns.ID));

        final String[] columns = new String[] { String.format("m.%s", ID),
                String.format("m.%s", TYPE), String.format("m.%s", LABEL), LONGITUDE, LATITUDE,
                String.format("b.%s is not null", ID) };

        final String where = String.format("m.%s = ?", ID);

        final String[] selectionArgs = new String[] { id };

        final Cursor c = builder.query(dbHelper.getReadableDatabase(), columns, where,
                selectionArgs, null, null, null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarkers.end - count={}", c.getCount());
        }
        return c;
    }

}
