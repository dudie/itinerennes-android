package fr.itinerennes.model;

/**
 * Represents a bike district.
 * 
 * @author Jérémie Huchet
 */
public class BikeDistrict {

    /** The identifier of the district. */
    private String id;

    /** The name of the district. */
    private String name;

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
     * Gets the name.
     * 
     * @return the name
     */
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
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("BikeDistrict [id=");
        builder.append(id);
        builder.append(", name=");
        builder.append(name);
        builder.append("]");
        return builder.toString();
    }

}
