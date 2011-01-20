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
    public static long currentTimeSeconds() {

        return (System.currentTimeMillis() / 1000);
    }

    /**
     * Gets a unix timestamp in seconds from a {@link Date}.
     * 
     * @param date
     *            a date
     * @return the amount of seconds since 1970-01-01
     */
    public static long toSeconds(final Date date) {

        return (date.getTime() / 1000);
    }

    /**
     * Is the given TTL expired at the given date for the current time.
     * 
     * @param date
     *            the date to test
     * @param ttl
     *            the time to live
     * @return true if the given date is expired
     */
    public static boolean isExpired(final Date date, final int ttl) {

        return isExpired((date.getTime() / 1000), ttl);
    }

    /**
     * Is the given TTL expired at the given date for the current time.
     * 
     * @param secondsTimestamp
     *            a unix timestamp in seconds
     * @param ttl
     *            the time to live
     * @return true if the given date is expired
     */
    public static boolean isExpired(final long secondsTimestamp, final int ttl) {

        return currentTimeSeconds() > secondsTimestamp + ttl;
    }
}
