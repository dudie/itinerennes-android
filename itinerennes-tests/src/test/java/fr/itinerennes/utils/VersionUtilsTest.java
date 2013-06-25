package fr.itinerennes.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

/**
 * {@link VersionUtils} unit tests.
 * 
 * @author Jérémie Huchet
 */
@RunWith(RobolectricTestRunner.class)
public final class VersionUtilsTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionUtilsTest.class);

    /**
     * Ensure the version returned by {@link VersionUtils#getCurrent(Context)} is not null.
     */
    @Test
    public void testGetVersion() {

        final Context ctx = Robolectric.application;
        final String currentVersion = VersionUtils.getCurrent(ctx);
        LOGGER.info("Current Version: {}", currentVersion);
        assertNotNull("a version label must be returned", currentVersion);
    }

    /**
     * Ensure the version code returned by {@link VersionUtils#getCode(Context)} is not null.
     */
    @Test
    public void testGetCode() {

        final Context ctx = Robolectric.application;
        final int currentVersionCode = VersionUtils.getCode(ctx);
        LOGGER.info("Current Version: {}", currentVersionCode);
        assertEquals("development version code is 0", 0, currentVersionCode);
    }

    /**
     * Check {@link VersionUtils#compare(String, String)} behavior with null values.
     */
    @Test
    public void testCompareWithNull() {

        assertEquals(0, VersionUtils.compare(null, null));
        assertEquals(0, VersionUtils.compare(null, ""));
        assertEquals(0, VersionUtils.compare("", null));
        assertTrue(0 > VersionUtils.compare(null, "2.1"));
        assertTrue(0 < VersionUtils.compare("2.1", null));
    }

    /**
     * Check {@link VersionUtils#compare(String, String)} behavior with non standard version name
     * such as "snapshot" or "none".
     */
    @Test
    public void testCompare() {

        // standard version names
        assertEquals(0, VersionUtils.compare("2.1", "2.1"));
        assertTrue(0 > VersionUtils.compare("1.2", "2.1"));
        assertTrue(0 < VersionUtils.compare("2.1", "1.2"));

        // "none" is considered as the newest version
        assertEquals(0, VersionUtils.compare("none", "none"));
        assertTrue(0 > VersionUtils.compare("2.1", "none"));
        assertTrue(0 < VersionUtils.compare("none", "2.1"));
    }
}
