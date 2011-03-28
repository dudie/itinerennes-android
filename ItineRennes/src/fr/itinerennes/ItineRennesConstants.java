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

    /*
     * Keolis constants.
     */

    /** Keolis API URL. */
    public static final String KEOLIS_API_URL = "http://data.keolis-rennes.com/json/";

    /** Key for Keolis API. */
    public static final String KEOLIS_API_KEY = "E6S9CADHA5XK4T4";

    /*
     * OneBusAway constants.
     */
    /** OneBusAway API URL. */
    public static final String OBA_API_URL = "http://api.itinerennes.fr/onebusaway-api-webapp/api/";

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

    /** The database insert script URI for bus station wheelchair accessibility. */
    public static final String DATABASE_INSERT_ACCESSIBILITY_SCRIPT_URI = "/fr/itinerennes/database/accessibility.sql";

    /** Map constants */
    public static final String MARKER_TYPE_BIKE = "BIKE";

    public static final String MARKER_TYPE_BUS = "BUS";

    public static final String MARKER_TYPE_SUBWAY = "SUBWAY";

}
