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
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link DateUtils}.
 * 
 * @author Jérémie Huchet
 */
public final class DateUtilsTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtilsTest.class);

    @Test
    public void testCurrentTimeSeconds() {

        final long result = DateUtils.currentTimeSeconds();
        final long currentTime = System.currentTimeMillis() / 1000;

        assertTrue("the given time must be in seconds", result <= currentTime);
    }

    @Test
    public void testToSecondsDate() {

        final long currentTimeSec = DateUtils.currentTimeSeconds();
        final Date date = new Date(currentTimeSec * 1000);
        final long result = DateUtils.toSeconds(date);

        assertEquals(currentTimeSec, result);
    }

    @Test
    public void testToSecondsLong() {

        final long currentTimeLong = System.currentTimeMillis();
        final long result = DateUtils.toSeconds(currentTimeLong);

        assertEquals(currentTimeLong / 1000, result);
    }

    @Test
    public void testGetDayCount() {

        assertEquals("getDayCount(0ms)", 0, DateUtils.getDayCount(0));

        assertEquals("getDayCount(1ms)", 0, DateUtils.getDayCount(1));

        assertEquals("getDayCount(1day)", 1, DateUtils.getDayCount(1000 * 60 * 60 * 24));

        assertEquals("getDayCount(2days)", 2, DateUtils.getDayCount(1000 * 60 * 60 * 24 * 2));

        assertEquals("getDayCount(1day + 1ms)", 1, DateUtils.getDayCount(1000 * 60 * 60 * 24 + 1));

    }

    @Test
    public void testAddDays() {

        Date d1;
        Date d2;
        final Calendar c = Calendar.getInstance();

        c.set(2010, 11, 31);
        d1 = c.getTime();
        c.set(2011, 0, 1);
        d2 = c.getTime();
        assertEquals("addDays(-1)", d1, DateUtils.addDays(d2, -1));

        c.set(2011, 0, 31);
        d1 = c.getTime();
        c.set(2011, 1, 1);
        d2 = c.getTime();
        assertEquals("addDays(+1)", d2, DateUtils.addDays(d1, 1));

        c.set(2021, 5, 15);
        d1 = c.getTime();
        c.set(2021, 6, 15);
        d2 = c.getTime();
        assertEquals("addDays(+30)", d2, DateUtils.addDays(d1, 30));

        c.set(2011, 1, 28);
        d1 = c.getTime();
        c.set(2011, 2, 1);
        d2 = c.getTime();
        assertEquals("addDays(+1 année non bisexctile)", d2, DateUtils.addDays(d1, 1));

        c.set(2004, 1, 28);
        d1 = c.getTime();
        c.set(2004, 1, 29);
        d2 = c.getTime();
        assertEquals("addDays(+1 année bisexctile)", d2, DateUtils.addDays(d1, 1));

        c.set(2004, 1, 29);
        d1 = c.getTime();
        c.set(2004, 2, 1);
        d2 = c.getTime();
        assertEquals("addDays(+1 année bisexctile)", d2, DateUtils.addDays(d1, 1));
    }
}
