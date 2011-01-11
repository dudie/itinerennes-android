package fr.itinerennes;


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
    public static final int CONFIG_RENNES_LAT = 48109600;

    /** Longitude of city Rennes. */
    public static final int CONFIG_RENNES_LON = -1679200;

    /** Minimum zoom level displaying overlay items. */
    public static final int CONFIG_MINIMUM_ZOOM_ITEMS = 15;

    /** Minimum zoom level for the map. */
    public static final int CONFIG_MINIMUM_ZOOM_MAP = 6;

    /** Maximum zoom level for the map. */
    public static final int CONFIG_MAXIMUM_ZOOM_MAP = 18;

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

    /** Geoserver stop feature prefix. */
    public static final String GEOSERVER_STOP_PREFIX = "stops.";

    /*
     * OpenTripPlanner constants.
     */

    /** OTP Extended API URL. */
    public static final String OTP_API_URL = "http://otp.itinerennes.fr/opentripplanner-api-extended/";

    /** Path to append to the Extended API URL to get stop informations. */
    public static final String OTP_API_STOP_PATH = "stop";

    /** Path to append to the Extended API URL to get departures informations. */
    public static final String OTP_API_DEPARTURES_PATH = "departures";

    /*
     * Database constantes.
     */

    /** The database schema version. */
    public static final int DATABASE_VERSION = 22;

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

    /** The keolis instant update time in seconds. */
    public static final int KEOLIS_INSTANT_UPDATE_TIME = 60;

    /** Minimum time between two calls to a keolis getAll*Stations() calls. */
    public static final int MIN_TIME_BETWEEN_KEOLIS_GET_ALL_CALLS = 3600;

    /** The time to live of an explored bounding box : {@value #GEO_CACHE_TTL}. */
    public static final int GEO_CACHE_TTL = 3600;

}
