package fr.itinerennes.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Simple component to display a MapBox on the map.
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
     * {@inheritDoc}
     * 
     * @see android.view.View#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public final boolean dispatchTouchEvent(final MotionEvent event) {

        super.dispatchTouchEvent(event);
        return true;
    }

}
