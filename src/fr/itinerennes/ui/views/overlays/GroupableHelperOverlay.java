package fr.itinerennes.ui.views.overlays;

import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.view.MotionEvent;

/**
 * A base overlay providing helper methods to filter base events.
 * 
 * @author Jérémie Huchet
 */
public abstract class GroupableHelperOverlay extends Overlay {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(GroupableHelperOverlay.class);

    /**
     * Creates the base helper overlay.
     * 
     * @param context
     *            the context
     */
    public GroupableHelperOverlay(final Context context) {

        super(context);
    }

    public final boolean canHandle(final MotionEvent event) {

        return true;
    }
}
