package fr.itinerennes.business.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.StationColumns;
import fr.itinerennes.database.Columns.SubwayStationColumns;
import fr.itinerennes.model.SubwayStation;
import fr.itinerennes.utils.DateUtils;

/**
 * Handles save / update / load / delete for {@link SubwayStation}.
 * 
 * @author Jérémie Huchet
 */
public class SubwayStationCacheEntryHandler implements CacheEntryHandler<SubwayStation>,
        SubwayStationColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(SubwayStationCacheEntryHandler.class);

    /** The database table name : {@value SubwayStationCacheEntryHandler#BIKE_STATION_TABLE_NAME} . */
    private static final String SUBWAY_STATION_TABLE_NAME = "subway_stations";

    /** The SQL where clause to select on {@link StationColumns#ID} : {@value #WHERE_CLAUSE_ID}. */
    private static final String WHERE_CLAUSE_ID = String.format("%s = ? ", ID);

    /** The database. */
    private SQLiteDatabase database = null;

    /**
     * Creates a subway station cache entry handler.
     * 
     * @param database
     *            the database
     */
    public SubwayStationCacheEntryHandler(final SQLiteDatabase database) {

        this.database = database;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#replace(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    @Override
    public final void replace(final String type, final String id, final SubwayStation station) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("replace.start - type={}, identifier={}", type, id);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("replace {}", station.toString());
            }
        }

        final ContentValues values = new ContentValues(11);
        values.put(ID, id);
        values.put(NAME, station.getName());
        values.put(LONGITUDE, station.getLongitude());
        values.put(LATITUDE, station.getLatitude());
        values.put(FLOORS, station.getFloors());
        values.put(RANK_PF_DIR_1, station.getRankingPlatformDirection1());
        values.put(RANK_PF_DIR_2, station.getRankingPlatformDirection2());
        values.put(HAS_PF_DIR_1, station.isHasPlatformDirection1());
        values.put(HAS_PF_DIR_2, station.isHasPlatformDirection2());
        values.put(LAST_UPDATE, DateUtils.toSeconds(station.getLastUpdate()));
        final long rowId = database.replace(SUBWAY_STATION_TABLE_NAME, null, values);
        if (-1 == rowId) {
            LOGGER.error("station was not successfully replaced : {}", station.toString());
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("replace.end");
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
        final int delCount = database.delete(SUBWAY_STATION_TABLE_NAME, WHERE_CLAUSE_ID,
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
    public final SubwayStation load(final String type, final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start - type={}, identifier={}", type, id);
        }

        final String[] columns = new String[] { ID, NAME, LONGITUDE, LATITUDE, FLOORS,
                RANK_PF_DIR_1, RANK_PF_DIR_2, HAS_PF_DIR_1, HAS_PF_DIR_2, LAST_UPDATE };
        final String[] selectionArgs = new String[] { id };
        final Cursor c = database.query(SUBWAY_STATION_TABLE_NAME, columns, WHERE_CLAUSE_ID,
                selectionArgs, null, null, null);

        final SubwayStation station;
        if (c.moveToFirst()) {
            station = new SubwayStation();
            station.setId(c.getString(0));
            station.setName(c.getString(1));
            station.setLongitude(c.getInt(2));
            station.setLatitude(c.getInt(3));
            station.setFloors(c.getInt(4));
            station.setRankingPlatformDirection1(c.getInt(5));
            station.setRankingPlatformDirection2(c.getInt(6));
            station.setHasPlatformDirection1(c.getInt(7) == 0 ? false : true);
            station.setHasPlatformDirection2(c.getInt(8) == 0 ? false : true);
            station.setLastUpdate(new Date(c.getInt(9)));
        } else {
            station = null;
        }
        c.close();

        if (LOGGER.isDebugEnabled()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("loaded {}", station.toString());
            }
            LOGGER.debug("load.end - resultNotNull={}", null != station);
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
    public final List<SubwayStation> load(final String type, final BoundingBoxE6 bbox) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start - type={}, bbox={}", type, bbox.toString());
        }
        final String[] columns = new String[] { ID, NAME, LONGITUDE, LATITUDE, FLOORS,
                RANK_PF_DIR_1, RANK_PF_DIR_2, HAS_PF_DIR_1, HAS_PF_DIR_2, LAST_UPDATE };
        final String selection = String.format("%s >= ? AND %s <= ? AND %s >= ? AND %s <= ?",
                LONGITUDE, LONGITUDE, LATITUDE, LATITUDE);
        final String[] selectionArgs = new String[] { String.valueOf(bbox.getLonWestE6()),
                String.valueOf(bbox.getLonEastE6()), String.valueOf(bbox.getLatSouthE6()),
                String.valueOf(bbox.getLatNorthE6()) };
        final Cursor c = database.query(SUBWAY_STATION_TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);

        final List<SubwayStation> listStations = new ArrayList<SubwayStation>();
        while (c.moveToNext()) {
            final SubwayStation station = new SubwayStation();
            station.setId(c.getString(0));
            station.setName(c.getString(1));
            station.setLongitude(c.getInt(2));
            station.setLatitude(c.getInt(3));
            station.setFloors(c.getInt(4));
            station.setRankingPlatformDirection1(c.getInt(5));
            station.setRankingPlatformDirection2(c.getInt(6));
            station.setHasPlatformDirection1(c.getInt(7) == 0 ? false : true);
            station.setHasPlatformDirection2(c.getInt(8) == 0 ? false : true);
            station.setLastUpdate(new Date(c.getInt(9)));
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
    public Class<SubwayStation> getHandledClass() {

        return SubwayStation.class;
    }

}
