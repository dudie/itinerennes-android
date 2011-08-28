package fr.itinerennes.ui.views.overlays;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Canvas;

/**
 * @author Jérémie Huchet
 */
public class EventGarbageOverlay extends Overlay {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EventGarbageOverlay.class);

    /**
     * Constructor.
     * 
     * @param ctx
     *            the context
     */
    public EventGarbageOverlay(final Context ctx) {

        super(ctx);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#draw(android.graphics.Canvas,
     *      org.osmdroid.views.MapView, boolean)
     */
    @Override
    protected void draw(final Canvas c, final MapView osmView, final boolean shadow) {

        // TJHU Auto-generated method stub

    }
}
