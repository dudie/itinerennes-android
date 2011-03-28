package fr.itinerennes.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ItinerennesContext;

/**
 * The map box view component is a simple {@link LinearLayout} with common functionalities to handle
 * updates of its content.
 * 
 * @author Jérémie Huchet
 */
public class MapBoxView extends LinearLayout {

    /** The clickable state set. */
    protected static final int[] CLICKABLE_STATE_SET = { R.attr.state_clickable };

    /** The content view. */
    private View contentView = null;

    /**
     * Creates the map box view.
     * 
     * @param context
     *            the context
     */
    public MapBoxView(final ItinerennesContext context) {

        this(context, null);
    }

    /**
     * Creates the map box view.
     * 
     * @param context
     *            the context
     * @param attrs
     *            the xml attributes
     */
    public MapBoxView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
        setClickable(true);
    }

    /**
     * Gets the contentView.
     * 
     * @return the contentView
     */
    public final View getContentView() {

        return contentView;
    }

    /**
     * Sets the contentView.
     * 
     * @param contentView
     *            the contentView to set
     */
    public final void setContentView(final View contentView) {

        removeAllViews();
        this.contentView = contentView;
        addView(contentView);
    }

}
