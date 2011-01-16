package fr.itinerennes.ui.views;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.itinerennes.R;

/**
 * The map box view component is a simple {@link LinearLayout} with common functionalities to handle
 * updates of its content.
 * 
 * @author Jérémie Huchet
 */
public class MapBoxView extends LinearLayout {

    /** The optional additional informations view. */
    private View additionalInformations = null;

    /** The intent to use to start a new activity when the box is clicked. */
    private Intent onClickIntent;

    /**
     * @param context
     */
    public MapBoxView(final Context context) {

        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public MapBoxView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
    }

    /**
     * Sets the icon for the map box.
     * 
     * @param icon
     *            the icon drawable to set
     */
    public final void setIcon(final Drawable icon) {

        ((ImageView) findViewById(R.id.map_box_icon)).setImageDrawable(icon);
    }

    /**
     * Sets the title for the map box.
     * 
     * @param title
     *            the title to set
     */
    public final void setTitle(final String title) {

        ((TextView) findViewById(R.id.map_box_title)).setText(title);
    }

    /**
     * Sets the loading state of the current view. It causes the map view to display or not a
     * loading icon.
     * 
     * @param loading
     *            the loading state
     */
    public final void setLoading(final boolean loading) {

        ((ProgressBar) findViewById(R.id.map_box_progressbar)).setVisibility(loading ? VISIBLE
                : GONE);
    }

    /**
     * Does the current map box view showing a loading icon.
     * 
     * @return true if the current map box view is showing a loading icon
     */
    public final boolean getLoading() {

        return ((ProgressBar) findViewById(R.id.map_box_progressbar)).getVisibility() == VISIBLE;
    }

    /**
     * Appends the given view below the title of the box. Replace any existing additional view.
     * 
     * @param view
     *            the additional information view to display
     */
    public final void setAdditionalInformations(final View view) {

        removeAdditionalInformations();
        if (view != null) {
            view.setVisibility(VISIBLE);
            additionalInformations = view;
            addView(view);
        }
    }

    /**
     * Gets the additional informations view.
     * 
     * @return the additional informations view
     */
    public final View getAdditionalInformations() {

        return additionalInformations;
    }

    /**
     * Removes the additional informations view.
     */
    public final void removeAdditionalInformations() {

        if (additionalInformations != null) {
            removeView(additionalInformations);
            additionalInformations = null;
        }
    }

    /**
     * Modify the background of the view when a listener is attached.
     * 
     * @see android.view.View#setOnClickListener(android.view.View.OnClickListener)
     */
    @Override
    public final void setOnClickListener(final OnClickListener l) {

        super.setOnClickListener(l);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.view.View#setClickable(boolean)
     */
    @Override
    public final void setClickable(final boolean clickable) {

        super.setClickable(clickable);
        if (null == getBackground()) {
            return;
        }

        final int[] actualState = getBackground().getState();
        final ArrayList<Integer> newState = new ArrayList<Integer>(actualState.length + 1);
        for (final int state : actualState) {
            if (state != R.attr.state_clickable) {
                newState.add(state);
            }
        }
        if (clickable) {
            newState.add(R.attr.state_clickable);
        }

        final int[] arrayNewStates = new int[newState.size()];
        for (int i = 0; i < arrayNewStates.length; i++) {
            arrayNewStates[i] = newState.get(i);
        }

        getBackground().setState(arrayNewStates);

    }

    /**
     * Gets the intent to use to start a new activity when the box is clicked.
     * 
     * @return the intent to use to start a new activity when the box is clicked
     */
    public final Intent getOnClickIntent() {

        return onClickIntent;
    }

    /**
     * Sets the intent to use to start a new activity when the box is clicked.
     * 
     * @param onClickIntent
     *            the intent to use to start a new activity when the box is clicked
     */
    public final void setOnClickIntent(final Intent onClickIntent) {

        setClickable(null != onClickIntent);
        this.onClickIntent = onClickIntent;
    }
}
