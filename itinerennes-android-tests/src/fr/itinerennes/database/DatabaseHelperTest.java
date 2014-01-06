package fr.itinerennes.database;

/*
 * [license]
 * Instrumentation tests
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

/**
 * Test class for {@link DatabaseHelper}.
 * 
 * @author Jérémie Huchet
 */
public class DatabaseHelperTest extends AndroidTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelperTest.class);

    /**
     * Test method for {@link DatabaseHelper#getReadableDatabase()}.
     */
    public final void testGetReadableDatabase() {

        LOGGER.info("testOpenDatabase.start");
        final DatabaseHelper dbHlpr = new DatabaseHelper(this.getContext());
        dbHlpr.getReadableDatabase();
        LOGGER.info("testOpenDatabase.end");
    }
}
