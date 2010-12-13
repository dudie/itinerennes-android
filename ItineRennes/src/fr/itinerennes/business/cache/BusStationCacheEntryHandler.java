package fr.itinerennes.business.cache;

import java.util.ArrayList;
import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import fr.itinerennes.beans.BusStation;
import fr.itinerennes.database.Columns.BusStationColumns;
import fr.itinerennes.database.Columns.StationColumns;

/**
 * Handles save / update / load / delete for {@link BusStation}.
 * 
 * @author Olivier Boudet
 */
public class BusStationCacheEntryHandler implements CacheEntryHandler<BusStation>,
        BusStationColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusStationCacheEntryHandler.class);

    /** The database table name : {@value BusStationCacheEntryHandler#BUS_STATION_TABLE_NAME} . */
    private static final String BUS_STATION_TABLE_NAME = "bus_stations";

    /** The SQL where clause to select on {@link StationColumns#ID} : {@value #WHERE_CLAUSE_ID}. */
    private static final String WHERE_CLAUSE_ID = String.format("%s = ? ", ID);

    /** The database. */
    private SQLiteDatabase database = null;

    public BusStationCacheEntryHandler(final SQLiteDatabase database) {

        this.database = database;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#replace(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    @Override
    public final void replace(final String type, final String id, final BusStation station) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("save.start - type={}, identifier={}", type, id);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("save {}", station.toString());
            }
        }

        final ContentValues values = new ContentValues(4);
        values.put(ID, id);
        values.put(NAME, station.getName());
        values.put(LONGITUDE, station.getLongitude());
        values.put(LATITUDE, station.getLatitude());

        final long rowId = database.replace(BUS_STATION_TABLE_NAME, null, values);
        if (-1 == rowId) {
            LOGGER.error("station was not successfully inserted : {}", station.toString());
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("save.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#update(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    @Override
    public final void update(final String type, final String id, final BusStation station) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("update.start -type={}, identifier={}", type, id);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("update {}", station.toString());
            }
        }
        final ContentValues values = new ContentValues(3);
        values.put(NAME, station.getName());
        values.put(LONGITUDE, station.getLongitude());
        values.put(LATITUDE, station.getLatitude());

        final int updCount = database.update(BUS_STATION_TABLE_NAME, values, WHERE_CLAUSE_ID,
                new String[] { id });
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("update.end - {} rows updated", updCount);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#delete(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public final void delete(final String type, final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("delete.start - type={}, identifier={}", type, id);
        }
        final int delCount = database.delete(BUS_STATION_TABLE_NAME, WHERE_CLAUSE_ID,
                new String[] { id });
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("delete.end - {} rows deleted", delCount);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#load(java.lang.String, java.lang.String)
     */
    @Override
    public final BusStation load(final String type, final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start - type={}, identifier={}", type, id);
        }

        final String[] columns = new String[] { ID, NAME, LONGITUDE, LATITUDE };
        final String[] selectionArgs = new String[] { id };
        final Cursor c = database.query(BUS_STATION_TABLE_NAME, columns, WHERE_CLAUSE_ID,
                selectionArgs, null, null, null);

        final BusStation station;
        if (c.moveToFirst()) {
            station = new BusStation();
            station.setId(c.getString(0));
            station.setName(c.getString(1));
            station.setLongitude(c.getInt(2));
            station.setLatitude(c.getInt(3));
        } else {
            station = null;
        }
        c.close();

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("load.end - result={}", station.toString());
        }
        return station;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#load(java.lang.String,
     *      org.andnav.osm.util.BoundingBoxE6)
     */
    @Override
    public final List<BusStation> load(final String type, final BoundingBoxE6 bbox) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start - type={}, bbox={}", type, bbox.toString());
        }
        final String[] columns = new String[] { ID, NAME, LONGITUDE, LATITUDE };
        final String selection = String.format("%s >= ? AND %s <= ? AND %s >= ? AND %s <= ?",
                LONGITUDE, LONGITUDE, LATITUDE, LATITUDE);
        final String[] selectionArgs = new String[] { String.valueOf(bbox.getLonWestE6()),
                String.valueOf(bbox.getLonEastE6()), String.valueOf(bbox.getLatSouthE6()),
                String.valueOf(bbox.getLatNorthE6()) };
        final Cursor c = database.query(BUS_STATION_TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        final List<BusStation> listStations = new ArrayList<BusStation>();
        while (c.moveToNext()) {
            final BusStation station = new BusStation();
            station.setId(c.getString(0));
            station.setName(c.getString(1));
            station.setLongitude(c.getInt(2));
            station.setLatitude(c.getInt(3));
            listStations.add(station);
        }
        c.close();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.end");
        }
        return listStations;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#getObjectClassName()
     */
    @Override
    public String getObjectClassName() {

        return BusStation.class.getName();
    }
}
