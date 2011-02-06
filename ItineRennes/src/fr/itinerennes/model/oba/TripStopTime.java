package fr.itinerennes.model.oba;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

/**
 * @author orgoz
 */
public class TripStopTime {

    /** Stop. */
    private Stop stop;

    /** Arrival time. */
    private Date arrivalTime;

    /** Departure time. */
    private Date departureTime;

    /** Distance along trip. */
    private Double distanceAlongTrip;

    /** Stop headsign. */
    private String stopHeadsign;

    /**
     * Gets the stop.
     * 
     * @return the stop
     */
    public Stop getStop() {

        return stop;
    }

    /**
     * Sets the stop.
     * 
     * @param stop
     *            the stop to set
     */
    public void setStop(final Stop stop) {

        this.stop = stop;
    }

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
     * Gets the distanceAlongTrip.
     * 
     * @return the distanceAlongTrip
     */
    public Double getDistanceAlongTrip() {

        return distanceAlongTrip;
    }

    /**
     * Sets the distanceAlongTrip.
     * 
     * @param distanceAlongTrip
     *            the distanceAlongTrip to set
     */
    public void setDistanceAlongTrip(final Double distanceAlongTrip) {

        this.distanceAlongTrip = distanceAlongTrip;
    }

    /**
     * Gets the stopHeadsign.
     * 
     * @return the stopHeadsign
     */
    public String getStopHeadsign() {

        return stopHeadsign;
    }

    /**
     * Sets the stopHeadsign.
     * 
     * @param stopHeadsign
     *            the stopHeadsign to set
     */
    public void setStopHeadsign(final String stopHeadsign) {

        this.stopHeadsign = stopHeadsign;
    }

}
