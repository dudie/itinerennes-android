package fr.itinerennes.database;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.provider.BaseColumns;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.database.Columns.MarkersColumns;

/**
 * Fetch markers from the database.
 * 
 * @author Olivier Boudet
 */
public class MarkerDao implements MarkersColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerDao.class);

    /** The itinerennes context. */
    private final ItineRennesApplication context;

    /** The database helper. */
    private final DatabaseHelper dbHelper;

    /**
     * Constructor.
     * 
     * @param context
     *            the itinerennes context
     */
    public MarkerDao(final Context context) {

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

        final String[] columns = new String[] { String.format("m.%s", ID),
                String.format("m.%s", TYPE), String.format("m.%s", LABEL), LONGITUDE, LATITUDE,
                String.format("b.%s is not null  AS bookmarked", ID) };

        final StringBuilder selection = new StringBuilder();

        selection.append(String.format("%s >= ? AND %s <= ? AND %s >= ? AND %s <= ?", LONGITUDE,
                LONGITUDE, LATITUDE, LATITUDE));

        selection.append("AND ( ");
        // filter on visible types
        for (int i = 0; i < types.size(); i++) {
            selection.append(String.format(" %s m.%s = '%s'", (i > 0) ? "OR" : "", TYPE,
                    types.get(i)));
        }
        selection.append(")");

        final String[] selectionArgs = new String[] { String.valueOf(bbox.getLonWestE6()),
                String.valueOf(bbox.getLonEastE6()), String.valueOf(bbox.getLatSouthE6()),
                String.valueOf(bbox.getLatNorthE6()) };

        final Cursor c = query(selection.toString(), selectionArgs, columns, "m.TYPE ASC");

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

        final String[] columns = new String[] { String.format("m.%s", ID),
                String.format("m.%s", TYPE), String.format("m.%s", LABEL), LONGITUDE, LATITUDE,
                String.format("b.%s is not null  AS bookmarked", ID) };

        final String selection = String.format("m.%s = ?", BaseColumns._ID);

        final String[] selectionArgs = new String[] { id };

        final Cursor c = query(selection, selectionArgs, columns);

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

        final String[] columns = new String[] { String.format("m.%s", ID),
                String.format("m.%s", TYPE), String.format("m.%s", LABEL), LONGITUDE, LATITUDE,
                String.format("b.%s is not null AS bookmarked", ID) };

        final String selection = String.format("m.%s = ?", ID);

        final String[] selectionArgs = new String[] { id };

        final Cursor c = query(selection, selectionArgs, columns);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarkers.end - count={}", c.getCount());
        }

        return c;
    }

    /**
     * Search markers containing the given string.
     * 
     * @param query
     *            string to search in markers' label
     * @return search results
     */
    public final Cursor searchMarkers(final String query) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("searchMarkers.start - query={}", query);
        }

        final String selection = String.format("m.%s LIKE ?", Columns.MarkersColumns.LABEL);
        final String[] columns = new String[] { "m." + BaseColumns._ID,
                "m." + Columns.MarkersColumns.ID, "m." + Columns.MarkersColumns.TYPE,
                "m." + Columns.MarkersColumns.LABEL, Columns.MarkersColumns.LONGITUDE,
                Columns.MarkersColumns.LATITUDE };

        final String[] selectionArgs = new String[] { "%" + query + "%" };

        final Cursor c = query(selection, selectionArgs, columns);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("searchMarkers.end - query={}");
        }
        return c;
    }

    /**
     * Fetches suggestions for searches. All markers containing the query in the label will be
     * fetched.
     * 
     * @param query
     *            string to search in markers' label
     * @return search results
     */
    public final Cursor getSuggestions(final String query) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getSuggestions.start - query={}", query);
        }

        final String[] selectionArgs = new String[] { "%" + query + "%" };

        final StringBuffer sql = new StringBuffer();

        sql.append(String.format("SELECT 'B', %s,", BaseColumns._ID));

        // building select clause part for the suggest_icon_id column
        sql.append(String.format(" CASE WHEN %s = '%s' THEN '%s'", Columns.MarkersColumns.TYPE,
                TypeConstants.TYPE_BUS, R.drawable.ic_mapbox_bus));
        sql.append(String.format(" WHEN %s = '%s' THEN '%s'", Columns.MarkersColumns.TYPE,
                TypeConstants.TYPE_BIKE, R.drawable.ic_mapbox_bike));
        sql.append(String.format(" WHEN %s = '%s' THEN '%s'", Columns.MarkersColumns.TYPE,
                TypeConstants.TYPE_SUBWAY, R.drawable.ic_mapbox_subway));
        sql.append(String.format(" END AS %s,", SearchManager.SUGGEST_COLUMN_ICON_1));

        sql.append(String.format(" %s AS %s,", Columns.MarkersColumns.LABEL,
                SearchManager.SUGGEST_COLUMN_TEXT_1));
        sql.append(String.format(" %s AS %s", BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID));

        sql.append(String.format(" FROM %s", MARKERS_TABLE_NAME));

        sql.append(String.format(" WHERE %s LIKE ?", Columns.MarkersColumns.LABEL));

        sql.append(String.format(
                " UNION ALL select 'A', 'nominatim','%s' as %s,'%s','nominatim' as %s",
                R.drawable.ic_osm, SearchManager.SUGGEST_COLUMN_ICON_1, context.getResources()
                        .getString(R.string.search_address),
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID));

        sql.append(String.format(" ORDER BY 1,%s", Columns.MarkersColumns.LABEL));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Marker query : %s", sql));
        }

        final Cursor c = dbHelper.getReadableDatabase().rawQuery(sql.toString(), selectionArgs);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getSuggestions.end");
        }
        return c;
    }

    /**
     * Queries the database.
     * 
     * @param selection
     *            the where clause
     * @param selectionArgs
     *            parameters for the selection
     * @param columns
     *            columns to retrieve
     * @return results
     */
    private Cursor query(final String selection, final String[] selectionArgs,
            final String[] columns) {

        return query(selection, selectionArgs, columns, null);

    }

    /**
     * Queries the database.
     * 
     * @param selection
     *            the where clause
     * @param selectionArgs
     *            parameters for the selection
     * @param columns
     *            columns to retrieve
     * @param orderBy
     *            order by clause
     * @return results
     */
    private Cursor query(final String selection, final String[] selectionArgs,
            final String[] columns, final String orderBy) {

        final SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        builder.setTables(String.format("%s m left join %s b on m.%s=b.%s", MARKERS_TABLE_NAME,
                Columns.BookmarksColumns.BOOKMARKS_TABLE_NAME, ID, Columns.BookmarksColumns.ID));

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Marker query : %s",
                    builder.buildQuery(columns, selection, selectionArgs, null, null, null, null)));
        }

        final Cursor c = builder.query(dbHelper.getReadableDatabase(), columns, selection,
                selectionArgs, null, null, orderBy);

        if (c == null) {
            return null;
        } else if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        return c;

    }

}
