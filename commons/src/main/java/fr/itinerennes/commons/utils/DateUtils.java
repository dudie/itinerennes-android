package fr.itinerennes.commons.utils;

/*
 * [license]
 * Common tools
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.util.Calendar;
import java.util.Date;

/**
 * Some utilities to deal with dates.
 * 
 * @author Jérémie Huchet
 */
public final class DateUtils {

    /** How many milliseconds in one second. */
    private static final int ONE_SECOND_IN_MILLISECONDS = 1000;

    /** How many milliseconds in one day. */
    private static final int ONE_DAY_IN_MILLISECONDS = 86400000;

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

        return (System.currentTimeMillis() / ONE_SECOND_IN_MILLISECONDS);
    }

    /**
     * Gets a unix timestamp in seconds from a {@link Date}.
     * 
     * @param date
     *            a date
     * @return the amount of seconds since 1970-01-01
     */
    public static long toSeconds(final Date date) {

        return (date.getTime() / ONE_SECOND_IN_MILLISECONDS);
    }

    /**
     * Gets a unix timestamp in seconds from a timestamp in milliseconds.
     * 
     * @param milliseconds
     *            the timestamp in milliseconds
     * @return seconds the timestamp in seconds
     */
    public static long toSeconds(final long milliseconds) {

        return milliseconds / ONE_SECOND_IN_MILLISECONDS;
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

        return isExpired((date.getTime() / ONE_SECOND_IN_MILLISECONDS), ttl);
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

    /**
     * Gets how long is the given time in days.
     * 
     * @param timeMillis
     *            a time in milliseconds
     * @return the day count the given time includes
     */
    public static int getDayCount(final long timeMillis) {

        return (int) (timeMillis / ONE_DAY_IN_MILLISECONDS);
    }

    /**
     * Add somes days to the given date.
     * 
     * @param date
     *            a date
     * @param days
     *            a number of days to add
     * @return the modified date
     */
    public static Date addDays(final Date date, final int days) {

        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);

        return c.getTime();

    }
}
