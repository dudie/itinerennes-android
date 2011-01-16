package fr.itinerennes.business.cache;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import fr.itinerennes.database.Columns.GeoExploreColumns;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.utils.FileUtils;

/**
 * Test class for {@link GeoCacheProvider}.
 * 
 * @author Jérémie Huchet
 */
public class GeoCacheProviderTest extends AndroidTestCase implements GeoExploreColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(GeoCacheProviderTest.class);

    /** The type of cached data used for the test. */
    private static final String TYPE = "UNIT_TESTING";

    /** The SQL script file containing the test data. */
    private static final String TEST_DATA_SCRIPT = "GeoCacheProviderTest.sql";

    /** The writable database. */
    private SQLiteDatabase database;

    /** The tested geo cache provider. */
    private GeoCacheProvider geoCache;

    /**
     * {@inheritDoc}
     * 
     * @see android.test.AndroidTestCase#setUp()
     */
    @Override
    protected final void setUp() throws Exception {

        super.setUp();
        final DatabaseHelper dbHlpr = new DatabaseHelper(this.getContext());
        database = dbHlpr.getWritableDatabase();
        dropGeoCacheData();
        insertTestGeoCacheData();

        geoCache = GeoCacheProvider.getInstance(dbHlpr);
    }

    /**
     * Drops all data in the geo cache database.
     */
    private void dropGeoCacheData() {

        database.delete(GeoCacheProvider.TABLE_NAME, null, null);
    }

    /**
     * Inserts the test data in the geo cache database.
     */
    private void insertTestGeoCacheData() {

        final String[] statements = FileUtils
                .read(getClass().getResourceAsStream(TEST_DATA_SCRIPT))
                .replaceAll("[\r\n]*\\s*$", "").split(";");
        for (final String stmt : statements) {
            database.execSQL(stmt);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.test.AndroidTestCase#tearDown()
     */
    @Override
    protected final void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test method for {@link GeoCacheProvider#isExplored(BoundingBoxE6, String)}.
     * <p>
     * Checks the method detects already explored areas.
     */
    public final void testIsExploredForExploredArea() {

        LOGGER.info("testIsExploredForExploredArea.start");
        boolean result;
        BoundingBoxE6 bbox;

        // bbox is included in an explored area
        bbox = new BoundingBoxE6(6, 8, 5, 7);
        result = geoCache.isExplored(bbox, TYPE);
        assertTrue(result);

        // bbox is the same as an explored area
        bbox = new BoundingBoxE6(2, 3, 1, 2);
        result = geoCache.isExplored(bbox, TYPE);
        assertTrue(result);

        // bbox is on the inner edge of an explored area
        bbox = new BoundingBoxE6(5, 8, 4, 7);
        result = geoCache.isExplored(bbox, TYPE);
        assertTrue(result);

        // bbox is on the inner corner of an explored area
        bbox = new BoundingBoxE6(5, 8, 4, 6);
        result = geoCache.isExplored(bbox, TYPE);
        assertTrue(result);

        // bbox is on the inner corner of an explored area
        bbox = new BoundingBoxE6(5, 9, 4, 6);
        result = geoCache.isExplored(bbox, TYPE);
        assertTrue(result);

        LOGGER.info("testIsExploredForExploredArea.end");
    }

    /**
     * Test method for {@link GeoCacheProvider#isExplored(BoundingBoxE6, String)}.
     * <p>
     * Checks the method detects unexplored areas.
     */
    public final void testIsExploredForUnexploredArea() {

        LOGGER.info("testIsExploredForUnexploredArea.start");
        boolean result;
        BoundingBoxE6 bbox;

        // bbox isn't included in any explored area
        // N E S W
        bbox = new BoundingBoxE6(2, 8, 1, 7);
        result = geoCache.isExplored(bbox, TYPE);
        assertFalse(result);

        // bbox contains multiple explored areas
        bbox = new BoundingBoxE6(3, 6, 1, 2);
        result = geoCache.isExplored(bbox, TYPE);
        assertFalse(result);

        // bbox is across an explored area (horizontally)
        bbox = new BoundingBoxE6(6, 11, 5, 1);
        result = geoCache.isExplored(bbox, TYPE);
        assertFalse(result);

        // bbox is across an explored area (vertically)
        bbox = new BoundingBoxE6(8, 8, 2, 7);
        result = geoCache.isExplored(bbox, TYPE);
        assertFalse(result);

        // bbox is across the corner of an explored area
        bbox = new BoundingBoxE6(8, 7, 6, 5);
        result = geoCache.isExplored(bbox, TYPE);
        assertFalse(result);

        LOGGER.info("testIsExploredForUnexploredArea.end");
    }

    /**
     * Test methods for rounding values.
     */
    public final void testRound() {

        int rounded;

        // normalize minus
        for (int i = -19; i < -10; i++) {
            rounded = GeoCacheProvider.normalizeMinus(i);
            assertEquals(-20, rounded);
        }
        for (int i = -10; i < 0; i++) {
            rounded = GeoCacheProvider.normalizeMinus(i);
            assertEquals(-10, rounded);
        }
        for (int i = 0; i < 10; i++) {
            rounded = GeoCacheProvider.normalizeMinus(i);
            assertEquals(0, rounded);
        }
        for (int i = 10; i < 20; i++) {
            rounded = GeoCacheProvider.normalizeMinus(i);
            assertEquals(10, rounded);
        }

        // normalize plus
        for (int i = -19; i <= -10; i++) {
            rounded = GeoCacheProvider.normalizePlus(i);
            assertEquals(-10, rounded);
        }
        for (int i = -9; i <= 0; i++) {
            rounded = GeoCacheProvider.normalizePlus(i);
            assertEquals(0, rounded);
        }
        for (int i = 1; i <= 10; i++) {
            rounded = GeoCacheProvider.normalizePlus(i);
            assertEquals(10, rounded);
        }
        for (int i = 11; i <= 20; i++) {
            rounded = GeoCacheProvider.normalizePlus(i);
            assertEquals(20, rounded);
        }
    }

    /**
     * Test method for {@link GeoCacheProvider#normalize(BoundingBoxE6)}.
     */
    public final void testNormalize() {

        final BoundingBoxE6 bbox = new BoundingBoxE6(1425, 233, 1322, 148);
        final BoundingBoxE6 newBbox = GeoCacheProvider.normalize(bbox);
        assertEquals(1430, newBbox.getLatNorthE6());
        assertEquals(240, newBbox.getLonEastE6());
        assertEquals(1320, newBbox.getLatSouthE6());
        assertEquals(140, newBbox.getLonWestE6());
    }
}
