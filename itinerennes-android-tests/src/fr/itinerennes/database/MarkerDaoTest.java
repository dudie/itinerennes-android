package fr.itinerennes.database;

/*
 * [license]
 * Instrumentation tests
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.util.ArrayList;
import java.util.HashMap;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.startup.DatabaseLoaderListener;

/**
 * Test class for {@link MarkerDao}.
 * 
 * @author Olivier Boudet
 */
public class MarkerDaoTest extends AndroidTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerDaoTest.class);

    /** The database helper. */
    private DatabaseHelper dbHelper;

    /** The marker dao. */
    private MarkerDao markerDao;

    /**
     * {@inheritDoc}
     * 
     * @see android.test.AndroidTestCase#setUp()
     */
    @Override
    protected final void setUp() throws Exception {

        super.setUp();

        final ItineRennesApplication appCtx = (ItineRennesApplication) getContext()
                .getApplicationContext();

        dbHelper = DatabaseHelper_.getInstance_(appCtx);

        // load data if necessary
        final DatabaseLoaderListener loader = new DatabaseLoaderListener(dbHelper, null,
                CSVDataReader.markers(appCtx));
        loader.execute();

        markerDao = MarkerDao_.getInstance_(appCtx);
        markerDao.dbHelper = dbHelper;
    }

    /**
     * Test method for {@link MarkerDao#getMarker(String)}.
     */
    public final void testGetMarkerByAndroidId() {

        LOGGER.info("testGetMarkerByAndroidId.start");

        // retrieve a marker from database (on 2012-01-28, 2_1024 means "Republique Nemours")
        final ItineRennesApplication appCtx = (ItineRennesApplication) getContext()
                .getApplicationContext();
        final SQLiteDatabase db = dbHelper.getReadableDatabase();
        final Cursor expected = db.query(MarkersColumns.MARKERS_TABLE_NAME, new String[] {
                MarkersColumns._ID, MarkersColumns.LABEL, MarkersColumns.LATITUDE,
                MarkersColumns.LONGITUDE, MarkersColumns.TYPE },
                String.format("%s = ?", MarkersColumns.ID), new String[] { "2_1024" }, null, null,
                null);

        assertTrue("cursor should have one result if database was correctly loaded",
                expected.moveToFirst());

        // query this marker using our DAO
        final String androidId = expected.getString(expected.getColumnIndex(MarkersColumns._ID));
        final Cursor c = markerDao.getMarker(androidId);

        assertNotNull("Cursor shouldn't be null", c);
        assertEquals("Ensure only one result has been fectched", 1, c.getCount());
        assertEquals("Check expected label", "République Nemours",
                c.getString(c.getColumnIndex(MarkersColumns.LABEL)));
        assertEquals(-1680212, c.getInt(c.getColumnIndex(MarkersColumns.LONGITUDE)));
        assertEquals(48109979, c.getInt(c.getColumnIndex(MarkersColumns.LATITUDE)));
        assertEquals(TypeConstants.TYPE_BUS, c.getString(c.getColumnIndex(MarkersColumns.TYPE)));

        LOGGER.info("testGetMarkerByAndroidId.end");
    }

    /**
     * Test method for {@link MarkerDao#getMarker(String, String)}.
     */
    public final void testGetMarkerByType() {

        LOGGER.info("testGetMarkerByType.start");

        final Cursor c = markerDao.getMarker("2_1016", TypeConstants.TYPE_BUS);

        assertNotNull("Cursor is null.", c);
        assertEquals("More than one result fetched.", 1, c.getCount());
        assertEquals("République Nemours",
                c.getString(c.getColumnIndex(Columns.MarkersColumns.LABEL)));
        assertEquals(-1680167, c.getInt(c.getColumnIndex(Columns.MarkersColumns.LONGITUDE)));
        assertEquals(48110073, c.getInt(c.getColumnIndex(Columns.MarkersColumns.LATITUDE)));
        assertEquals(TypeConstants.TYPE_BUS,
                c.getString(c.getColumnIndex(Columns.MarkersColumns.TYPE)));

        c.close();

        LOGGER.info("testGetMarkerByType.end");
    }

    /**
     * Test method for {@link MarkerDao#getMarkers(org.osmdroid.util.BoundingBoxE6, java.util.List)}
     * .
     */
    public final void testGetMarkers() {

        LOGGER.info("testGetMarkers.start");

        final BoundingBoxE6 bbox = new BoundingBoxE6(48110000, -1680000, 48100000, -1681000);
        final ArrayList<String> visibleLayers = new ArrayList<String>();
        visibleLayers.add(TypeConstants.TYPE_BUS);
        visibleLayers.add(TypeConstants.TYPE_SUBWAY);

        Cursor c = markerDao.getMarkers(bbox, visibleLayers);

        assertNotNull("Cursor is null.", c);
        assertEquals("bad number of fetched results.", 4, c.getCount());

        visibleLayers.add(TypeConstants.TYPE_BIKE);

        c = markerDao.getMarkers(bbox, visibleLayers);

        assertNotNull("Cursor is null.", c);
        assertEquals("bad number of fetched results.", 5, c.getCount());

        c.close();

        LOGGER.info("testGetMarkers.end");
    }

    /**
     * Test method for {@link MarkerDao#getMarkers(String, String)} .
     */
    public final void testGetMarkersWithTextFilter() {

        LOGGER.info("testGetMarkersWithTextFilter.start");

        final HashMap<String, Integer> values = new HashMap<String, Integer>();
        values.put("Charles de Gaulle", 1);
        values.put("ga", 6);
        values.put("mel", 1);
        values.put("", 83);

        for (final String value : values.keySet()) {
            final Cursor c = markerDao.getMarkers(TypeConstants.TYPE_BIKE, value, null);
            assertNotNull("Cursor is null.", c);
            assertEquals(String.format("bad number of fetched results for filter %s.", value),
                    (int) values.get(value), c.getCount());
            c.close();
        }

        LOGGER.info("testGetMarkersWithTextFilter.end");
    }
}
