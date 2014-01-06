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

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link DateUtils}.
 * 
 * @author Jérémie Huchet
 */
@RunWith(Parameterized.class)
public final class DateUtilsTestIsExpired {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtilsTestIsExpired.class);

    /** The date. */
    private final Date date;

    /** The time to live. */
    private final int ttl;

    /** The expected result. */
    private final boolean expectedResult;

    /**
     * Test constructor.
     * 
     * @param date
     *            the date
     * @param ttl
     *            the ttl
     * @param expectedResult
     *            the expected result
     */
    public DateUtilsTestIsExpired(final Date date, final int ttl, final boolean expectedResult) {

        this.date = date;
        this.ttl = ttl;
        this.expectedResult = expectedResult;
    }

    @Parameters
    public static List<Object[]> data() {

        return Arrays.asList(new Object[][] { { getDate(-2000), 1000, true },
                { getDate(0), Integer.MAX_VALUE, false }, { getDate(-1000), 10, true } });
    }

    private static Date getDate(final long ms) {

        return new Date(System.currentTimeMillis() + ms * 1000);
    }

    @Test
    public void testIsExpiredDateInt() {

        final String message;
        if (expectedResult) {
            message = "Date %s should be expired for a ttl=%s";
        } else {
            message = "Date %s shouldn't be expired for a ttl=%s";
        }
        assertEquals(String.format(message, date, ttl), expectedResult,
                DateUtils.isExpired(date, ttl));
    }

    @Test
    public void testIsExpiredLongInt() {

        final String message;
        if (expectedResult) {
            message = "Date %s should be expired for a ttl=%s";
        } else {
            message = "Date %s shouldn't be expired for a ttl=%s";
        }
        assertEquals(String.format(message, date, ttl), expectedResult,
                DateUtils.isExpired(date.getTime() / 1000, ttl));
    }

}
