package fr.itinerennes.ui.views;

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

    public final void add(final MapListener listener) {

        mapListeners.add(listener);
    }

    public final void remove(final MapListener listener) {

        mapListeners.remove(listener);
    }
}
