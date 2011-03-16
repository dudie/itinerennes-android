package fr.itinerennes.ui.views.overlays;

import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

/**
 * @author Jérémie Huchet
 */
public abstract class LazyOverlay extends Overlay {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LazyOverlay.class);

    public LazyOverlay(final Context ctx) {

        super(ctx);
        // TJHU Auto-generated constructor stub
    }

    public abstract void onZoom();

    public abstract void onScroll();
}
