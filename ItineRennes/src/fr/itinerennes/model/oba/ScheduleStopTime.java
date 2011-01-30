package fr.itinerennes.model.oba;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

/**
 * @author orgoz
 */
public class ScheduleStopTime implements Comparable<ScheduleStopTime> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(ScheduleStopTime.class);

    /** Arrival time. */
    private Date arrivalTime;

    /** Departure time. */
    private Date departureTime;

    /** Service id. */
    private String serviceId;

    /** Trip id. */
    private String tripId;

    /** Route. */
    private Route route;

    private String headsign;

    /**
     * Gets the arrivalTime.
     * 
     * @return the arrivalTime
     */
    public Date getArrivalTime() {

        return arrivalTime;
    }

    /**
     * Sets the arrivalTime.
     * 
     * @param arrivalTime
     *            the arrivalTime to set
     */
    public void setArrivalTime(final Date arrivalTime) {

        this.arrivalTime = arrivalTime;
    }

    /**
     * Gets the departureTime.
     * 
     * @return the departureTime
     */
    public Date getDepartureTime() {

        return departureTime;
    }

    /**
     * Sets the departureTime.
     * 
     * @param departureTime
     *            the departureTime to set
     */
    public void setDepartureTime(final Date departureTime) {

        this.departureTime = departureTime;
    }

    /**
     * Gets the serviceId.
     * 
     * @return the serviceId
     */
    public String getServiceId() {

        return serviceId;
    }

    /**
     * Sets the serviceId.
     * 
     * @param serviceId
     *            the serviceId to set
     */
    public void setServiceId(final String serviceId) {

        this.serviceId = serviceId;
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

    /**
     * Gets the route.
     * 
     * @return the route
     */
    public Route getRoute() {

        return route;
    }

    /**
     * Sets the route.
     * 
     * @param route
     *            the route to set
     */
    public void setRoute(final Route route) {

        this.route = route;
    }

    /**
     * Gets the headsign.
     * 
     * @return the headsign
     */
    public String getHeadsign() {

        return headsign;
    }

    /**
     * Gets the route headsign truncated to not display the route shortname.
     * 
     * @return the route headsign truncated to not display the route shortname
     */
    public final String getSimpleHeadsign() {

        return headsign.replaceFirst("^.* \\| ", "");
    }

    /**
     * Sets the headsign.
     * 
     * @param headsign
     *            the headsign to set
     */
    public void setHeadsign(final String headsign) {

        this.headsign = headsign;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(final ScheduleStopTime scheduleStopTime2) {

        return getDepartureTime().compareTo(scheduleStopTime2.getDepartureTime());
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("ScheduleStopTime [arrivalTime=");
        builder.append(arrivalTime);
        builder.append(", departureTime=");
        builder.append(departureTime);
        builder.append(", serviceId=");
        builder.append(serviceId);
        builder.append(", tripId=");
        builder.append(tripId);
        builder.append(", routeId=");
        builder.append(route);
        builder.append("]");
        return builder.toString();
    }

}
