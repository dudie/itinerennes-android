package fr.itinerennes.business.cache;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.GeoExploreColumns;
import fr.itinerennes.utils.DateUtils;

/**
 * This class can save metadata about areas. You tell it which areas you have explored, and when you
 * query it about an area, it can tell you if you have already explored it.
 * 
 * <pre>
 *  _______________________________________
 * |A                                      |
 * |          ______________               |
 * |    _____|C             |______________|_______________
 * |   |B    |              |                              |
 * |   |     |              |                              |
 * |   |     |______________|                              |
 * |   |                                                   |
 * |   |                                                   |
 * |   |                                                   |
 * |   |                                                   |
 * |   |___________________________________________________|
 * |_______________________________________|
 * </pre>
 * 
 * <dt>Example 1 :</dt>
 * <dl>
 * I've explored <code>A</code> and <code>B</code> and i've notified {@link GeoCacheProvider}. If
 * I ask it if I've already explored <code>C</code> it will tell me yes.
 * </dl>
 * <dt>Example 2 :</dt>
 * <dl>
 * I've explored <code>B</code> and <code>C</code> and i've notified {@link GeoCacheProvider}. If
 * I ask it if I've already explored <code>A</code> it will tell me NO.
 * </dl>
 * 
 * @author Jérémie Huchet
 */
/**
 * @author kops
 */
public final class GeoCacheProvider implements GeoExploreColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(GeoCacheProvider.class);

    /** SQL selection to retrieve bounding box containing the given one. */
    private static final String CONTAINS_BBOX_W_N_E_S_T = String.format(
            "%s <= ? AND %s >= ? AND %s >= ? AND %s <= ? AND %s = ?", LON_WEST, LAT_NORTH,
            LON_EAST, LAT_SOUTH, TYPE);

    /** SQL selection to retrieve bounding box contained by the given one. */
    private static final String CONTAINED_BBOX_W_N_E_S_T = String.format(
            "%s >= ? AND %s <= ? AND %s <= ? AND %s >= ? AND %s = ?", LON_WEST, LAT_NORTH,
            LON_EAST, LAT_SOUTH, TYPE);

    /** SQL selection to retrieve bounding box by <code>_id</code>. */
    private static final String WHERE_ID_EQUALS = String.format("%s = ", _ID);

    /** The geo cache table name. */
    public static final String TABLE_NAME = "geo_explore";

    /** The time to live of an explored bounding box. */
    private static final int TTL = 1000;

    /** The unique instance of the geo cache. */
    private static GeoCacheProvider instance;

    /** The database. */
    private final SQLiteDatabase database;

    /**
     * Creates the geo cache.
     * 
     * @param database
     *            the database
     */
    private GeoCacheProvider(final SQLiteDatabase database) {

        this.database = database;
    }

    /**
     * Returns the unique instance of the geo cache, may create it if it does not exists.
     * <p>
     * If you provide a null database, then a mock will be created and used until
     * 
     * @param database
     *            the database, used at the first call of this method to create the singleton
     * @return the {@link GeoCacheProvider};
     */
    public static GeoCacheProvider getInstance(final SQLiteDatabase database) {

        if (instance == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("initializing geo cache provider");
            }
            instance = new GeoCacheProvider(database);
        }
        return instance;
    }

    /**
     * Tells the geo cache that the given bounding box has been explored.
     * <p>
     * It will cause the geo cache to check it has not been explored this area and delete explored
     * areas which are located into it.
     * 
     * @param bbox
     *            the explored bounding box
     * @param type
     *            the type of data explored
     */
    public synchronized void markExplored(final BoundingBoxE6 bbox, final String type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("markExplored.start - bbox={}, type={}", bbox.toString(), type);
        }
        // mark the bouding box explored only if it has not already been
        // if it has already been explored, the last update date will remains the old one
        if (!isExplored(bbox, type)) {

            /*
             * before marking an area explored, we must remove smaller areas included in it
             */
            // finds areas included in the explored boundingbox
            final String[] selectionArgs = new String[] { String.valueOf(bbox.getLonWestE6()),
                    String.valueOf(bbox.getLatNorthE6()), String.valueOf(bbox.getLonEastE6()),
                    String.valueOf(bbox.getLatSouthE6()), type };
            final Cursor c = database.query(TABLE_NAME, new String[] { _ID },
                    CONTAINED_BBOX_W_N_E_S_T, selectionArgs, null, null, null);
            // prepare a query to delete them
            final StringBuilder where = new StringBuilder();
            final String[] idsToDelete = new String[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                if (!c.isFirst()) {
                    where.append(" OR ");
                }
                where.append(WHERE_ID_EQUALS);
                idsToDelete[i] = c.getString(0);
                i++;
            }
            c.close();
            if (idsToDelete.length > 0) {
                database.delete(TABLE_NAME, where.toString(), idsToDelete);
            }

            // once included areas are deleted, insert the bigger and new one
            final ContentValues values = new ContentValues(5);
            values.put(LAST_UPDATE, System.currentTimeMillis());
            values.put(LON_WEST, bbox.getLonWestE6());
            values.put(LAT_NORTH, bbox.getLatNorthE6());
            values.put(LON_EAST, bbox.getLonEastE6());
            values.put(LAT_SOUTH, bbox.getLatSouthE6());

            database.insert(TABLE_NAME, null, values);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("markExplored.end");
        }
    }

    /**
     * Ask the geo cache if the given bounding box have already been explored.
     * <p>
     * If the bouding box is explored, checks that the TTL has not expired. When the TTL is expired,
     * the whole previously explored bounding box is removed.
     * 
     * @param bbox
     *            the bounding box where you search the data
     * @param type
     *            the type of searched data
     * @return true if the bounding box have already been explored for the given type of data
     */
    public synchronized boolean isExplored(final BoundingBoxE6 bbox, final String type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isExplored.start - bbox={}, type={}", bbox.toString(), type);
        }

        final String[] selectionArgs = new String[] { String.valueOf(bbox.getLonWestE6()),
                String.valueOf(bbox.getLatNorthE6()), String.valueOf(bbox.getLonEastE6()),
                String.valueOf(bbox.getLatSouthE6()), type };
        final Cursor c = database.query(TABLE_NAME, new String[] { _ID, LAST_UPDATE },
                CONTAINS_BBOX_W_N_E_S_T, selectionArgs, null, null, null);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("bbox[{}] has been explored {} times", bbox.toString(), c.getCount());
        }

        // returned bounding boxes have already been explored
        // check if they're not outdated
        boolean isExplored = false;
        while (c.moveToNext()) {

            if (TTL < DateUtils.currentTimeSeconds() - c.getInt(1)) {
                // ttl expired, remove this bbox from the cache
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("data for bbox[_id={}] is expired, removing it", c.getInt(0));
                }
                database.delete(TABLE_NAME, WHERE_ID_EQUALS, new String[] { c.getString(0) });

            } else if (!isExplored) {
                isExplored = true;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("bbox[{}] has been explored and data is up to date",
                            bbox.toString());
                }
            }
        }
        c.close();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isExplored.start - result={}", isExplored);
        }
        return isExplored;
    }
}
