package fr.itinerennes.business.http.keolis;

/**
 * Contains constants for variables for Keolis API.
 * 
 * @author Jérémie Huchet
 */
public final class Keo {

    /** Avoid instantiation. */
    private Keo() {

    }

    /** Parameter to set the Keolis API version used. */
    public static final String API_VERSION = "version";

    /** Parameter to set the Keolis API key. */
    public static final String API_KEY = "key";

    /**
     * Contains constants for keolis commands parameter.
     * 
     * @author Jérémie Huchet
     */
    public static final class Command {

        /** Avoid instantiation. */
        private Command() {

        }

        /** Keolis result code for success. */
        public static final int RESULT_SUCCESS = 0;

        /** The name of the parameter to use to set the method name. */
        public static final String PARAM_NAME = "cmd";

        /** Constant for the name of the method to retrieve informations about bike stations. */
        public static final String GET_BIKE_STATIONS = "getbikestations";

        /** Constant for the name of the method to retrieve all bike districts. */
        public static final String GET_BIKE_DISTRICTS = "getbikedistricts";

        /** Constant for the name of the method to retrieve alert informations about lines. */
        public static final String GET_LINES_ALERT = "getlinesalerts";

        /** Constant for the name of the method to retrieve URLs to fetch lines icons. */
        public static final String GET_LINES_ICONS = "getlines";

        /** Constant for the name of the method to retrieve the equipments of stations. */
        public static final String GET_EQUIPEMENTS = "getequipments";

        /** Constant for the name of the method to retrieve the status of equipments. */
        public static final String GET_EQUIPEMENTS_STATUS = "getequipmentsstatus";

        /** Constant for the name of the method to retrieve the subway stations. */
        public static final String GET_METRO_STATIONS = "getmetrostations";

        /** Constant for the name of the method to retrieve the subway stations status. */
        public static final String GET_METRO_STATIONS_STATUS = "getmetrostationsstatus";

        /** Constant for the name of the method to retrieve the relay parks. */
        public static final String GET_RELAY_PARKS = "getrelayparks";

        /** Constant for the name of the method to retrieve the points of sale. */
        public static final String GET_POINT_OF_SALE = "getpos";
    }

    /**
     * Contains constants for keolis networks parameter.
     * 
     * @author Jérémie Huchet
     */
    public static final class Network {

        /** Avoid instantiation. */
        private Network() {

        }

        /** The name of the network attribute. */
        public static final String PARAM_NAME = "param[network]";

        /** Constant for the name of the network attribute to set in API calls for bikes. */
        public static final String VALUE_LE_VELO_STAR = "levelostar";

        /** Constant for the name of the network attribute to set in API calls for subway and bus. */
        public static final String VALUE_STAR = "star";
    }

    /**
     * Contains constants for method {@link Command#GET_BIKE_STATIONS}.
     * 
     * @author Jérémie Huchet
     */
    public static final class GetBikeStations {

        /** Avoid instantiation. */
        private GetBikeStations() {

        }

        /**
         * Used to set the fetch mode. See its values {@link #VALUE_STATION_ALL},
         * {@link #VALUE_STATION_PROXIMITY}, {@link #VALUE_STATION_DISTRICT},
         * {@link #VALUE_STATION_IDENTIFIER}.
         */
        public static final String PARAM_STATION = "param[station]";

        /** Fetch all stations. */
        public static final String VALUE_STATION_ALL = "all";

        /** Fetch 3 stations by proximity. You need to specify {@link #PARAM_MODE}. */
        public static final String VALUE_STATION_PROXIMITY = "proximity";

        /**
         * Fetch stations by district. You need to specify {@link #PARAM_VALUE} with a district
         * name.
         */
        public static final String VALUE_STATION_DISTRICT = "district";

        /**
         * Fetch station by identifier. You need to specify {@link #PARAM_VALUE} with a station
         * identifier.
         */
        public static final String VALUE_STATION_IDENTIFIER = "number";

        /**
         * Sets the proximity mode. Use with {@link #PARAM_STATION}={@link #VALUE_STATION_PROXIMITY}
         * . Can take values {@link #VALUE_MODE_COORDINATES} or {@link #VALUE_MODE_IDENTIFIER}.
         */
        public static final String PARAM_MODE = "param[mode]";

        /** Fetch the 3 closest stations of the given station id. */
        public static final String VALUE_MODE_IDENTIFIER = "id";

        /** Fetch the 3 closest stations of the given coordinates. */
        public static final String VALUE_MODE_COORDINATES = "coord";

        /** Use with {@link #PARAM_MODE}={@link #VALUE_MODE_COORDINATES} to set the latitude value. */
        public static final String PARAM_LATITUDE = "param[lat]";

        /** Use with {@link #PARAM_MODE}={@link #VALUE_MODE_COORDINATES} to set the longitude value. */
        public static final String PARAM_LONGITUDE = "param[long]";

        /**
         * Use with {@link #PARAM_STATION}={@link #VALUE_STATION_DISTRICT} to set the district name
         * or {@link #PARAM_STATION}={@link #VALUE_STATION_IDENTIFIER} to set the identifier of the
         * station.
         */
        public static final String PARAM_VALUE = "param[value]";
    }

    /**
     * Contains constants for method {@link Command#GET_METRO_STATIONS}.
     * 
     * @author Jérémie Huchet
     */
    public static final class GetSubwayStations {

        /** Avoid instantiation. */
        private GetSubwayStations() {

        }

        /**
         * Used to set the fetch mode. See its values {@link #VALUE_MODE_ALL},
         * {@link #VALUE_MODE_STATION}, {@link #VALUE_STATION_PROXIMITY}.
         */
        public static final String PARAM_MODE = "param[mode]";

        /** Fetch all stations. */
        public static final String VALUE_MODE_ALL = "all";

        /**
         * Fetch stations by identifier. You need to specify {@link #PARAM_STATION} with a station
         * identifier.
         */
        public static final String VALUE_MODE_STATION = "station";

        /** Fetch 3 stations by proximity. You need to specify {@link #PARAM_PROXIMITY_TYPE}. */
        public static final String VALUE_MODE_PROXIMITY = "proximity";

        /**
         * Use with {@link #PARAM_MODE}={@link #VALUE_MODE_STATION} to set the identifier of the
         * station to fetch.
         */
        public static final String PARAM_STATION_IDENTIFIER = "param[station]";

        /** Used to set the kind of proximity look up. */
        public static final String PARAM_PROXIMITY_TYPE = "param[type]";

        /** Fetch the 3 closest stations of the given station id. */
        public static final String VALUE_PROXIMITY_TYPE_IDENTIFIER = "station";

        /** Fetch the 3 closest stations of the given coordinates. */
        public static final String VALUE_PROXIMITY_TYPE_COORDINATES = "coords";

        /**
         * Use with {@link #PARAM_PROXIMITY_TYPE}={@link #VALUE_PROXIMITY_TYPE_COORDINATES} to set
         * the latitude value.
         */
        public static final String PARAM_LATITUDE = "param[lat]";

        /**
         * Use with {@link #PARAM_PROXIMITY_TYPE}={@link #VALUE_PROXIMITY_TYPE_COORDINATES} to set
         * the longitude value.
         */
        public static final String PARAM_LONGITUDE = "param[lng]";
    }
}
