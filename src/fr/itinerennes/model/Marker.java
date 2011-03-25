package fr.itinerennes.model;

import org.osmdroid.util.GeoPoint;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;

/**
 * Bean representing a marker.
 * 
 * @author Olivier Boudet
 */
public class Marker {

    /** The identifier of the marker. */
    private String id;

    /** The type of the marker. */
    private String type;

    /** The label of the marker. */
    private String label;

    /** The latitude of the marker. */
    private int latitude;

    /** The longitude of the marker. */
    private int longitude;

    /**
     * Gets the identifier of the marker.
     * 
     * @return the identifier of the marker
     */
    public final String getId() {

        return id;
    }

    /**
     * Sets the identifier of the marker.
     * 
     * @param id
     *            the identifier of the marker to set
     */
    public final void setId(final String id) {

        this.id = id;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public String getType() {

        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the type to set
     */
    public void setType(final String type) {

        this.type = type;
    }

    /**
     * Gets the name of the marker.
     * 
     * @return the name of the marker
     */
    public final String getLabel() {

        return label;
    }

    /**
     * Sets the label of the marker.
     * 
     * @param label
     *            the label of the marker to set
     */
    public final void setName(final String label) {

        this.label = label;
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
     * Sets the latitude of the marker.
     * 
     * @param latitude
     *            the latitude of the marker to set
     */
    public final void setLatitude(final int latitude) {

        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the marker.
     * 
     * @return the longitude of the marker
     */
    public final int getLongitude() {

        return longitude;
    }

    /**
     * Sets the longitude of the marker.
     * 
     * @param longitude
     *            the longitude of the marker to set
     */
    public final void setLongitude(final int longitude) {

        this.longitude = longitude;
    }

    /**
     * Gets the geographical localization of the station.
     * 
     * @return the geographical localization of the station
     */
    public final GeoPoint getGeoPoint() {

        return new GeoPoint(this.latitude, this.longitude);
    }

    /**
     * Gets the id of the drawable icon
     * 
     * @return id of the drawable to use to draw the marker icon on the map
     */
    public int getIconDrawableId() {

        // TOBO est ce que ca n'aurait pas une meilleure place qu'ici ?
        if (type.equals(ItineRennesConstants.MARKER_TYPE_BIKE)) {
            return R.drawable.bike_marker_icon_focusable;
        } else if (type.equals(ItineRennesConstants.MARKER_TYPE_BUS)) {
            return R.drawable.bus_marker_icon_focusable;
        } else if (type.equals(ItineRennesConstants.MARKER_TYPE_SUBWAY)) {
            return R.drawable.subway_marker_icon_focusable;
        }

        return 0;

    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("Marker [id=");
        builder.append(id);
        builder.append(", label=");
        builder.append(label);
        builder.append(", latitude=");
        builder.append(latitude);
        builder.append(", longitude=");
        builder.append(longitude);
        builder.append("]");
        return builder.toString();
    }

}
