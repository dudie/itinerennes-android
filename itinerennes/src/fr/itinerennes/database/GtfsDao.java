package fr.itinerennes.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteQuery;

import fr.dudie.onebusaway.model.Route;
import fr.dudie.onebusaway.model.ScheduleStopTime;
import fr.dudie.onebusaway.model.Stop;
import fr.itinerennes.database.Columns.RoutesStopsColumns;
import fr.itinerennes.database.exception.DatabaseAccessException;
import fr.itinerennes.database.query.ScheduleForStop;

/**
 * A DAO to access GTFS data loaded in database.
 * 
 * @author Jérémie Huchet
 */
public final class GtfsDao implements RoutesStopsColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GtfsDao.class);

    /** The context. */
    private final Context context;

    /** The database helper. */
    private final DatabaseHelper dbHelper;

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     * @param databaseHelper
     *            the itinerennes context
     */
    public GtfsDao(final Context context, final DatabaseHelper databaseHelper) {

        this.context = context;
        dbHelper = databaseHelper;
    }

    /**
     * Gets the list of the identifiers of the routes stopping at the given stop.
     * 
     * @param stopAndAgencyId
     *            a stop identifier with its agency identifier (e.g. 1_2345)
     * @return a list of route identifiers with the agency id (e.g. 1_6789)
     */
    public Cursor getRoutesForStop(final String stopAndAgencyId) {

        LOGGER.debug("getRoutesForStop.start - stopId={}", stopAndAgencyId);

        final String sel = String.format("%s = ?", STOP_ID);
        final String[] args = new String[] { stopAndAgencyId };
        final String[] cols = new String[] { ROUTE_ID };

        final Cursor c = dbHelper.getReadableDatabase().query(ROUTES_STOPS_TABLE_NAME, cols, sel,
                args, null, null, null);

        LOGGER.debug("getRoutesForStop.end");
        return c;
    }

    /**
     * Gets the list of the identifiers of the stops the given stop will stop at.
     * 
     * @param routeAndAgencyId
     *            a route identifier with its agency identifier (e.g. 1_6789)
     * @return a list of stop identifiers with the agency id (e.g. 1_2345)
     */
    public Cursor getStopsForRoute(final String routeAndAgencyId) {

        LOGGER.debug("getStopsForRoute.start - routeId={}", routeAndAgencyId);

        final String sel = String.format("%s = ?", ROUTE_ID);
        final String[] args = new String[] { routeAndAgencyId };
        final String[] cols = new String[] { STOP_ID };

        final Cursor c = dbHelper.getReadableDatabase().query(ROUTES_STOPS_TABLE_NAME, cols, sel,
                args, null, null, null);

        LOGGER.debug("getStopsForRoute.end");
        return c;
    }
    
    public Stop getStop(final String stopId) {

        final String[] columns = new String[] { "stop_id", "stop_code", "stop_name", "stop_lat", "stop_lon" };
        final Cursor c = dbHelper.getReadableDatabase().query("stops", columns, "stop_id = ?", new String[] { stopId }, null, null, null);

        final Stop stop;
        if (c.moveToFirst()) {
            stop = new Stop();
            stop.setId(c.getString(0)); // stop_id
            stop.setCode(c.getInt(1)); // stop_code
            stop.setName(c.getString(2)); // stop_name
            stop.setLat(c.getInt(3) / 1E6); // stop_lat
            stop.setLon(c.getInt(4) / 1E6); // stop_lon
            stop.setDirection(null); //
        } else {
            stop = null;
        }
        
        c.close();

        return stop;
    }

    public List<ScheduleStopTime> getStopTimes(final String stopId, final Date date) throws DatabaseAccessException {
        final ScheduleForStop query = new ScheduleForStop();
        query.setDate(date);
        query.setStopId(stopId);
        return query.execute(dbHelper.getReadableDatabase());
    }
}
