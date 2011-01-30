package fr.itinerennes.model.oba;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Olivier Boudet
 */
public class StopSchedule {

    private Date date;

    private Stop stop;

    /** List of stop times for the route and the stop. */
    private final List<ScheduleStopTime> stopTimes;

    public StopSchedule() {

        stopTimes = new ArrayList<ScheduleStopTime>();
    }

    /**
     * Gets the date.
     * 
     * @return the date
     */
    public Date getDate() {

        return date;
    }

    /**
     * Sets the date.
     * 
     * @param date
     *            the date to set
     */
    public void setDate(final Date date) {

        this.date = date;
    }

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
     * Gets the stopTimes.
     * 
     * @return the stopTimes
     */
    public List<ScheduleStopTime> getStopTimes() {

        return stopTimes;
    }

    /*
     * (non-javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("StopSchedule [date=");
        builder.append(date);
        builder.append(", stop=");
        builder.append(stop);
        builder.append(", stopTimes=");
        builder.append(stopTimes);
        builder.append("]");
        return builder.toString();
    }

}
