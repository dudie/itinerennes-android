package fr.itinerennes.model.oba;

/**
 * Class representing a OneBusAway route element.
 * 
 * @author Olivier Boudet
 */
public class Route {

    /** Id of the route. */
    private String id;

    /** Text color of the route. */
    private String textColor;

    /** Color of the route. */
    private String color;

    /** Description of the route. */
    private String description;

    /** Long name of the route. */
    private String longName;

    /** Short name of the route. */
    private String shortName;

    /** Type of the route. */
    private int type;

    /** Agency id of the route. */
    private String agencyId;

    /**
     * Gets the id.
     * 
     * @return the id
     */
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
     * Gets the textColor.
     * 
     * @return the textColor
     */
    public String getTextColor() {

        return textColor;
    }

    /**
     * Sets the textColor.
     * 
     * @param textColor
     *            the textColor to set
     */
    public void setTextColor(final String textColor) {

        this.textColor = textColor;
    }

    /**
     * Gets the color.
     * 
     * @return the color
     */
    public String getColor() {

        return color;
    }

    /**
     * Sets the color.
     * 
     * @param color
     *            the color to set
     */
    public void setColor(final String color) {

        this.color = color;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {

        return description;
    }

    /**
     * Sets the description.
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(final String description) {

        this.description = description;
    }

    /**
     * Gets the longName.
     * 
     * @return the longName
     */
    public String getLongName() {

        return longName;
    }

    /**
     * Sets the longName.
     * 
     * @param longName
     *            the longName to set
     */
    public void setLongName(final String longName) {

        this.longName = longName;
    }

    /**
     * Gets the shortName.
     * 
     * @return the shortName
     */
    public String getShortName() {

        return shortName;
    }

    /**
     * Sets the shortName.
     * 
     * @param shortName
     *            the shortName to set
     */
    public void setShortName(final String shortName) {

        this.shortName = shortName;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public int getType() {

        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the type to set
     */
    public void setType(final int type) {

        this.type = type;
    }

    /**
     * Gets the agencyId.
     * 
     * @return the agencyId
     */
    public String getAgencyId() {

        return agencyId;
    }

    /**
     * Sets the agencyId.
     * 
     * @param agencyId
     *            the agencyId to set
     */
    public void setAgencyId(final String agencyId) {

        this.agencyId = agencyId;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("Route [id=");
        builder.append(id);
        builder.append(", textColor=");
        builder.append(textColor);
        builder.append(", color=");
        builder.append(color);
        builder.append(", description=");
        builder.append(description);
        builder.append(", longName=");
        builder.append(longName);
        builder.append(", shortName=");
        builder.append(shortName);
        builder.append(", type=");
        builder.append(type);
        builder.append(", agencyId=");
        builder.append(agencyId);
        builder.append("]");
        return builder.toString();
    }

}
