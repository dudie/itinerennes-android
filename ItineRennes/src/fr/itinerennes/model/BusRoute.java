package fr.itinerennes.model;

/**
 * Class representing a bus route.
 * 
 * @author Olivier Boudet
 */
public class BusRoute implements Route {

    /** Id of the route. */
    private String id;

    /** Short name of the route. */
    private String shortName;

    /** Long name of the route. */
    private String longName;

    /** Id of the agency of the route. */
    private String agencyId;

    /**
     * Gets the agency id of the route.
     * 
     * @return id of the agency
     */
    public final String getAgencyId() {

        return agencyId;
    }

    /**
     * Gets the route id.
     * 
     * @return id of the route
     */
    @Override
    public final String getId() {

        return id;
    }

    /**
     * Gets the long name of the route.
     * 
     * @return long name of the route
     */
    public final String getLongName() {

        return longName;
    }

    /**
     * Gets the short name of the route.
     * 
     * @return short name of the route
     */
    public final String getShortName() {

        return shortName;
    }

    /**
     * Sets the agency id of the route.
     * 
     * @param agencyId
     */
    public final void setAgencyId(final String agencyId) {

        this.agencyId = agencyId;
    }

    /**
     * Sets the route id.
     * 
     * @param id
     *            route id
     */
    public final void setId(final String id) {

        this.id = id;
    }

    /**
     * Sets the long name of the route.
     * 
     * @param longName
     *            long name of the route
     */
    public final void setLongName(final String longName) {

        this.longName = longName;
    }

    /**
     * Sets the short name of the route.
     * 
     * @param shortName
     *            short name of the route
     */
    public final void setShortName(final String shortName) {

        this.shortName = shortName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("BusRoute [id=");
        builder.append(id);
        builder.append(", shortName=");
        builder.append(shortName);
        builder.append(", longName=");
        builder.append(longName);
        builder.append(", agencyId=");
        builder.append(agencyId);
        builder.append("]");
        return builder.toString();
    }

}
