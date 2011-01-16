package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Activity;

import fr.itinerennes.business.service.BikeService;
import fr.itinerennes.business.service.BusDepartureService;
import fr.itinerennes.business.service.BusRouteService;
import fr.itinerennes.business.service.BusService;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.business.service.SubwayService;
import fr.itinerennes.database.DatabaseHelper;

/**
 * An abstract activity providing common functionalities such as automatic.
 * 
 * @author Jérémie Huchet
 */
public abstract class ITRContext extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(ITRContext.class);

    /** The database helper. */
    private static DatabaseHelper databaseHelper;

    /** The bus service. */
    private static BusService busService;

    /** The bike service. */
    private static BikeService bikeService;

    /** The subway service. */
    private static SubwayService subwayService;

    /** The bus route service. */
    private static BusRouteService busRouteService;

    /** The bus departure service. */
    private BusDepartureService busDepartureService;

    /** The line icon service. */
    private LineIconService lineIconService;

    /**
     * Close the database helper. If this method is derived, you must ensure to call
     * <code>super.onDestroy()</code>.
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }

    /**
     * Gets a reference to the database helper.
     * 
     * @return a reference to the database helper
     */
    public final DatabaseHelper getDatabaseHelper() {

        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(getBaseContext());
        }
        return databaseHelper;
    }

    /**
     * Gets a reference to the bus service.
     * 
     * @return a reference to the bus service
     */
    public final BusService getBusService() {

        if (busService == null) {
            busService = new BusService(getDatabaseHelper());
        }
        return busService;
    }

    /**
     * Gets a reference to the bike service.
     * 
     * @return a reference to the bike service
     */
    public final BikeService getBikeService() {

        if (bikeService == null) {
            bikeService = new BikeService(getDatabaseHelper());
        }
        return bikeService;
    }

    /**
     * Gets a reference to the subway service.
     * 
     * @return a reference to the subway service
     */
    public final SubwayService getSubwayService() {

        if (subwayService == null) {
            subwayService = new SubwayService(getDatabaseHelper());
        }
        return subwayService;
    }

    /**
     * Gets a reference to the bus route service.
     * 
     * @return a reference to the bus route service
     */
    public final BusRouteService getBusRouteService() {

        if (busRouteService == null) {
            busRouteService = new BusRouteService(getDatabaseHelper());
        }
        return busRouteService;
    }

    /**
     * Gets a reference to the bus route service.
     * 
     * @return a reference to the bus route service
     */
    public final BusDepartureService getBusDepartureService() {

        if (busDepartureService == null) {
            busDepartureService = new BusDepartureService(getDatabaseHelper());
        }
        return busDepartureService;
    }

    /**
     * Gets a reference to the line icon service.
     * 
     * @return a reference to the line icon service
     */
    public final LineIconService getLineIconService() {

        if (lineIconService == null) {
            lineIconService = new LineIconService(getDatabaseHelper());
        }
        return lineIconService;
    }
}
