package fr.itinerennes.utils;

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

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.MatrixCursor;

import fr.dudie.nominatim.model.Address;

import fr.itinerennes.database.Columns.NominatimColumns;

/**
 * Translates nominatim {@link Address}es to {@link Cursor}.
 * 
 * @author Jérémie Huchet
 */
public final class NominatimTranslator implements NominatimColumns {

    /**
     * Private constructor to avoid instantiation.
     */
    private NominatimTranslator() {

    }

    /**
     * Gets an ampty cursor of nominatim results.
     * 
     * @return an ampty cursor of nominatim results
     */
    public static Cursor emptyCursor() {

        return toCursor(new ArrayList<Address>(0));
    }

    /**
     * Translates a list of addresses to a cursor.
     * 
     * @param addresses
     *            a list of {@link Address} objects
     * @return a cursor
     */
    public static Cursor toCursor(final List<Address> addresses) {

        final String[] cols = new String[] { _ID, DISPLAY_NAME, LONGITUDE, LATITUDE };
        final MatrixCursor c = new MatrixCursor(cols, addresses.size());

        int id = 0;
        for (final Address address : addresses) {
            c.addRow(toColumnValues(id++, address));
        }

        return c;
    }

    /**
     * Translates an address to an array of cursor row values.
     * 
     * @param id
     *            a unique id (will be mapped to <code>_ID</code>)
     * @param address
     *            the address to translate
     * @return an array of values
     */
    private static Object[] toColumnValues(final int id, final Address address) {

        final Object[] values = new Object[4];
        values[0] = id;
        values[1] = address.getDisplayName();
        values[2] = address.getLongitudeE6();
        values[3] = address.getLatitudeE6();
        return values;
    }
}
