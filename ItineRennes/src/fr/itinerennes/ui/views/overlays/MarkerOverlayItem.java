package fr.itinerennes.ui.views.overlays;

import org.osmdroid.util.GeoPoint;

import android.graphics.drawable.Drawable;

/**
 * Represents a marker on the map.
 * 
 * @author Jérémie Huchet
 */
public class MarkerOverlayItem {

    /** The type of the marker. */
    private String type;

    /** The marker identifier. */
    private String id;

    /** The marker title. */
    private String label;

    /** The marker icon. */
    private Drawable icon;

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
     * Gets the id.
     * 
     * @return the id
     */
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
     * Gets the label.
     * 
     * @return the label
     */
    public final String getLabel() {

        return label;
    }

    /**
     * Sets the label.
     * 
     * @param label
     *            the label to set
     */
    public final void setLabel(final String label) {

        this.label = label;
    }

    /**
     * Gets the icon.
     * 
     * @return the icon
     */
    public final Drawable getIcon() {

        return icon;
    }

    /**
     * Sets the icon.
     * 
     * @param icon
     *            the icon to set
     */
    public final void setIcon(final Drawable icon) {

        this.icon = icon;
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
}