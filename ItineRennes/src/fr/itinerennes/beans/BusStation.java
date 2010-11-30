package fr.itinerennes.beans;

import org.andnav.osm.util.GeoPoint;

/**
 * Bean representing a bus station.
 * 
 * @author Olivier Boudet
 */
public class BusStation implements Station {

    /** The identifier of the station. */
    private String id;

    /** The name of the station. */
    private String name;

    /** The latitude of the station. */
    private double latitude;

    /** The longitude of the station. */
    private double longitude;

    /**
     * Gets the identifier of the station.
     * 
     * @return the identifier of the station
     */
    public final String getId() {

        return id;
    }

    /**
     * Sets the identifier of the station.
     * 
     * @param id
     *            the identifier of the station to set
     */
    public final void setId(final String id) {

        this.id = id;
    }

    /**
     * Gets the name of the station.
     * 
     * @return the name of the station
     */
    public final String getName() {

        return name;
    }

    /**
     * Sets the name of the station.
     * 
     * @param name
     *            the name of the station to set
     */
    public final void setName(final String name) {

        this.name = name;
    }

    /**
     * Gets the latitude of the station.
     * 
     * @return the latitude of the station
     */
    public final double getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude of the station.
     * 
     * @param latitude
     *            the latitude of the station to set
     */
    public final void setLatitude(final double latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the station.
     * 
     * @return the longitude of the station
     */
    public final double getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude of the station.
     * 
     * @param longitude
     *            the longitude of the station to set
     */
    public final void setLongitude(final double longitude) {

        this.longitude = longitude;
    }

    @Override
    public final GeoPoint getGeoPoint() {

        return new GeoPoint(this.latitude, this.longitude);
    }

    @Override
    public final int getType() {

        return Station.TYPE_BUS;
    }

}
