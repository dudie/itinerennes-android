package fr.itinerennes.business.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.BikeStationColumns;
import fr.itinerennes.database.Columns.StationColumns;
import fr.itinerennes.model.BikeStation;
import fr.itinerennes.utils.DateUtils;

/**
 * Handles save / update / load / delete for {@link BikeStation}.
 * 
 * @author Jérémie Huchet
 */
public class BikeStationCacheEntryHandler implements CacheEntryHandler<BikeStation>,
        BikeStationColumns {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(BikeStationCacheEntryHandler.class);

    /** The database table name : {@value BikeStationCacheEntryHandler#BIKE_STATION_TABLE_NAME} . */
    private static final String BIKE_STATION_TABLE_NAME = "bike_stations";

    /** The SQL where clause to select on {@link StationColumns#ID} : {@value #WHERE_CLAUSE_ID}. */
    private static final String WHERE_CLAUSE_ID = String.format("%s = ? ", ID);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#replace(java.lang.String,
     *      java.lang.String, java.lang.Object, android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public final void replace(final String type, final String id, final BikeStation station,
            final SQLiteDatabase database) {

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("replace {}", station.toString());
        }

        final ContentValues values = new ContentValues(11);
        values.put(ID, id);
        values.put(NAME, station.getName());
        values.put(LONGITUDE, station.getLongitude());
        values.put(LATITUDE, station.getLatitude());
        values.put(STREET_NAME, station.getAddress());
        values.put(IS_ACTIVE, station.isActive());
        values.put(AVAILABLE_BIKES, station.getAvailableBikes());
        values.put(AVAILABLE_SLOTS, station.getAvailableSlots());
        values.put(IS_POS, station.isPos());
        values.put(DISTRICT_NAME, station.getDistrict());
        values.put(LAST_UPDATE, DateUtils.toSeconds(station.getLastUpdate()));
        final long rowId = database.replace(BIKE_STATION_TABLE_NAME, null, values);
        if (-1 == rowId) {
            LOGGER.error("station was not successfully replaced : {}", station.toString());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#delete(java.lang.String,
     *      java.lang.String, android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public final void delete(final String type, final String id, final SQLiteDatabase database) {

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("delete - type={}, identifier={}", type, id);
        }
        final int delCount = database.delete(BIKE_STATION_TABLE_NAME, WHERE_CLAUSE_ID,
                new String[] { id });
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#load(java.lang.String, java.lang.String,
     *      android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public final BikeStation load(final String type, final String id, final SQLiteDatabase database) {

        final String[] columns = new String[] { ID, NAME, LONGITUDE, LATITUDE, AVAILABLE_BIKES,
                AVAILABLE_SLOTS, DISTRICT_NAME, IS_ACTIVE, IS_POS, LAST_UPDATE, STREET_NAME };
        final String[] selectionArgs = new String[] { id };
        final Cursor c = database.query(BIKE_STATION_TABLE_NAME, columns, WHERE_CLAUSE_ID,
                selectionArgs, null, null, null);

        final BikeStation station;
        if (c.moveToFirst()) {
            station = new BikeStation();
            station.setId(c.getString(0));
            station.setName(c.getString(1));
            station.setLongitude(c.getInt(2));
            station.setLatitude(c.getInt(3));
            station.setAvailableBikes(c.getInt(4));
            station.setAvailableSlots(c.getInt(5));
            station.setDistrict(c.getString(6));
            station.setActive(Boolean.valueOf(c.getString(7)));
            station.setPos(Boolean.valueOf(c.getString(8)));
            station.setLastUpdate(new Date(c.getInt(9)));
            station.setAddress(c.getString(10));
        } else {
            station = null;
        }
        c.close();

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("load - {}", station.toString());
        }
        return station;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#load(java.lang.String,
     *      org.andnav.osm.util.BoundingBoxE6, SQLiteDatabase)
     */
    @Override
    public final List<BikeStation> load(final String type, final BoundingBoxE6 bbox, SQLiteDatabase database) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start - type={}, bbox={}", type, bbox.toString());
        }

        final String[] columns = new String[] { ID, NAME, LONGITUDE, LATITUDE, AVAILABLE_BIKES,
                AVAILABLE_SLOTS, DISTRICT_NAME, IS_ACTIVE, IS_POS, LAST_UPDATE, STREET_NAME };
        final String selection = String.format("%s >= ? AND %s <= ? AND %s >= ? AND %s <= ?",
                LONGITUDE, LONGITUDE, LATITUDE, LATITUDE);
        final String[] selectionArgs = new String[] { String.valueOf(bbox.getLonWestE6()),
                String.valueOf(bbox.getLonEastE6()), String.valueOf(bbox.getLatSouthE6()),
                String.valueOf(bbox.getLatNorthE6()) };
        final Cursor c = database.query(BIKE_STATION_TABLE_NAME, columns, selection, selectionArgs,
                null, null, null);

        final List<BikeStation> listStations = new ArrayList<BikeStation>();
        while (c.moveToNext()) {
            final BikeStation station = new BikeStation();
            station.setId(c.getString(0));
            station.setName(c.getString(1));
            station.setLongitude(c.getInt(2));
            station.setLatitude(c.getInt(3));
            station.setAvailableBikes(c.getInt(4));
            station.setAvailableSlots(c.getInt(5));
            station.setDistrict(c.getString(6));
            station.setActive(Boolean.valueOf(c.getString(7)));
            station.setPos(Boolean.valueOf(c.getString(8)));
            station.setLastUpdate(new Date(c.getInt(9)));
            station.setAddress(c.getString(10));
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
     * @see fr.itinerennes.business.cache.CacheEntryHandler#getHandledClass()
     */
    @Override
    public final Class<BikeStation> getHandledClass() {

        return BikeStation.class;
    }

}
