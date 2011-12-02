package fr.itinerennes.commons.utils;

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
