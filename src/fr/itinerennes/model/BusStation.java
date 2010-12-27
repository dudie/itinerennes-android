package fr.itinerennes.model;

import org.andnav.osm.util.GeoPoint;

import fr.itinerennes.R;

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
    private int latitude;

    /** The longitude of the station. */
    private int longitude;

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
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.model.Station#getGeoPoint()
     */
    @Override
    public final GeoPoint getGeoPoint() {

        return new GeoPoint(this.latitude, this.longitude);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.model.Station#getType()
     */
    @Override
    public final int getType() {

        return Station.TYPE_BUS;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("BusStation [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append(", latitude=");
        builder.append(latitude);
        builder.append(", longitude=");
        builder.append(longitude);
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

        return R.drawable.icon_bus;
    }
}
