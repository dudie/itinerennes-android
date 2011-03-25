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

    /**
     * Minimum zoom level displaying detailed overlay items. On higher zoom, a simple circle will be
     * displayed.
     */
    public static final int CONFIG_MINIMUM_ZOOM_ITEMS = 17;

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
    public static final String GEOSERVER_API_URL = "http://api.itinerennes.fr/geoserver/ows/";

    /** Geoserver stop feature prefix. */
    public static final String GEOSERVER_STOP_PREFIX = "stops.";

    /*
     * OneBusAway constants.
     */
    /** OneBusAway API URL. */
    public static final String OBA_API_URL = "http://api.itinerennes.fr/onebusaway-api-webapp/api/";

    /** OneBusAway API version. */
    public static final String OBA_API_VERSION = "2";

    /** OneBusAway API version. */
    public static final String OBA_API_KEY = "web";

    /*
     * Database constantes.
     */

    /** The database schema version. */
    public static final int DATABASE_VERSION = 24;

    /** The database name. */
    public static final String DATABASE_NAME = "fr.itinerennes";

    /** The database create script URI. */
    public static final String DATABASE_CREATE_SCRIPT_URI = "/fr/itinerennes/database/create_database.sql";

    /** The database drop script URI. */
    public static final String DATABASE_DROP_SCRIPT_URI = "/fr/itinerennes/database/drop_database.sql";

    /** The database update script URI. */
    public static final String DATABASE_UPDATE_SCRIPT_URI = "/fr/itinerennes/database/update_database.sql";

    /** The database insert script URI for bus station wheelchair accessibility. */
    public static final String DATABASE_INSERT_ACCESSIBILITY_SCRIPT_URI = "/fr/itinerennes/database/accessibility.sql";

    /*
     * Cache life time values.
     */

    /** The keolis instant update time in seconds. */
    public static final int KEOLIS_INSTANT_UPDATE_TIME = 60; // 1 minute

    /** Minimum time between two calls to a keolis getAll*Stations() calls. */
    public static final int MIN_TIME_BETWEEN_KEOLIS_GET_ALL_CALLS = 604800; // 7 days

    /** The time to live of an explored bounding box : {@value #GEO_CACHE_TTL}. */
    public static final int GEO_CACHE_TTL = 7776000; // 3 months

    /** Map constants */
    public static final String MARKER_TYPE_BIKE = "BIKE";

    public static final String MARKER_TYPE_BUS = "BUS";

    public static final String MARKER_TYPE_SUBWAY = "SUBWAY";

}
