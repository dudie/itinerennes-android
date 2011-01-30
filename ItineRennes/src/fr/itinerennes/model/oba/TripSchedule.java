package fr.itinerennes.model.oba;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a trip schedule.
 * 
 * @author Olivier Boudet
 */
public class TripSchedule {

    /** List of stop times. */
    private final List<TripStopTime> stopTimes;

    /** Id of the previous trip; */
    private String previousTripId;

    /** Id of the next trip. */
    private String nextTripId;

    public TripSchedule() {

        stopTimes = new ArrayList<TripStopTime>();
    }

    /**
     * Gets the stopTimes.
     * 
     * @return the stopTimes
     */
    public List<TripStopTime> getStopTimes() {

        return stopTimes;
    }

    /**
     * Gets the previousTripId.
     * 
     * @return the previousTripId
     */
    public String getPreviousTripId() {

        return previousTripId;
    }

    /**
     * Sets the previousTripId.
     * 
     * @param previousTripId
     *            the previousTripId to set
     */
    public void setPreviousTripId(final String previousTripId) {

        this.previousTripId = previousTripId;
    }

    /**
     * Gets the nextTripId.
     * 
     * @return the nextTripId
     */
    public String getNextTripId() {

        return nextTripId;
    }

    /**
     * Sets the nextTripId.
     * 
     * @param nextTripId
     *            the nextTripId to set
     */
    public void setNextTripId(final String nextTripId) {

        this.nextTripId = nextTripId;
    }

}
