package fr.itinerennes.ui.views;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Simple component to display a MapBox on the map.
 * <p>
 * Must be used to set up a child view on a {@link MapView} to ensure a consistent event management.
 * See {@link #dispatchTouchEvent(MotionEvent)}.
 * 
 * @author Olivier Boudet
 */
public class MapBoxView extends LinearLayout {

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     */
    public MapBoxView(final Context context) {

        super(context);
    }

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     * @param attrs
     *            view attributes
     */
    public MapBoxView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
    }

    /**
     * Always return true to to tell parent view the event has been dispatched and handled by this
     * view. This mechanism is used with {@link MapView} {@link Overlay}s to avoid events happening
     * on views on top of the overlays to be dispatched to the overlays.
     * <p>
     * {@inheritDoc}
     * 
     * @return always true
     * @see android.view.View#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public final boolean dispatchTouchEvent(final MotionEvent event) {

        super.dispatchTouchEvent(event);
        return true;
    }

}
