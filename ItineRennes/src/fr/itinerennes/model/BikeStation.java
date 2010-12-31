package fr.itinerennes.model;

import java.util.Date;

import org.andnav.osm.util.GeoPoint;

import fr.itinerennes.R;

/**
 * Bean representing a bike station.
 * 
 * @author Jérémie Huchet
 */
public class BikeStation implements Station {

    /** The identifier of the station. */
    private String id;

    /** The name of the station. */
    private String name;

    /** The address of the station. */
    private String address;

    /** The state of the station. */
    private boolean active;

    /** The latitude of the station. */
    private int latitude;

    /** The longitude of the station. */
    private int longitude;

    /** The amount of available slots in the station. */
    private int availableSlots;

    /** The amount of available bikes in the station. */
    private int availableBikes;

    /** The district of the station. */
    private String district;

    /** The last update date of these informations. */
    private Date lastUpdate;

    /** True if this bike station is a the point of sale. */
    private boolean pos;

    /**
     * Gets the identifier of the station.
     * 
     * @return the identifier of the station
     */
    @Override
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
    @Override
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
     * Gets the address of the station.
     * 
     * @return the address of the station
     */
    public final String getAddress() {

        return address;
    }

    /**
     * Sets the address of the station.
     * 
     * @param address
     *            the address of the station to set
     */
    public final void setAddress(final String address) {

        this.address = address;
    }

    /**
     * Gets the state of the station.
     * 
     * @return the state of the station
     */
    public final boolean isActive() {

        return active;
    }

    /**
     * Sets the state of the station.
     * 
     * @param active
     *            the state of the station to set
     */
    public final void setActive(final boolean active) {

        this.active = active;
    }

    /**
     * Gets the latitude of the station.
     * 
     * @return the latitude of the station
     */
    public final int getLatitude() {

        return latitude;
    }

    /**
     * Sets the latitude of the station.
     * 
     * @param latitude
     *            the latitude of the station to set
     */
    public final void setLatitude(final int latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the station.
     * 
     * @return the longitude of the station
     */
    public final int getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude of the station.
     * 
     * @param longitude
     *            the longitude of the station to set
     */
    public final void setLongitude(final int longitude) {

        this.longitude = longitude;
    }

    /**
     * Gets the amount of available slots in the station.
     * 
     * @return the amount of available slots in the station
     */
    public final int getAvailableSlots() {

        return availableSlots;
    }

    /**
     * Sets the amount of available slots in the station.
     * 
     * @param availableSlots
     *            the amount of available slots in the station to set
     */
    public final void setAvailableSlots(final int availableSlots) {

        this.availableSlots = availableSlots;
    }

    /**
     * Gets the amount of available bikes in the station.
     * 
     * @return the amount of available bikes in the station
     */
    public final int getAvailableBikes() {

        return availableBikes;
    }

    /**
     * Sets the amount of available bikes in the station.
     * 
     * @param availableBikes
     *            the amount of available bikes in the station to set
     */
    public final void setAvailableBikes(final int availableBikes) {

        this.availableBikes = availableBikes;
    }

    /**
     * Gets the district of the station.
     * 
     * @return the district of the station
     */
    public final String getDistrict() {

        return district;
    }

    /**
     * Sets the district of the station.
     * 
     * @param district
     *            the district of the station to set
     */
    public final void setDistrict(final String district) {

        this.district = district;
    }

    /**
     * Gets the last update date of these informations.
     * 
     * @return the last update date of these informations
     */
    public final Date getLastUpdate() {

        return lastUpdate;
    }

    /**
     * Sets the last update date of these informations.
     * 
     * @param lastUpdate
     *            the last update date of these informations to set
     */
    public final void setLastUpdate(final Date lastUpdate) {

        this.lastUpdate = lastUpdate;
    }

    /**
     * Returns true if this bike station is a the point of sale.
     * 
     * @return true if this bike station is a the point of sale else false
     */
    public final boolean isPos() {

        return pos;
    }

    /**
     * Sets if this bike station is a the point of sale.
     * 
     * @param pos
     *            true if this bike station is a the point of sale
     */
    public final void setPos(final boolean pos) {

        this.pos = pos;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.model.Station#getGeoPoint()
     */
    @Override
    public final GeoPoint getGeoPoint() {

        // TJHU ne faut-il pas plutot retirer this.latitude et this.longitude pour mettre un
        // this.geopoint ?
        return new GeoPoint(this.latitude, this.longitude);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.model.Station#getType()
     */
    @Override
    public final int getType() {

        return Station.TYPE_BIKE;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("BikeStation [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", address=");
        builder.append(address);
        builder.append(", active=");
        builder.append(active);
        builder.append(", latitude=");
        builder.append(latitude);
        builder.append(", longitude=");
        builder.append(longitude);
        builder.append(", availableSlots=");
        builder.append(availableSlots);
        builder.append(", availableBikes=");
        builder.append(availableBikes);
        builder.append(", district=");
        builder.append(district);
        builder.append(", lastUpdate=");
        builder.append(lastUpdate);
        builder.append(", pos=");
        builder.append(pos);
        builder.append("]");
        return builder.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.model.Station#getIconDrawableId()
     */
    @Override
    public int getIconDrawableId() {

        return R.drawable.bike_marker_icon;
    }
}
