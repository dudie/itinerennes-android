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

    public EventGarbageOverlay(final Context ctx) {

        super(ctx);
        // TJHU Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#onDraw(android.graphics.Canvas,
     *      org.osmdroid.views.MapView)
     */
    @Override
    protected void onDraw(final Canvas c, final MapView osmv) {

        // TJHU Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#onDrawFinished(android.graphics.Canvas,
     *      org.osmdroid.views.MapView)
     */
    @Override
    protected void onDrawFinished(final Canvas c, final MapView osmv) {

        // TJHU Auto-generated method stub

    }
}
