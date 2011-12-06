package fr.itinerennes;

/**
 * Constants for preferences.
 * 
 * @author Jérémie Huchet
 */
public final class ITRPrefs {

    /**
     * Private constructor to avoid instantiation.
     */
    private ITRPrefs() {

    }

    /** Preferences file name. */
    public static final String PREFS_NAME = "fr.itinerennes";

    /** Preferences key for map center latitude. */
    public static final String MAP_CENTER_LAT = String.format("%s.%s", PREFS_NAME, "latitude");

    /** Preferences key for map center longitude. */
    public static final String MAP_CENTER_LON = String.format("%s.%s", PREFS_NAME, "longitude");

    /** Preferences key for map zoom level. */
    public static final String MAP_ZOOM_LEVEL = String.format("%s.%s", PREFS_NAME, "zoomLevel");

    /** Preferences key for follow location feature. */
    public static final String MAP_SHOW_LOCATION = String.format("%s.%s", PREFS_NAME,
            "followLocation");

    /** Preference attribute name defining if the bus overlay should be displayed on startup. */
    public static final String OVERLAY_BUS_ACTIVATED = String.format("%s.%s", PREFS_NAME,
            "displayBusOverlayOnStartup");

    /** Preference attribute name defining if the bike overlay should be displayed on startup. */
    public static final String OVERLAY_BIKE_ACTIVATED = String.format("%s.%s", PREFS_NAME,
            "displayBikeOverlayOnStartup");

    /** Preference attribute name defining if the subway overlay should be displayed on startup. */
    public static final String OVERLAY_SUBWAY_ACTIVATED = String.format("%s.%s", PREFS_NAME,
            "displaySubwayOverlayOnStartup");

    /** Preference attribute name defining if the park overlay should be displayed on startup. */
    public static final String OVERLAY_PARK_ACTIVATED = String.format("%s.%s", PREFS_NAME,
            "displayParkOverlayOnStartup");
}
