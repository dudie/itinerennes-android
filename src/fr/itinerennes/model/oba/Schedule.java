package fr.itinerennes.model.oba;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a trip schedule.
 * 
 * @author Olivier Boudet
 */
public class Schedule {

    private final List<StopTime> stopTimes;

    private String previousTripId;

    private String nextTripId;

    public Schedule() {

        stopTimes = new ArrayList<StopTime>();
    }

    /**
     * Gets the stopTimes.
     * 
     * @return the stopTimes
     */
    public List<StopTime> getStopTimes() {

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
