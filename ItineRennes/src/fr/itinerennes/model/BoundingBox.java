package fr.itinerennes.model;

import org.andnav.osm.util.BoundingBoxE6;

/**
 * Simple class representing a Bounding Box.
 * 
 * @author Olivier Boudet
 */

public class BoundingBox {

    /**
     * Minimum latitude of the bounding box.
     */
    private double minLat;

    /**
     * Minimum longitude of the bounding box.
     */
    private double minLon;

    /**
     * Maximum latitude of the bounding box.
     */
    private double maxLat;

    /**
     * Maximum longitude of the bounding box.
     */
    private double maxLon;

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
    public BoundingBox(final double minLon, final double maxLon, final double minLat,
            final double maxLat) {

        this.minLon = minLon;
        this.maxLon = maxLon;
        this.minLat = minLat;
        this.maxLat = maxLat;
    }

    /**
     * Creates a BoundingBox from an osmdroid BoundingBoxE6 object.
     * 
     * @param boundingBoxE6
     */
    public BoundingBox(final BoundingBoxE6 boundingBoxE6) {

        this.minLon = boundingBoxE6.getLonWestE6() / 1E6;
        this.maxLon = boundingBoxE6.getLonEastE6() / 1E6;
        this.minLat = boundingBoxE6.getLatSouthE6() / 1E6;
        this.maxLat = boundingBoxE6.getLatNorthE6() / 1E6;
    }

    public final String toString() {

        return String.format("%s,%s,%s,%s", minLon, minLat, maxLon, maxLat);
    }
}
