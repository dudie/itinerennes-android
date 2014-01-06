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

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import android.content.Context;

/**
 * An overlay listening to map events to fill itself.
 * 
 * @author Jérémie Huchet
 */
public abstract class LazyOverlay extends Overlay implements MapListener {

    /**
     * Constructor.
     * 
     * @param ctx
     *            the context
     */
    public LazyOverlay(final Context ctx) {

        super(ctx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.events.MapListener#onScroll(org.osmdroid.events.ScrollEvent)
     */
    @Override
    public final boolean onScroll(final ScrollEvent e) {

        onMapMove(e.getSource());
        e.getSource().postInvalidate();
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.events.MapListener#onZoom(org.osmdroid.events.ZoomEvent)
     */
    @Override
    public final boolean onZoom(final ZoomEvent e) {

        onMapMove(e.getSource());
        e.getSource().postInvalidate();
        return false;
    }

    /**
     * Triggered when the map is scrolled or moved.
     * 
     * @param source
     *            the map view which generated this event
     */
    public abstract void onMapMove(MapView source);
}
