package fr.itinerennes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Easy access to itinerennes.properties configuration.
 * 
 * @author Jérémie Huchet
 */
public final class Conf {

    /**
     * Annomation to mark a field to be injected from the configuration file.
     * 
     * @author Jérémie Huchet
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface Property {

        /**
         * The property name.
         */
        String name() default "default";
    }

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Conf.class);

    /*
     * Map configuration elements.
     */
    /** Default zoom level. */
    @Property
    public static int MAP_DEFAULT_ZOOM;

    /** Latitude of city Rennes. */
    @Property
    public static int MAP_RENNES_LAT;

    /** Longitude of city Rennes. */
    @Property
    public static int MAP_RENNES_LON;

    /**
     * Minimum zoom level displaying detailed overlay items. On higher zoom, a simple circle will be
     * displayed.
     */
    @Property
    public static int MAP_MINIMUM_ZOOM_ITEMS;

    /**
     * Zoom level for zooming on a precise marker or location.
     */
    @Property
    public static int MAP_ZOOM_ON_LOCATION;

    /*
     * Itinerennes.
     */
    /** URL to get XML file describing Itinerennes versions. */
    @Property
    public static String ITINERENNES_VERSION_URL;

    /** The APK download page URL. */
    @Property
    public static String ITINERENNES_DOWNLOAD_PAGE_URL;

    /*
     * Nominatim.
     */
    /** Nominatim search bounding box offset. */
    @Property
    public static int NOMINATIM_SEARCH_OFFSET = 150000;

    /*
     * Keolis constants.
     */
    /** Keolis API URL. */
    @Property
    public static String KEOLIS_API_URL;

    /** Key for Keolis API. */
    @Property
    public static String KEOLIS_API_KEY;

    /*
     * OneBusAway constants.
     */
    /** OneBusAway API URL. */
    @Property
    public static String ONEBUSAWAY_API_URL;

    /** OneBusAway API version. */
    @Property
    public static String ONEBUSAWAY_API_KEY;

    /*
     * Database constants.
     */
    /** The database schema version. */
    @Property
    public static int DATABASE_SCHEMA_VERSION;

    /*
     * Misc.
     */
    /** True if ACRA reporting is enabled. */
    @Property
    public static boolean ACRA_ENABLED;

    /** Duration of toast messages. */
    @Property
    public static final int TOAST_DURATION = 5000;

    static {
        new Conf();
    }

    /**
     * Reads the configuration file.
     */
    private Conf() {

        final Properties props = new Properties();
        try {
            props.load(Conf.class.getClassLoader().getResourceAsStream(
                    "assets/itinerennes.properties"));

            for (final Field field : this.getClass().getFields()) {
                final Property prop = field.getAnnotation(Property.class);
                if (null != prop) {
                    loadProperty(field, prop, props);
                }
            }

            ACRA_ENABLED = Boolean.valueOf(props.getProperty("acra.enabled"));

        } catch (final Throwable t) {
            throw new IllegalStateException("Can't load itinerennes.properties configuration file",
                    t);
        }
    }

    private void loadProperty(final Field field, final Property prop, final Properties properties)
            throws IllegalArgumentException, IllegalAccessException, MalformedURLException {

        final String name;
        if ("default".equals(prop.name())) {
            name = field.getName().toLowerCase().replaceAll("[^a-z0-9]", ".");
        } else {
            name = prop.name();
        }

        if (properties.containsKey(name)) {
            field.set(null, convertValue(field, properties.getProperty(name)));
        } else {
            throw new IllegalStateException(String.format("Missing property named '%s'", name));
        }
    }

    private Object convertValue(final Field field, final String stringValue)
            throws MalformedURLException {

        final Object value;
        if ("boolean".equals(field.getType().getName()) || Boolean.class.equals(field.getType())) {
            value = Boolean.valueOf(stringValue);
        } else if ("int".equals(field.getType().getName()) || Integer.class.equals(field.getType())) {
            value = Integer.valueOf(stringValue);
        } else if (URL.class.equals(field.getType())) {
            value = new URL(stringValue).toString();
        } else {
            value = stringValue;
        }
        return value;
    }
}
