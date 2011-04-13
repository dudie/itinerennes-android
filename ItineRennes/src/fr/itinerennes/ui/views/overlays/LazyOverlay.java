package fr.itinerennes.ui.views.overlays;

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
