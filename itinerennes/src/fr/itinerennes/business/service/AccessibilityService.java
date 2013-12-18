package fr.itinerennes.business.service;

/*
 * [license]
 * ItineRennes
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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;

import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.DatabaseHelper;

/**
 * A service to fetch accessibility attributes from database.
 * 
 * @author Olivier Boudet
 */
@EBean
public class AccessibilityService implements AccessibilityColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessibilityService.class);

    /** The database helper. */
    @Bean
    DatabaseHelper dbHelper;

    /**
     * Returns whether or not a resource is accessible for wheelchairs.
     * 
     * @param id
     *            the identifier of the resource
     * @param type
     *            type of the resource
     * @return true if the resource is accessible or false
     */
    public final boolean isAccessible(final String id, final String type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isAccessible.start - id={}, type={}", id, type);
        }
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final String selection = String
                .format("%s = ? AND %s = ? AND %s = ?", ID, TYPE, WHEELCHAIR);
        final String[] selectionArgs = new String[] { id, type, "1" };
        final Cursor c = database.query(ACCESSIBILITY_TABLE_NAME, new String[] { _ID }, selection,
                selectionArgs, null, null, null);

        final boolean hasResult = c.moveToNext();
        c.close();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isAccessible.end - isAccessible={}", hasResult);
        }
        return hasResult;
    }

}
