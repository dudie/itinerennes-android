package fr.itinerennes.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.adapter.MapBoxAdapter;
import fr.itinerennes.ui.tasks.DisplayMapBoxTask;

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

    /** The task used to fill the map box view with additional information in background. */
    private DisplayMapBoxTask<?> mapBoxDisplayer = null;

    /**
     * Creates the map box view.
     * 
     * @param context
     *            the context
     */
    public MapBoxView(final ITRContext context) {

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
     * When visibility switch to {@link View#GONE} we must cancel the running map box displayer
     * task.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.view.View#setVisibility(int)
     */
    @Override
    public final void setVisibility(final int visibility) {

        if (GONE == visibility && mapBoxDisplayer != null) {
            mapBoxDisplayer.cancel(true);
        }
        super.setVisibility(visibility);
    }

    /**
     * Updates the content of the map box view with the given adapter for the given item.
     * 
     * @param <D>
     *            the type of data of the item bundle
     * @param adapter
     *            the adapter to use to update the view
     * @param item
     *            the item to be displayed
     */
    public final <D> void updateInBackground(final MapBoxAdapter<D> adapter, final D item) {

        if (null != mapBoxDisplayer) {
            mapBoxDisplayer.cancel(true);
        }
        mapBoxDisplayer = new DisplayMapBoxTask<D>((ITRContext) getContext(), this, adapter, item);
        mapBoxDisplayer.execute();
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
