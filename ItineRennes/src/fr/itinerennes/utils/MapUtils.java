package fr.itinerennes.utils;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.GeoPoint;

public final class MapUtils {

    /**
     * Private constructor to avoid instantiation.
     */
    private MapUtils() {

    }

    /**
     * Decodes the given polyline string to a list of geopoints.
     * 
     * @param encoded
     *            an encoded polyline
     * @param points
     *            the amount of points the encoded polyline string contains (used to creates the
     *            geopoint list with the right size)
     * @return a list of geopoints
     */
    public static List<GeoPoint> decodePolyline(final String encoded, final int points) {

        final List<GeoPoint> poly = new ArrayList<GeoPoint>(points);
        int index = 0;
        final int len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            final int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            final int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            final GeoPoint p = new GeoPoint((int) ((lat / 1E5) * 1E6), (int) ((lng / 1E5) * 1E6));
            poly.add(p);
        }

        return poly;
    }

    /**
     * Decodes the given polyline string to a list of geopoints.
     * 
     * @param encoded
     *            an encoded polyline
     * @return a list of geopoints
     * @see #decodePolyline(String, int)
     */
    public static List<GeoPoint> decodePolyline(final String encoded) {

        return decodePolyline(encoded, 10);
    }
}
