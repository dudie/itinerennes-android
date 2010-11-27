package fr.itinerennes.beans;

import java.util.Date;

import org.andnav.osm.util.GeoPoint;

/**
 * Represents a subway station.
 * 
 * @author Jérémie Huchet
 */
public class SubwayStation implements Station {

    /** The identifier of the subway station. */
    private String id;

    /** The name of the subway station. */
    private String name;

    /** The latitude of the subway station. */
    private double latitude;

    /** The longitude of the subway station. */
    private double longitude;

    /** Has the platform a direction 1 ? */
    private boolean hasPlatformDirection1;

    /** Has the platform a direction 2 ? */
    private boolean hasPlatformDirection2;

    /** The ranking platform direction 1. */
    private int rankingPlatformDirection1;

    /** The ranking platform direction 2. */
    private int rankingPlatformDirection2;

    /** The floors. */
    private int floors;

    /** The last update date of these informations. */
    private Date lastUpdate;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    @Override
    public String getId() {

        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the id to set
     */
    public void setId(final String id) {

        this.id = id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    @Override
    public String getName() {

        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the name to set
     */
    public void setName(final String name) {

        this.name = name;
    }

    /**
     * Gets the latitude.
     * 
     * @return the latitude
     */
    public double getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude.
     * 
     * @param latitude
     *            the latitude to set
     */
    public void setLatitude(final double latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     * 
     * @return the longitude
     */
    public double getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude.
     * 
     * @param longitude
     *            the longitude to set
     */
    public void setLongitude(final double longitude) {

        this.longitude = longitude;
    }

    /**
     * Gets the hasPlatformDirection1.
     * 
     * @return the hasPlatformDirection1
     */
    public boolean isHasPlatformDirection1() {

        return hasPlatformDirection1;
    }

    /**
     * Sets the hasPlatformDirection1.
     * 
     * @param hasPlatformDirection1
     *            the hasPlatformDirection1 to set
     */
    public void setHasPlatformDirection1(final boolean hasPlatformDirection1) {

        this.hasPlatformDirection1 = hasPlatformDirection1;
    }

    /**
     * Gets the hasPlatformDirection2.
     * 
     * @return the hasPlatformDirection2
     */
    public boolean isHasPlatformDirection2() {

        return hasPlatformDirection2;
    }

    /**
     * Sets the hasPlatformDirection2.
     * 
     * @param hasPlatformDirection2
     *            the hasPlatformDirection2 to set
     */
    public void setHasPlatformDirection2(final boolean hasPlatformDirection2) {

        this.hasPlatformDirection2 = hasPlatformDirection2;
    }

    /**
     * Gets the rankingPlatformDirection1.
     * 
     * @return the rankingPlatformDirection1
     */
    public int getRankingPlatformDirection1() {

        return rankingPlatformDirection1;
    }

    /**
     * Sets the rankingPlatformDirection1.
     * 
     * @param rankingPlatformDirection1
     *            the rankingPlatformDirection1 to set
     */
    public void setRankingPlatformDirection1(final int rankingPlatformDirection1) {

        this.rankingPlatformDirection1 = rankingPlatformDirection1;
    }

    /**
     * Gets the rankingPlatformDirection2.
     * 
     * @return the rankingPlatformDirection2
     */
    public int getRankingPlatformDirection2() {

        return rankingPlatformDirection2;
    }

    /**
     * Sets the rankingPlatformDirection2.
     * 
     * @param rankingPlatformDirection2
     *            the rankingPlatformDirection2 to set
     */
    public void setRankingPlatformDirection2(final int rankingPlatformDirection2) {

        this.rankingPlatformDirection2 = rankingPlatformDirection2;
    }

    /**
     * Gets the floors.
     * 
     * @return the floors
     */
    public int getFloors() {

        return floors;
    }

    /**
     * Sets the floors.
     * 
     * @param floors
     *            the floors to set
     */
    public void setFloors(final int floors) {

        this.floors = floors;
    }

    /**
     * Gets the lastUpdate.
     * 
     * @return the lastUpdate
     */
    public Date getLastUpdate() {

        return lastUpdate;
    }

    /**
     * Sets the lastUpdate.
     * 
     * @param lastUpdate
     *            the lastUpdate to set
     */
    public void setLastUpdate(final Date lastUpdate) {

        this.lastUpdate = lastUpdate;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.beans.Station#getGeoPoint()
     */
    @Override
    public GeoPoint getGeoPoint() {

        return new GeoPoint(latitude, longitude);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.beans.Station#getType()
     */
    @Override
    public int getType() {

        return Station.TYPE_SUBWAY;
    }

}
