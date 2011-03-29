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

    /** Is this marker bookmarked ? */
    private boolean bookmarked;

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

    /**
     * Gets the bookmarked.
     * 
     * @return the bookmarked
     */
    public boolean isBookmarked() {

        return bookmarked;
    }

    /**
     * Sets the bookmarked.
     * 
     * @param bookmarked
     *            the bookmarked to set
     */
    public void setBookmarked(final boolean bookmarked) {

        this.bookmarked = bookmarked;
    }

    /**
     * (non-javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("MarkerOverlayItem [type=");
        builder.append(type);
        builder.append(", id=");
        builder.append(id);
        builder.append(", label=");
        builder.append(label);
        builder.append(", icon=");
        builder.append(icon);
        builder.append(", location=");
        builder.append(location);
        builder.append(", bookmarked=");
        builder.append(bookmarked);
        builder.append("]");
        return builder.toString();
    }

}
