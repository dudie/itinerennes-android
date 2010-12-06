package fr.itinerennes;

import fr.itinerennes.beans.BikeStation;

/**
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class ItineRennesConstants {

    /*
     * Default config values.
     */

    /** Default zoom level. */
    public static final int CONFIG_DEFAULT_ZOOM = 16;

    /** Latitude of city Rennes. */
    public static final double CONFIG_RENNES_LAT = 48.1096;

    /** Longitude of city Rennes. */
    public static final double CONFIG_RENNES_LON = -1.6792;

    /*
     * Keolis constants.
     */

    /** Keolis API URL. */
    public static final String KEOLIS_API_URL = "http://data.keolis-rennes.com/json/";

    /** Key for Keolis API. */
    public static final String KEOLIS_API_KEY = "E6S9CADHA5XK4T4";

    /** Keolis API version. */
    public static final String KEOLIS_API_VERSION = "2.0";

    /*
     * Geoserver constants.
     */

    /** Geoserver API URL. */
    public static final String GEOSERVER_API_URL = "http://otp.itinerennes.fr/geoserver/ows/";

    /*
     * Database constantes.
     */
    /** The database schema version. */
    public static final int DATABASE_VERSION = 5;

    /** The database name. */
    public static final String DATABASE_NAME = "fr.itinerennes";

    /** The database create script URI. */
    public static final String DATABASE_CREATE_SCRIPT_URI = "/fr/itinerennes/database/create_database.sql";

    /** The database drop script URI. */
    public static final String DATABASE_DROP_SCRIPT_URI = "/fr/itinerennes/database/drop_database.sql";

    /** The database update script URI. */
    public static final String DATABASE_UPDATE_SCRIPT_URI = "/fr/itinerennes/database/update_database.sql";

    /*
     * Cache life time values.
     */
    /** Life time for {@link BikeStation}s : {@value #TTL_BIKE_STATIONS} seconds. */
    public static final int TTL_BIKE_STATIONS = 3600;
}
