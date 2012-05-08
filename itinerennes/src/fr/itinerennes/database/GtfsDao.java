package fr.itinerennes.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.database.Cursor;

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
}
