package fr.itinerennes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * Easy access to itinerennes.properties configuration.
 * 
 * @author Jérémie Huchet
 */
public final class Conf {

    /*
     * Map configuration elements.
     */
    /** Default zoom level. */
    public static final int MAP_DEFAULT_ZOOM;

    /** Latitude of city Rennes. */
    public static final int MAP_RENNES_LAT;

    /** Longitude of city Rennes. */
    public static final int MAP_RENNES_LON;

    /**
     * Zoom level for zooming on a precise marker or location.
     */
    public static final int MAP_ZOOM_ON_LOCATION;

    /*
     * Itinerennes.
     */
    /** URL to get XML file describing Itinerennes versions. */
    public static final String ITINERENNES_VERSION_URL;

    /** The APK download page URL. */
    public static final String ITINERENNES_DOWNLOAD_PAGE_URL;

    /*
     * Nominatim.
     */
    /** Nominatim search bounding box offset. */
    public static final int NOMINATIM_SEARCH_OFFSET;

    /*
     * Keolis constants.
     */
    /** Keolis API URL. */
    public static final String KEOLIS_API_URL;

    /** Key for Keolis API. */
    public static final String KEOLIS_API_KEY;

    /*
     * OneBusAway constants.
     */
    /** OneBusAway API URL. */
    public static final String ONEBUSAWAY_API_URL;

    /** OneBusAway API version. */
    public static final String ONEBUSAWAY_API_KEY;

    /*
     * Database constants.
     */
    /** The database schema version. */
    public static final int DATABASE_SCHEMA_VERSION;

    /*
     * Misc.
     */
    /** True if ACRA reporting is enabled. */
    public static final boolean ACRA_ENABLED;

    /** Duration of toast messages. */
    public static final int TOAST_DURATION;

    /**
     * Private constructor to avoid instantiation.
     */
    private Conf() {

    }

    static {

        final Properties props = new Properties();
        try {
            props.load(Conf.class.getClassLoader().getResourceAsStream(
                    "assets/itinerennes.properties"));

            MAP_DEFAULT_ZOOM = getInteger(props, "map.default.zoom");
            MAP_RENNES_LAT = getInteger(props, "map.rennes.lat");
            MAP_RENNES_LON = getInteger(props, "map.rennes.lon");
            MAP_ZOOM_ON_LOCATION = getInteger(props, "map.zoom.on.location");

            ITINERENNES_VERSION_URL = getUrl(props, "itinerennes.version.url");
            ITINERENNES_DOWNLOAD_PAGE_URL = getUrl(props, "itinerennes.download.page.url");

            NOMINATIM_SEARCH_OFFSET = getInteger(props, "nominatim.search.offset");

            KEOLIS_API_URL = getUrl(props, "keolis.api.url");
            KEOLIS_API_KEY = getString(props, "keolis.api.key");

            ONEBUSAWAY_API_URL = getUrl(props, "onebusaway.api.url");
            ONEBUSAWAY_API_KEY = getString(props, "onebusaway.api.key");;

            DATABASE_SCHEMA_VERSION = getInteger(props, "database.schema.version");

            ACRA_ENABLED = getBoolean(props, "acra.enabled");
            TOAST_DURATION = getInteger(props, "toast.duration");

        } catch (final Throwable t) {
            throw new IllegalStateException("Can't load itinerennes.properties configuration file",
                    t);
        }
    }

    /**
     * Get a string property and ensure it is not null.
     * 
     * @param properties
     *            the configuration
     * @param key
     *            the property name
     * @return the property value
     */
    private static String getString(final Properties properties, final String key) {

        final String value = properties.getProperty(key);
        if (null == value || value.trim().equals("")) {
            throw new IllegalArgumentException(
                    String.format("empty value for property '%s'", value));
        }
        return value;
    }

    /**
     * Get a property as a boolean value.
     * 
     * @param properties
     *            the configuration
     * @param key
     *            the property name
     * @return a boolean value
     */
    private static boolean getBoolean(final Properties properties, final String key) {

        return Boolean.valueOf(getString(properties, key));
    }

    /**
     * Get an URL property as a string.
     * 
     * @param properties
     *            the configuration
     * @param key
     *            the property name
     * @return the url as a string value
     * @throws MalformedURLException
     *             a wrong URL was specified in configuration file
     */
    private static String getUrl(final Properties properties, final String key)
            throws MalformedURLException {

        // instantiate an URL object to validate syntax, but return a string
        return new URL(getString(properties, key)).toString();
    }

    /**
     * Get a property as a integer value.
     * 
     * @param properties
     *            the configuration
     * @param key
     *            the property name
     * @return an integer value
     */
    private static int getInteger(final Properties properties, final String key) {

        return Integer.valueOf(getString(properties, key));
    }

}
