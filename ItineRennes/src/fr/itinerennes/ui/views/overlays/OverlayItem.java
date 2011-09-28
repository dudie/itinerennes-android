package fr.itinerennes.ui.views.overlays;

import java.io.Serializable;

import org.osmdroid.util.GeoPoint;

/**
 * @author Olivier Boudet
 */
public class OverlayItem implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 570074780221962142L;

    /** The type of the item. */
    private String type;

    /** The marker position. */
    private GeoPoint location;

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public final String getType() {

        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the type to set
     */
    public final void setType(final String type) {

        this.type = type;
    }

    /**
     * Gets the location.
     * 
     * @return the location
     */
    public final GeoPoint getLocation() {

        return location;
    }

    /**
     * Sets the location.
     * 
     * @param location
     *            the location to set
     */
    public final void setLocation(final GeoPoint location) {

        this.location = location;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("OverlayItem [type=");
        builder.append(type);
        builder.append(", location=");
        builder.append(location);
        builder.append("]");
        return builder.toString();
    }

}
