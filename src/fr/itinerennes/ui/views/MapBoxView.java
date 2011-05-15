package fr.itinerennes.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ItineRennesActivity;

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

    /** On display animation. */
    private final Animation fadeIn;

    /** On hide animation. */
    private final Animation fadeOut;

    /**
     * Creates the map box view.
     * 
     * @param context
     *            the context
     */
    public MapBoxView(final ItineRennesActivity context) {

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

        fadeIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
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

    /**
     * {@inheritDoc}
     * 
     * @see android.view.View#setVisibility(int)
     */
    @Override
    public void setVisibility(final int visibility) {

        if (View.VISIBLE == getVisibility() && View.GONE == visibility) {
            // if map box is visible and asked to be gone
            setAnimation(fadeOut);
            getAnimation().start();
        } else if (View.GONE == getVisibility() && View.VISIBLE == visibility) {
            // if map box is gone and asked to be visible
            setAnimation(fadeIn);
            getAnimation().start();
        }
        super.setVisibility(visibility);
    }

}
