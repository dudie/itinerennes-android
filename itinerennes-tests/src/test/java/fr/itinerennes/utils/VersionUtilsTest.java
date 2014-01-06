package fr.itinerennes.utils;

/*
 * [license]
 * Java-based tests
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

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import com.xtremelabs.robolectric.Robolectric;

import fr.itinerennes.test.ItineRennesRobolelectricTestRunner;

/**
 * {@link VersionUtils} unit tests.
 * 
 * @author Jérémie Huchet
 */
@RunWith(ItineRennesRobolelectricTestRunner.class)
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
