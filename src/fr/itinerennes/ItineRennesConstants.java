package fr.itinerennes;

/**
 * @author Jérémie Huchet
 */
public class ItineRennesConstants {

    /*
     * Default config values.
     */

    /** Default zoom level. */
    public static final int CONFIG_DEFAULT_ZOOM = 12;

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
}
