package fr.itinerennes.business.cache;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.BusRouteColumns;
import fr.itinerennes.database.Columns.BusStationRouteColumns;
import fr.itinerennes.database.Columns.StationColumns;
import fr.itinerennes.model.BusRoute;
import fr.itinerennes.model.BusStation;

/**
 * Handles save / update / load / delete for {@link BusRoute}.
 * 
 * @author Olivier Boudet
 */
public class BusRouteCacheEntryHandler implements CacheRelationEntryHandler<BusRoute>,
        BusRouteColumns, BusStationRouteColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusRouteCacheEntryHandler.class);

    /** The database table name : {@value BusRouteCacheEntryHandler#BUS_ROUTE_TABLE_NAME} . */
    private static final String BUS_ROUTE_TABLE_NAME = "bus_routes";

    /**
     * The database table name for relations between {@link BusRoute} and {@link BusStation} :
     * {@value BusRouteCacheEntryHandler#BUS_STATION_ROUTE_TABLE_NAME} .
     */
    private static final String BUS_STATION_ROUTE_TABLE_NAME = "bus_routes_stations";

    /** The SQL where clause to select on {@link StationColumns#ID} : {@value #WHERE_CLAUSE_ID}. */
    private static final String WHERE_CLAUSE_ID = String.format("%s = ? ", STATION_ID);

    /** The database. */
    private SQLiteDatabase database = null;

    /**
     * Creates a bus station cache entry handler.
     * 
     * @param database
     *            the database
     */
    public BusRouteCacheEntryHandler(final SQLiteDatabase database) {

        this.database = database;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheRelationEntryHandler#replace(java.lang.String,
     *      java.lang.String, java.lang.Object, java.lang.String)
     */
    @Override
    public void replace(final String type, final String id, final BusRoute route,
            final String stationId) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("replace.start - type={}, identifier={}", type, id);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("replace {}", route.toString());
            }
        }

        ContentValues values = new ContentValues(4);
        values.put(ID, id);
        values.put(LONG_NAME, route.getLongName());
        values.put(SHORT_NAME, route.getShortName());
        values.put(AGENCY, route.getAgencyId());

        database.beginTransaction();
        try {
            database.replace(BUS_ROUTE_TABLE_NAME, null, values);

            values = new ContentValues(2);
            values.put(STATION_ID, stationId);
            values.put(ROUTE_ID, id);

            database.replace(BUS_STATION_ROUTE_TABLE_NAME, null, values);
            database.setTransactionSuccessful();
        } catch (final Exception e) {
            LOGGER.error("route was not successfully replaced : {}", route.toString());
        } finally {
            database.endTransaction();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("replace.end");
        }

    }

    @Override
    public void delete(final String type, final String id) {

        // TOBO Auto-generated method stub

    }

    @Override
    public Class<BusRoute> getHandledClass() {

        return BusRoute.class;
    }

    @Override
    public List<BusRoute> load(final String type, final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start - type={}, identifier={}", type, id);
        }

        List<BusRoute> routes = new ArrayList<BusRoute>();

        final String[] columns = new String[] { ID, SHORT_NAME, LONG_NAME, AGENCY };
        final String[] selectionArgs = new String[] { id };
        final Cursor c = database.query(BUS_STATION_ROUTE_TABLE_NAME + " INNER JOIN "
                + BUS_ROUTE_TABLE_NAME + " r ON route_id=r.id", columns, WHERE_CLAUSE_ID,
                selectionArgs, null, null, null);

        BusRoute route;
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                route = new BusRoute();
                route.setId(c.getString(0));
                route.setShortName(c.getString(1));
                route.setLongName(c.getString(2));
                route.setAgencyId(c.getString(3));

                routes.add(route);
                c.moveToNext();
            }
        } else {
            routes = null;
        }
        c.close();

        if (LOGGER.isDebugEnabled()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("loaded {} routes", routes != null ? routes.size() : 0);
            }
            LOGGER.debug("load.end - resultNotNull={}", null != routes);
        }
        return routes;
    }
}
