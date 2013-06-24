package fr.itinerennes.database;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.Cursor;

import fr.dudie.onebusaway.model.Route;
import fr.dudie.onebusaway.model.ScheduleStopTime;
import fr.dudie.onebusaway.model.Stop;
import fr.itinerennes.database.Columns.RoutesStopsColumns;

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

    public List<ScheduleStopTime> getStopTimes(final String stopId, final Date date) {

//        final ScheduleStopTime st = new ScheduleStopTime();
//        st.setArrivalTime(arrivalTime);
//        st.setDepartureTime(departureTime);
//        st.setHeadsign(headsign);
//        st.setRoute(this.getRoute(routeId));
//        st.setServiceId(serviceId);
//        st.setTripId(tripId);
        return null;
    }

    public Route getRoute(final String routeId) {
        final String[] columns = new String[] { "agency_id", "route_id", "route_short_name", "route_long_name", "route_desc", "route_type", "route_color",
                "route_text_color" };
        final Cursor c = dbHelper.getReadableDatabase().query("routes", columns, "route_id = ?", new String[] { routeId }, null, null, null);

        final Route r;
        if (c.moveToFirst()) {
            r = new Route();
            r.setAgencyId(c.getString(0));
            r.setId(c.getString(1));
            r.setShortName(c.getString(2));
            r.setLongName(c.getString(3));
            r.setDescription(c.getString(4));
            r.setType(c.getInt(5));
            r.setColor(c.getString(6));
            r.setTextColor(c.getString(7));
        } else {
            r = null;
        }
        c.close();
        return r;
    }
}
