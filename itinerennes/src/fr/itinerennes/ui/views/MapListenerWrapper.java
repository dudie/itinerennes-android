package fr.itinerennes.ui.views;

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

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;

/**
 * A listener which propagates events to others.
 * 
 * @author Jérémie Huchet
 */
public class MapListenerWrapper implements MapListener {

    /** The listeners which wants to receive events. */
    private final ArrayList<MapListener> mapListeners;

    /**
     * Constructor.
     * 
     * @param nb
     *            Initialization size of the listeners list.
     */
    public MapListenerWrapper(final int nb) {

        mapListeners = new ArrayList<MapListener>(nb);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public final boolean onScroll(final ScrollEvent event) {

        for (final MapListener listener : mapListeners) {
            if (listener.onScroll(event)) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.events.MapListener#onZoom(org.andnav.osm.events.ZoomEvent)
     */
    @Override
    public final boolean onZoom(final ZoomEvent event) {

        for (final MapListener listener : mapListeners) {
            if (listener.onZoom(event)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a listener.
     * 
     * @param listener
     *            the listener to add
     */
    public final void add(final MapListener listener) {

        mapListeners.add(listener);
    }

    /**
     * Removes a listener.
     * 
     * @param listener
     *            the listener to remove
     */
    public final void remove(final MapListener listener) {

        mapListeners.remove(listener);
    }
}
