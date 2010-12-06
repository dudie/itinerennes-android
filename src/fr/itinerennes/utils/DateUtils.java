package fr.itinerennes.utils;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

/**
 * Some utilities to deal with dates.
 * 
 * @author Jérémie Huchet
 */
public final class DateUtils {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(DateUtils.class);

    /**
     * Private constructor to avoid instantiation.
     */
    private DateUtils() {

    }

    /**
     * Gets a unix timestamp in seconds.
     * 
     * @return the amount of seconds since 1970-01-01
     */
    public static int currentTimeSeconds() {

        return (int) (System.currentTimeMillis() / 1000);
    }

    /**
     * Gets a unix timestamp in seconds from a {@link Date}.
     * 
     * @param date
     *            a date
     * @return the amount of seconds since 1970-01-01
     */
    public static int toSeconds(final Date date) {

        return (int) (date.getTime() / 1000);
    }
}
