package fr.itinerennes.model;

import java.util.Date;

/**
 * Class representing a bus departure.
 * 
 * @author Olivier Boudet
 */
public class BusDeparture {

    /** Id of the route. */
    private String routeId;

    /** Short name of the route. */
    private String routeShortName;

    /** Headsign of the trip. */
    private String headsign;

    /** Date of the departure. */
    private Date date;

    /** Trip id of this departure. */
    private String tripId;

    /**
     * Gets the route id.
     * 
     * @return id of the route
     */
    public final String getRouteId() {

        return routeId;
    }

    /**
     * Gets the route short name.
     * 
     * @return short name of the route
     */
    public final String getRouteShortName() {

        return routeShortName;
    }

    /**
     * Gets the date of the departure.
     * 
     * @return date of the departure
     */
    public final Date getDepartureDate() {

        return date;
    }

    /**
     * Gets the short name of the route.
     * 
     * @return short name of the route
     */
    public final String getHeadsign() {

        return headsign;
    }

    /**
     * Sets the route id.
     * 
     * @param id
     *            route id
     */
    public final void setRouteId(final String id) {

        this.routeId = id;
    }

    /**
     * Sets the route short name.
     * 
     * @param shortName
     *            route short name
     */
    public final void setRouteShortName(final String shortName) {

        this.routeShortName = shortName;
    }

    /**
     * Sets the date of the departure.
     * 
     * @param date
     *            date of the departure
     */
    public final void setDepartureDate(final Date date) {

        this.date = date;
    }

    /**
     * Sets the headsign.
     * 
     * @param headsign
     *            headsign for this trip
     */
    public final void setHeadsign(final String headsign) {

        this.headsign = headsign;
    }

    /**
     * Gets the tripId.
     * 
     * @return the tripId
     */
    public String getTripId() {

        return tripId;
    }

    /**
     * Sets the tripId.
     * 
     * @param tripId
     *            the tripId to set
     */
    public void setTripId(final String tripId) {

        this.tripId = tripId;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("BusDeparture [routeId=");
        builder.append(routeId);
        builder.append(", routeShortName=");
        builder.append(routeShortName);
        builder.append(", headsign=");
        builder.append(headsign);
        builder.append(", date=");
        builder.append(date);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append("]");
        return builder.toString();
    }

}
