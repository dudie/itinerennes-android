package fr.itinerennes.model;

/**
 * Represents a bookmark.
 * 
 * @author Jérémie Huchet
 */
public class Bookmark {

    /** The identifier of the bookmarked resource. */
    private String id;

    /** The type of the bookmarked resource, like "fr.itinerennes.model.BusStation". */
    private String type;

    /** The label of the bookmark. */
    private String label;

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
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("Bookmark [id=");
        builder.append(id);
        builder.append(", type=");
        builder.append(type);
        builder.append(", label=");
        builder.append(label);
        builder.append("]");
        return builder.toString();
    }

}
