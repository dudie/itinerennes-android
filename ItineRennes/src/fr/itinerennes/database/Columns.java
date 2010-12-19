package fr.itinerennes.database;

import android.provider.BaseColumns;

/**
 * Contains database columns names constants.
 * 
 * @author Jérémie Huchet
 */
public final class Columns {

    /**
     * Columns for cache metadata. {@link #TYPE} and {@link #ID} are unique and references a unique
     * value.
     * 
     * @author Jérémie Huchet
     */
    public interface MetadataColumns extends BaseColumns {

        /**
         * The type of the data represented by this metadata.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String TYPE = "type";

        /**
         * The unique ID of the data represented by this metadata.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String ID = "id";

        /**
         * The last update date of this metadata entry.
         * <P>
         * Type: INTEGER (unix timestamp in seconds in seconds)
         * </P>
         */
        String LAST_UPDATE = "last_update";

    }

    /**
     * Columns for geo explore cache.
     * 
     * @author Jérémie Huchet
     */
    public interface GeoExploreColumns extends BaseColumns {

        /**
         * The type of the data explored.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String TYPE = "type";

        /**
         * The west longitude bound of the box.
         * <P>
         * Type: INTEGER (int)
         * </P>
         */
        String LON_WEST = "lon_west";

        /**
         * The north longitude bound of the box.
         * <P>
         * Type: INTEGER (int)
         * </P>
         */
        String LAT_NORTH = "lat_north";

        /**
         * The east longitude bound of the box.
         * <P>
         * Type: INTEGER (int)
         * </P>
         */
        String LON_EAST = "lon_east";

        /**
         * The south longitude bound of the box.
         * <P>
         * Type: INTEGER (int)
         * </P>
         */
        String LAT_SOUTH = "lat_south";

        /**
         * The last update date of this explored bounding box.
         * <P>
         * Type: INTEGER (unix timestamp in seconds)
         * </P>
         */
        String LAST_UPDATE = "last_update";
    }

    /**
     * Generic columns for a station.
     * 
     * @author Jérémie Huchet
     */
    public interface StationColumns extends BaseColumns {

        /**
         * The unique ID for a station.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String ID = "id";

        /**
         * The name for a station.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String NAME = "name";

        /**
         * The latitude for a station.
         * <P>
         * Type: REAL (double)
         * </P>
         */
        String LATITUDE = "lat";

        /**
         * The latitude for a station.
         * <P>
         * Type: REAL (double)
         * </P>
         */
        String LONGITUDE = "lon";
    }

    /**
     * Generic columns for a bike station.
     * 
     * @author Jérémie Huchet
     */
    public interface BikeStationColumns extends StationColumns {

        /**
         * The street name for a bike station.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String STREET_NAME = "street_name";

        /**
         * The state for a bike station.
         * <P>
         * Type: INTEGER (boolean)
         * </P>
         */
        String IS_ACTIVE = "is_active";

        /**
         * The amount of available slots for a bike station.
         * <P>
         * Type: INTEGER (int)
         * </P>
         */
        String AVAILABLE_SLOTS = "avail_slots";

        /**
         * The amount of available bikes for a bike station.
         * <P>
         * Type: INTEGER (int)
         * </P>
         */
        String AVAILABLE_BIKES = "avail_bikes";

        /**
         * The bike station provides a point of sale.
         * <P>
         * Type: INTEGER (boolean)
         * </P>
         */
        String IS_POS = "is_pos";

        /**
         * The district name for a bike station.
         * <P>
         * Type: TEXT (String)
         * </P>
         */
        String DISTRICT_NAME = "district_name";

        /**
         * The last update date of available bikes and slots for a bike station.
         * <P>
         * Type: INTEGER (unix timestamp in seconds in seconds)
         * </P>
         */
        String LAST_UPDATE = "last_update";
    }

    /**
     * Generic columns for a bus station.
     * 
     * @author Olivier Boudet
     */
    public interface BusStationColumns extends StationColumns {

    }

    /**
     * Generic columns for the icon of a transport line.
     * 
     * @author Jérémie Huchet
     */
    public interface LineIconColumns extends BaseColumns {

        /**
         * The line identifier the icon belongs to.
         * <P>
         * Type: STRING
         * </P>
         */
        String LINE_ID = "line_id";

        /**
         * The URL where to fetch the icon.
         * <P>
         * Type: STRING
         * </P>
         */
        String URL = "url";

        /**
         * The bytes of the icon image.
         * <P>
         * Type: BLOB
         * </P>
         */
        String ICON = "icon";
    }
}
