package fr.itinerennes;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

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

    /**
     * Preference key where the version code of the last execution of the application is stored.
     * {@link fr.itinerennes.startup.LoadingActivity} is in charge to update this property.
     */
    public static final String PREV_EXEC_VERSION_CODE = "prev.exec.version.code";

    /** Preferences key for map center latitude. */
    public static final String MAP_CENTER_LAT = String.format("%s.%s", PREFS_NAME, "latitude");

    /** Preferences key for map center longitude. */
    public static final String MAP_CENTER_LON = String.format("%s.%s", PREFS_NAME, "longitude");

    /** Preferences key for map zoom level. */
    public static final String MAP_ZOOM_LEVEL = String.format("%s.%s", PREFS_NAME, "zoomLevel");

    /** Preferences key for follow location feature. */
    public static final String MAP_SHOW_LOCATION = String.format("%s.%s", PREFS_NAME, "followLocation");

    /** Preference key holding the name of the user tile provider preference. */
    public static final String MAP_TILE_PROVIDER = String.format("%s.%s", PREFS_NAME, "tileProvider");

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
