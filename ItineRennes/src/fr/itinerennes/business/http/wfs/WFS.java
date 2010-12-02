package fr.itinerennes.business.http.wfs;

/**
 * Contains constants Geoserver WMS API.
 * 
 * @author Olivier Boudet
 */
public class WFS {

    /** The value of the service parameter. */
    public static final String VALUE_SERVICE = "WFS";

    /** The value of the parameter for feature request. */
    public static final String VALUE_FEATURE_REQUEST = "GetFeature";

    /** The value of the version parameter. */
    public static final String VALUE_VERSION = "1.1.0";

    /** The value of the format parameter. */
    public static final String VALUE_FORMAT = "JSON";

    /** The value of the srs parameter. */
    public static final String VALUE_SRS = "EPSG:4326";

    /** The value of the stops layers parameter. */
    public static final String VALUE_STOPS_LAYERS = "gtfsdb:stops";

    /** The value of the route layers parameter. */
    public static final String VALUE_ROUTES_LAYERS = "gtfsdb:routes";
}
