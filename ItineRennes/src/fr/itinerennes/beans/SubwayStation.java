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
    private int latitude;

    /** The longitude of the subway station. */
    private int longitude;

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
    public final String getId() {

        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the id to set
     */
    public final void setId(final String id) {

        this.id = id;
    }

    /**
     * Gets the name.
     * 
     * @return the name
     */
    @Override
    public final String getName() {

        return name;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            the name to set
     */
    public final void setName(final String name) {

        this.name = name;
    }

    /**
     * Gets the latitude.
     * 
     * @return the latitude
     */
    public final int getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude.
     * 
     * @param latitude
     *            the latitude to set
     */
    public final void setLatitude(final int latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longitude.
     * 
     * @return the longitude
     */
    public final int getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude.
     * 
     * @param longitude
     *            the longitude to set
     */
    public final void setLongitude(final int longitude) {

        this.longitude = longitude;
    }

    /**
     * Gets the hasPlatformDirection1.
     * 
     * @return the hasPlatformDirection1
     */
    public final boolean isHasPlatformDirection1() {

        return hasPlatformDirection1;
    }

    /**
     * Sets the hasPlatformDirection1.
     * 
     * @param hasPlatformDirection1
     *            the hasPlatformDirection1 to set
     */
    public final void setHasPlatformDirection1(final boolean hasPlatformDirection1) {

        this.hasPlatformDirection1 = hasPlatformDirection1;
    }

    /**
     * Gets the hasPlatformDirection2.
     * 
     * @return the hasPlatformDirection2
     */
    public final boolean isHasPlatformDirection2() {

        return hasPlatformDirection2;
    }

    /**
     * Sets the hasPlatformDirection2.
     * 
     * @param hasPlatformDirection2
     *            the hasPlatformDirection2 to set
     */
    public final void setHasPlatformDirection2(final boolean hasPlatformDirection2) {

        this.hasPlatformDirection2 = hasPlatformDirection2;
    }

    /**
     * Gets the rankingPlatformDirection1.
     * 
     * @return the rankingPlatformDirection1
     */
    public final int getRankingPlatformDirection1() {

        return rankingPlatformDirection1;
    }

    /**
     * Sets the rankingPlatformDirection1.
     * 
     * @param rankingPlatformDirection1
     *            the rankingPlatformDirection1 to set
     */
    public final void setRankingPlatformDirection1(final int rankingPlatformDirection1) {

        this.rankingPlatformDirection1 = rankingPlatformDirection1;
    }

    /**
     * Gets the rankingPlatformDirection2.
     * 
     * @return the rankingPlatformDirection2
     */
    public final int getRankingPlatformDirection2() {

        return rankingPlatformDirection2;
    }

    /**
     * Sets the rankingPlatformDirection2.
     * 
     * @param rankingPlatformDirection2
     *            the rankingPlatformDirection2 to set
     */
    public final void setRankingPlatformDirection2(final int rankingPlatformDirection2) {

        this.rankingPlatformDirection2 = rankingPlatformDirection2;
    }

    /**
     * Gets the floors.
     * 
     * @return the floors
     */
    public final int getFloors() {

        return floors;
    }

    /**
     * Sets the floors.
     * 
     * @param floors
     *            the floors to set
     */
    public final void setFloors(final int floors) {

        this.floors = floors;
    }

    /**
     * Gets the lastUpdate.
     * 
     * @return the lastUpdate
     */
    public final Date getLastUpdate() {

        return lastUpdate;
    }

    /**
     * Sets the lastUpdate.
     * 
     * @param lastUpdate
     *            the lastUpdate to set
     */
    public final void setLastUpdate(final Date lastUpdate) {

        this.lastUpdate = lastUpdate;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.beans.Station#getGeoPoint()
     */
    @Override
    public final GeoPoint getGeoPoint() {

        return new GeoPoint(latitude, longitude);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.beans.Station#getType()
     */
    @Override
    public final int getType() {

        return Station.TYPE_SUBWAY;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("SubwayStation [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", latitude=");
        builder.append(latitude);
        builder.append(", longitude=");
        builder.append(longitude);
        builder.append(", hasPlatformDirection1=");
        builder.append(hasPlatformDirection1);
        builder.append(", hasPlatformDirection2=");
        builder.append(hasPlatformDirection2);
        builder.append(", rankingPlatformDirection1=");
        builder.append(rankingPlatformDirection1);
        builder.append(", rankingPlatformDirection2=");
        builder.append(rankingPlatformDirection2);
        builder.append(", floors=");
        builder.append(floors);
        builder.append(", lastUpdate=");
        builder.append(lastUpdate);
        builder.append("]");
        return builder.toString();
    }

}
