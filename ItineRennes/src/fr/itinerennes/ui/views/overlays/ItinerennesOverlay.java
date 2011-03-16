package fr.itinerennes.ui.views.overlays;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.view.MotionEvent;

/**
 * @author Jérémie Huchet
 */
public abstract class ItinerennesOverlay extends Overlay {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItinerennesOverlay.class);

    public ItinerennesOverlay(final Context ctx) {

        super(ctx);
        // TJHU Auto-generated constructor stub
    }

    public abstract void onUnselect(MotionEvent e, MapView view);
}
