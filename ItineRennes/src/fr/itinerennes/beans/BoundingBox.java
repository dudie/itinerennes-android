package fr.itinerennes.beans;

import org.andnav.osm.util.BoundingBoxE6;

/**
 * Simple class representing a Bounding Box.
 * 
 * @author Olivier Boudet
 */

public class BoundingBox {

    public double minLat;

    public double minLon;

    public double maxLat;

    public double maxLon;

    /**
     * Creates a BoundingBox from a square determined by latitude and longitude.
     * 
     * @param minLon
     *            the minimum longitude
     * @param maxLon
     *            the maximum longitude
     * @param minLat
     *            the minimum latitude
     * @param maxLat
     *            the maximum latitude
     */
    public BoundingBox(double minLon, double maxLon, double minLat, double maxLat) {

        this.minLon = minLon;
        this.maxLon = maxLon;
        this.minLat = minLat;
        this.maxLat = maxLat;
    }

    /**
     * Creates a BoundingBox from an osmdroid BoundingBoxE6 object.
     * 
     * @param visibleBoundingBoxE6
     */
    public BoundingBox(BoundingBoxE6 boundingBoxE6) {

        this.minLon = boundingBoxE6.getLonWestE6() / 1E6;
        this.maxLon = boundingBoxE6.getLonEastE6() / 1E6;
        this.minLat = boundingBoxE6.getLatSouthE6() / 1E6;
        this.maxLat = boundingBoxE6.getLatNorthE6() / 1E6;
    }

    public String toString() {

        return String.format("%s,%s,%s,%s", minLon, minLat, maxLon, maxLat);
    }
}
