package fr.itinerennes.ui.views.overlays;

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

import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.PathOverlay;

import android.content.Context;

import fr.itinerennes.utils.MapUtils;

/**
 * Overlay displaying polylines.
 * 
 * @author Jérémie Huchet
 */
public class PolylineOverlay extends PathOverlay {

    /**
     * Constructor.
     * 
     * @param color
     *            color of the line
     * @param context
     *            the context
     */
    public PolylineOverlay(final int color, final Context context) {

        super(color, context);
    }

    /**
     * Sets the polyline to display.
     * 
     * @param encoded
     *            the polyline
     */
    public final void setPolyline(final String encoded) {

        final List<GeoPoint> points = MapUtils.decodePolyline(encoded);
        for (final GeoPoint point : points) {
            this.addPoint(point);
        }
    }

}
