package fr.itinerennes.business.service;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.model.Marker;

/**
 * Fetch markers from the database.
 * 
 * @author Olivier Boudet
 */
public class MarkersService extends AbstractService implements MarkersColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkersService.class);

    /**
     * Constructor.
     * 
     * @param dbHelper
     *            The Database Helper
     */
    public MarkersService(final DatabaseHelper dbHelper) {

        super(dbHelper);
    }

    /**
     * Fetch markers contained in the given bounding box from the database.
     * 
     * @param bbox
     *            the bounding box in which fetch markers
     * @param type
     *            the type of markers to retrieve
     * @return the list of Marker
     */
    public final List<Marker> getMarkers(final BoundingBoxE6 bbox, final String type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarkers.start - bbox={}", bbox);
        }
        final SQLiteDatabase database = dbHelper.getReadableDatabase();

        final String[] columns = new String[] { ID, TYPE, LABEL, LONGITUDE, LATITUDE };

        final String selection = String.format(
                "%s = ? AND %s >= ? AND %s <= ? AND %s >= ? AND %s <= ?", TYPE, LONGITUDE,
                LONGITUDE, LATITUDE, LATITUDE);

        final String[] selectionArgs = new String[] { type, String.valueOf(bbox.getLonWestE6()),
                String.valueOf(bbox.getLonEastE6()), String.valueOf(bbox.getLatSouthE6()),
                String.valueOf(bbox.getLatNorthE6()) };

        final Cursor c = database.query(MARKERS_TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        final List<Marker> markers = new ArrayList<Marker>();
        while (c.moveToNext()) {
            final Marker marker = new Marker();
            marker.setId(c.getString(0));
            marker.setType(c.getString(1));
            marker.setName(c.getString(2));
            marker.setLongitude(c.getInt(3));
            marker.setLatitude(c.getInt(4));

            markers.add(marker);
        }

        c.close();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarkers.end - markers={}", markers);
        }
        return markers;
    }

    /**
     * Fetch a single marker from the database.
     * 
     * @param id
     *            if of the marker
     * @return the Marker
     */
    public final Marker getMarker(final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarker.start - id={}", id);
        }
        final SQLiteDatabase database = dbHelper.getReadableDatabase();

        final String[] columns = new String[] { ID, TYPE, LABEL, LONGITUDE, LATITUDE };

        final String selection = String.format("%s = ?", ID);

        final String[] selectionArgs = new String[] { id };

        final Cursor c = database.query(MARKERS_TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        Marker marker = null;
        if (c.moveToNext()) {
            marker = new Marker();
            marker.setId(c.getString(0));
            marker.setType(c.getString(1));
            marker.setName(c.getString(2));
            marker.setLongitude(c.getInt(3));
            marker.setLatitude(c.getInt(4));
        }
        c.close();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getMarkers.end - marker={}", marker);
        }
        return marker;
    }
}
