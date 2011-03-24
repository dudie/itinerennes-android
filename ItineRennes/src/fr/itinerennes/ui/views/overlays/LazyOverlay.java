package fr.itinerennes.ui.views.overlays;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

/**
 * An overlay listening to map events to fill itself.
 * 
 * @author Jérémie Huchet
 */
public abstract class LazyOverlay extends Overlay implements MapListener {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LazyOverlay.class);

    public LazyOverlay(final Context ctx) {

        super(ctx);
        // TJHU Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.events.MapListener#onScroll(org.osmdroid.events.ScrollEvent)
     */
    @Override
    public final boolean onScroll(final ScrollEvent e) {

        onMapMove(e.getSource());
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
        return false;
    }

    /**
     * Triggered when the map is scrolled or moved.
     * 
     * @param source
     *            the map view which generated this event
     */
    protected abstract void onMapMove(MapView source);
}
