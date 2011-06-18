package fr.itinerennes.database;

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
