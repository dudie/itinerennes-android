package fr.itinerennes.database;

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

import android.provider.BaseColumns;

/**
 * Contains database columns names constants.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public final class Columns {

    /**
     * Generic columns for a geographic positioned element.
     * 
     * @author Jérémie Huchet
     */
    public interface LocationColumns extends BaseColumns {

        /**
         * The latitude for a marker.
         * <P>
         * Type: INTEGER
         * </P>
         */
        String LATITUDE = "lat";

        /**
         * The longitude for a marker.
         * <P>
         * Type: INTEGER
         * </P>
         */
        String LONGITUDE = "lon";
    }

    /**
     * Generic columns for a station.
     * 
     * @author Olivier Boudet
     */
    public interface MarkersColumns extends LocationColumns {

        /** The name of the markers table. */
        String MARKERS_TABLE_NAME = "markers";

        /** ContentProvider type. */
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.itinerennes.markers";

        /**
         * The unique ID for a station.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String ID = "id";

        /**
         * The type of a station.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String TYPE = "type";

        /**
         * The name for a station.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String LABEL = "label";

        /**
         * The name for a station.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String SEARCH_LABEL = "search_label";

        /**
         * Indicates if a station is bookmarked.
         * <P>
         * Type: BOOLEAN
         * </P>
         */
        String IS_BOOKMARKED = "is_bookmarked";

        /**
         * The name of the city in which the marker is.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String CITY = "city";
    }

    /**
     * Columns for a bookmark.
     * 
     * @author Jérémie Huchet
     */
    public interface BookmarksColumns extends BaseColumns {

        /** The name of the bookmarks table. */
        String BOOKMARKS_TABLE_NAME = "bookmarks";

        /**
         * The label of the bookmark.
         * <P>
         * Type: STRING
         * </P>
         */
        String LABEL = "label";

        /**
         * The type of the resource the bookmark represents.
         * <P>
         * Type: STRING
         * </P>
         */
        String TYPE = "type";

        /**
         * The identifier of the resource the bookmark represents.
         * <P>
         * Type: STRING
         * </P>
         */
        String ID = "id";
    }

    /**
     * Columns for accessibility attributes.
     * 
     * @author Olivier Boudet
     */
    public interface AccessibilityColumns extends BaseColumns {

        /** The name of the accessibility table. */
        String ACCESSIBILITY_TABLE_NAME = "accessibility";

        /**
         * The id of the ressource.
         * <P>
         * Type: STRING
         * </P>
         */
        String ID = "id";

        /**
         * The type of the resource.
         * <P>
         * Type: STRING
         * </P>
         */
        String TYPE = "type";

        /**
         * The flag indicating if a ressource is accessible for wheelchairs.
         * <P>
         * Type: INTEGER
         * </P>
         */
        String WHEELCHAIR = "wheelchair";
    }

    /**
     * Columns for routes and stops relationships.
     * 
     * @author Jérémie Huchet
     */
    public interface RoutesStopsColumns extends BaseColumns {

        /** The name of the table for routes and stops relationships. */
        String ROUTES_STOPS_TABLE_NAME = "routesstops";

        /**
         * The route identifier.
         * <P>
         * Type: STRING
         * </P>
         */
        String ROUTE_ID = "route_id";

        /**
         * The stop identifier.
         * <P>
         * Type: STRING
         * </P>
         */
        String STOP_ID = "stop_id";

    }

    /**
     * Columns for nominatim address.
     * 
     * @author Jérémie Huchet
     */
    public interface NominatimColumns extends LocationColumns {

        // no table name
        // /** The name of the nominatim table. */
        // String NOMINATIM_TABLE_NAME = "nominatim";

        /**
         * The address display name.
         * <P>
         * Type: STRING
         * </P>
         */
        String DISPLAY_NAME = "display_name";
    }
}
