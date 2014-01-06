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

import org.osmdroid.util.GeoPoint;

import android.database.Cursor;

import fr.itinerennes.database.Columns;

/**
 * Provides some useful methods to work with map.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public final class MapUtils {

    /**
     * Private constructor to avoid instantiation.
     */
    private MapUtils() {

    }

    /**
     * Decodes the given polyline string to a list of geopoints.
     * 
     * @param encoded
     *            an encoded polyline
     * @param points
     *            the amount of points the encoded polyline string contains (used to creates the
     *            geopoint list with the right size)
     * @return a list of geopoints
     */
    public static List<GeoPoint> decodePolyline(final String encoded, final int points) {

        final List<GeoPoint> poly = new ArrayList<GeoPoint>(points);
        int index = 0;
        final int len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            final int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            final int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            final GeoPoint p = new GeoPoint((int) ((lat / 1E5) * 1E6), (int) ((lng / 1E5) * 1E6));
            poly.add(p);
        }

        return poly;
    }

    /**
     * Decodes the given polyline string to a list of geopoints.
     * 
     * @param encoded
     *            an encoded polyline
     * @return a list of geopoints
     * @see #decodePolyline(String, int)
     */
    public static List<GeoPoint> decodePolyline(final String encoded) {

        return decodePolyline(encoded, 10);
    }

    /**
     * Calculates the barycentre of all markers in the given Cursor.
     * 
     * @param c
     *            cursor containing markers
     * @return the barycentre
     */
    public static GeoPoint getBarycenter(final Cursor c) {

        if (c != null && c.moveToFirst()) {

            int newLat = 0;
            int newLon = 0;

            while (!c.isAfterLast()) {
                newLat += c.getInt(c.getColumnIndex(Columns.MarkersColumns.LATITUDE));
                newLon += c.getInt(c.getColumnIndex(Columns.MarkersColumns.LONGITUDE));

                c.moveToNext();
            }
            newLat = newLat / c.getCount();
            newLon = newLon / c.getCount();

            return new GeoPoint(newLat, newLon);
        }
        return null;
    }
}
