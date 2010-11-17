package fr.itinerennes.business.http.keolis;

/**
 * Contains constants for variables for Keolis API.
 * 
 * @author Jérémie Huchet
 */
public final class Keo {

    /** The name of the generic value attribute name. */
    public static final String VALUE = "param[value]";

    /**
     * Contains constants for keolis commands parameter.
     * 
     * @author Jérémie Huchet
     */
    public static final class Command {

        /** Keolis result code for success. */
        public static final int RESULT_SUCCESS = 0;

        /** The name of the parameter to use to set the method name. */
        public static final String ATT_NAME = "cmd";

        /** Constant for the name of the method to retrieve informations about bike stations. */
        public static final String GET_BIKE_STATIONS = "getbikestations";

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

        /** The name of the network attribute. */
        public static final String ATT_NAME = "param[network]";

        /** Constant for the name of the network attribute to set in API calls. */
        public static final String LE_VELO_STAR = "levelostar";
    }

    /**
     * Contains constants for keolis station parameter.
     * 
     * @author Jérémie Huchet
     */
    public static final class Station {

        /** The name of the mode attribute. */
        public static final String ATT_NAME = "param[station]";

        /** Constant for the attribute value to get all stations. */
        public static final String ALL = "all";

        /** Constant for the attribute value to get stations by proximity. */
        public static final String PROXIMITY = "proximity";

        /** Constant for the attribute value to get stations by identifier / number. */
        public static final String IDENTIFIER = "number";
    }

    /**
     * Contains constants for keolis mode parameter.
     * 
     * @author Jérémie Huchet
     */
    public static final class ProximityMode {

        /** The name of the mode attribute. */
        public static final String ATT_NAME = "param[mode]";

        /** Constant for the attribute value to get proximity stations by identifier / number. */
        public static final String ID = "id";

        /** Constant for the attribute value to get proximity stations coordinates. */
        public static final String COORD = "coord";
    }

    /**
     * Contains constants for keolis coords type parameter.
     * 
     * @author Jérémie Huchet
     */
    public static final class Coords {

        /** Constant for the attribute name to set a latitude. */
        public static final String LATITUDE = "param[lat]";

        /** Constant for the attribute name to set a longitude.. */
        public static final String LONGITUDE = "param[long]";
    }
}
