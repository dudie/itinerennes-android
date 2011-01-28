package fr.itinerennes.ui.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.adapter.MapBoxAdapter;
import fr.itinerennes.ui.tasks.DisplayMapBoxTask;
import fr.itinerennes.ui.views.overlays.Marker;
import fr.itinerennes.ui.views.overlays.SelectableMarker;

/**
 * The map box view component is a simple {@link LinearLayout} with common functionalities to handle
 * updates of its content.
 * 
 * @author Jérémie Huchet
 */
public class MapBoxView extends LinearLayout {

    /** The clickable state set. */
    protected static final int[] CLICKABLE_STATE_SET = { R.attr.state_clickable };

    /** The optional additional informations view. */
    private View additionalInformations = null;

    /** The intent to use to start a new activity when the box is clicked. */
    private Intent onClickIntent;

    /** The task used to fill the map box view with additional information in background. */
    private DisplayMapBoxTask<?> mapBoxDisplayer = null;

    private String bookmarkType;

    private String bookmarkId;

    private String bookmarkLabel;

    /**
     * @param context
     */
    public MapBoxView(final ITRContext context) {

        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public MapBoxView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
        setClickable(true);
    }

    @Override
    protected final void onFinishInflate() {

        super.onFinishInflate();

        final ToggleButton favButton = (ToggleButton) findViewById(R.id.map_box_toggle_bookmark);
        favButton.setVisibility(VISIBLE);
        favButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

                if (null != bookmarkId && null != bookmarkType && null != bookmarkLabel) {
                    if (isChecked) {
                        ((ITRContext) getContext()).getBookmarksService().setStarred(bookmarkType,
                                bookmarkId, bookmarkLabel);
                    } else {
                        ((ITRContext) getContext()).getBookmarksService().setNotStarred(
                                bookmarkType, bookmarkId);
                    }
                }
            }
        });
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

        this.onClickIntent = onClickIntent;
        refreshDrawableState();
    }

    /**
     * Cancel the {@link DisplayMapBoxTask} if you request the visibility to be {@link View#GONE}.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.view.View#setVisibility(int)
     */
    @Override
    public final void setVisibility(final int visibility) {

        if (GONE == visibility) {
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
    public final <D> void updateInBackground(final MapBoxAdapter<Marker<D>, D> adapter,
            final SelectableMarker<D> item) {

        if (null != mapBoxDisplayer) {
            mapBoxDisplayer.cancel(true);
        }
        mapBoxDisplayer = new DisplayMapBoxTask<D>((ITRContext) getContext(), this, adapter, item);
        mapBoxDisplayer.execute();
    }

    /**
     * Check if an {@link #onClickIntent} is set and update the background state.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.view.ViewGroup#drawableStateChanged()
     */
    @Override
    protected final void drawableStateChanged() {

        super.drawableStateChanged();
        final Drawable d = getBackground();
        if (null != d) {
            if (null != onClickIntent) {
                d.setState(mergeDrawableStates(super.onCreateDrawableState(1), CLICKABLE_STATE_SET));
            } else {
                super.onCreateDrawableState(0);
            }
        }
    }

    /**
     * Sets the star button state.
     * 
     * @param starred
     *            true to highlight the star icon
     */
    public final void setStarred(final boolean starred) {

        final ToggleButton favButton = (ToggleButton) findViewById(R.id.map_box_toggle_bookmark);
        favButton.setVisibility(VISIBLE);
        favButton.setChecked(starred);
    }

    /**
     * Sets the bookmarkType.
     * 
     * @param bookmarkType
     *            the bookmarkType to set
     */
    public final void setBookmarkType(final String bookmarkType) {

        this.bookmarkType = bookmarkType;
    }

    /**
     * Sets the bookmarkId.
     * 
     * @param bookmarkId
     *            the bookmarkId to set
     */
    public final void setBookmarkId(final String bookmarkId) {

        this.bookmarkId = bookmarkId;
    }

    /**
     * Sets the bookmarkLabel.
     * 
     * @param bookmarkLabel
     *            the bookmarkLabel to set
     */
    public final void setBookmarkLabel(final String bookmarkLabel) {

        this.bookmarkLabel = bookmarkLabel;
    }

}
