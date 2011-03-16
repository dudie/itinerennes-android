package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.adapter.ItemizedOverlayAdapter;
import fr.itinerennes.ui.tasks.UpdateOverlayTask;
import fr.itinerennes.ui.views.ITRMapView;
import fr.itinerennes.ui.views.MapListenerWrapper;
import fr.itinerennes.ui.views.overlays.event.OnItemizedOverlayUpdateListener;

/**
 * An enhanced itemized overlay which can use an {@link ItemizedOverlayAdapter} to update itself
 * when receiving map events.
 * <p>
 * It needs to ba attached as a {@link MapListener} to the {@link OpenStreetMapView}. You can use a
 * {@link MapListenerWrapper} to register multiple map listeners to a same map view.
 * 
 * @see MapListenerWrapper
 * @param <T>
 *            the type of the items of this overlay
 * @author Jérémie Huchet
 */
public class ITRItemizedOverlay<T extends Marker<?>> extends ItemizedOverlay<T> implements
        MapListener, ItemizedOverlay.OnItemGestureListener<T> {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(ItemizedOverlay.class);

    /** The itinerennes application context. */
    private final ITRContext context;

    /** The index of the current focused item. <code>NOT_SET</code> if not item is focused. */
    protected int focusedItemIndex = NOT_SET;

    /**
     * The index of the previously focused item. Value may be <code>NOT_SET</code> initially or
     * after onBlur() calls.
     */
    protected int prevFocusedItemIndex = NOT_SET;

    /** The current screen coordinates. */
    protected Point curScreenCoords = new Point();

    /** The adapter to use to provide items. */
    protected ItemizedOverlayAdapter<T> adapter;

    /** The async task in charge for managing overlay updates. */
    private UpdateOverlayTask<T> overlayUpdater = null;

    /** The localized name of the overlay. */
    private String localizedName;

    /** A reference to a object which listen for item updates in this overlay. */
    private OnItemizedOverlayUpdateListener updateListener;

    /**
     * The map on which this overlay is displayed. Used to attach a listener on map moves when this
     * overlay is enabled and detach it when disabled.
     */
    private final ITRMapView map;

    /**
     * Creates the itemized overlay.
     * 
     * @param context
     *            a context
     * @param adapter
     *            the adapter this overlay should use to update its content when the map moves
     */
    public ITRItemizedOverlay(final ITRContext context, final ITRMapView map,
            final ItemizedOverlayAdapter<T> adapter) {

        super(context, new ArrayList<T>(), null);
        this.context = context;
        this.map = map;
        this.adapter = adapter;
        super.mOnItemGestureListener = this;
        this.localizedName = context.getString(R.string.overlay_unamed);
    }

    @Override
    public boolean onSingleTapUp(final MotionEvent event, final MapView mapView) {

        final boolean handled = super.onSingleTapUp(event, mapView);
        // if single tap up returns true, no item have matched the tap coordinates
        // focus has been loosed
        if (handled == false) {
            prevFocusedItemIndex = focusedItemIndex;
            focusedItemIndex = NOT_SET;
        }
        return handled;
    }

    @Override
    public final boolean onItemSingleTapUp(final int index, final T item) {

        // set the new focused element
        prevFocusedItemIndex = focusedItemIndex;
        focusedItemIndex = index;
        return true;
    }

    @Override
    public final boolean onItemLongPress(final int index, final T item) {

        // TJHU que fait on si clic long sur un item ?
        return false;
    }

    /**
     * Removes all items from the overlay.
     */
    public void onClearOverlay(final MapView mapview) {

        focusedItemIndex = NOT_SET;
        prevFocusedItemIndex = NOT_SET;
        super.mItemList.clear();
        mapview.postInvalidate();

        if (null != updateListener) {
            updateListener.onContentUpdated(new ArrayList());
        }
    }

    /**
     * Adds the given items to the overlay.
     * 
     * @param items
     *            the items to add to the overlay
     */
    public void onAddItems(final MapView mapview, final T prevSelectedItem, final List<T> items) {

        // change the list of items
        super.mItemList.addAll(items);
        mapview.postInvalidate();
    }

    public void onReplaceItems(final MapView mapview, final List<T> items) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onReplaceItems.start - {} new items", items == null ? 0 : items.size());
        }
        // store the current focused item
        // it will be used to retrieve its new index in the new item list
        final T currentFocusedItem;
        if (NOT_SET != focusedItemIndex) {
            currentFocusedItem = super.mItemList.get(focusedItemIndex);
        } else {
            currentFocusedItem = null;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("currentFocusedItem = {}", null == currentFocusedItem ? "NONE"
                    : currentFocusedItem.getData());
            LOGGER.debug("focusedItemIndex = {}", NOT_SET == focusedItemIndex ? "NOT_SET"
                    : focusedItemIndex);
        }

        super.mItemList.clear();
        super.mItemList.addAll(items);

        // if an item was previously focused, set the focusedItemIndex to its new index value
        if (null != currentFocusedItem) {
            boolean indexUpdated = false;
            for (int i = 0; i < super.mItemList.size() && !indexUpdated; i++) {
                if (currentFocusedItem.getId().equals(super.mItemList.get(i).getId())) {
                    indexUpdated = true;
                    focusedItemIndex = i;
                }
            }
            // focusedItemIndex was not updated, the previous item is no longer in the current
            // bounding box
            // '- trigger onBlur() as the item is no longer focused)
            // - set the focusedItemIndex to NOT_SET
            if (!indexUpdated) {
                // TJHU onBlurHelper(additionalInformationView, focusedItem);
                focusedItemIndex = NOT_SET;
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("newFocusedItemIndex = {}", NOT_SET == focusedItemIndex ? "NOT_SET"
                    : focusedItemIndex);
        }

        mapview.postInvalidate();

        if (null != updateListener) {
            updateListener.onContentUpdated(items);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onReplaceItems.end");
        }
    }

    /**
     * When this overlay is linked to a {@link ITRMapView}, this method is triggered when the map
     * scrolls.
     * <p>
     * When it occurs, the overlay use its adapter and update its content.
     * 
     * @param event
     *            the scroll event
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public final boolean onScroll(final ScrollEvent event) {

        updateOverlay(event.getSource());
        return false;
    }

    /**
     * When this overlay is linked to a {@link ITRMapView}, this method is triggered when the map
     * zoom changes.
     * <p>
     * When it occurs, the overlay use its adapter and update its content.
     * 
     * @param event
     *            the zoom event
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public final boolean onZoom(final ZoomEvent event) {

        updateOverlay(event.getSource());
        return false;
    }

    /**
     * Creates and starts an {@link UpdateOverlayTask} to refresh the overlay's content. If a
     * previous task was working, it is canceled and then the new one starts.
     * 
     * @param osmView
     *            the open street map view
     */
    protected void updateOverlay(final MapView osmView) {

        if (isEnabled()) {
            if (null != overlayUpdater) {
                overlayUpdater.cancel(true);
            }
            overlayUpdater = new UpdateOverlayTask<T>(context, osmView, this, adapter);
            overlayUpdater.execute(osmView.getBoundingBox());
        }
    }

    /**
     * Sets the localized name of the overlay.
     * 
     * @param name
     *            the new name of the overlay
     */
    public final void setLocalizedName(final String name) {

        localizedName = name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.ToggleableOverlay#getLocalizedName()
     */
    public final String getLocalizedName() {

        return localizedName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#setEnabled(boolean)
     */
    @Override
    public final void setEnabled(final boolean enabled) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("setEnabled.start - overlay={}, enabled={}", localizedName, enabled);
        }
        super.setEnabled(enabled);

        if (enabled) {
            map.getListeners().add(this);
            updateOverlay(map);
        } else {
            map.getListeners().remove(this);
            if (null != overlayUpdater) {
                overlayUpdater.cancel(true);
            }
            onClearOverlay(map);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("setEnabled.end - overlay={}, enabled={}", localizedName, enabled);
        }
    }

    /**
     * Sets listener for item updates.
     * 
     * @param updateListener
     *            the listener to set
     */
    public final void setOnItemizedOverlayUpdateListener(
            final OnItemizedOverlayUpdateListener updateListener) {

        this.updateListener = updateListener;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.ItemizedOverlay#onDrawItem(android.graphics.Canvas, int,
     *      android.graphics.Point)
     */
    @Override
    protected void onDrawItem(final Canvas canvas, final int index, final Point curScreenCoords) {

        // get this item's preferred marker & hotspot
        final T item = super.mItemList.get(index);

        final Drawable marker = item.getDrawable();
        final int[] originalState = marker.getState();
        if (map.getZoomLevel() < 17) {
            marker.setState(new int[] { R.attr.state_low_zoom });
        }

        final Rect rect = new Rect();
        getItemBoundingRetangle(item, rect, curScreenCoords);
        marker.setBounds(rect);
        marker.draw(canvas);

        // restore original state
        marker.setState(originalState);
    }

    /**
     * <strong>from OSMDROID</strong> Finds the bounding rectangle for the object in current
     * projection.
     * 
     * @param item
     *            the item to draw
     * @param rect
     *            the rectangle where to set bounds
     * @param ctr
     *            the center
     * @return the bounding rectangle for the item's drawable
     */
    protected Rect getItemBoundingRetangle(final T item, final Rect rect, final Point ctr) {

        final Drawable marker = (item.getMarker(0) == null) ? this.mDefaultItem.getMarker(0) : item
                .getMarker(0);
        final Point markerHotspot = (item.getMarkerHotspot(0) == null) ? this.mDefaultItem
                .getMarkerHotspot(0) : item.getMarkerHotspot(0);

        // calculate bounding rectangle
        final int markerWidth = marker.getIntrinsicWidth();
        final int markerHeight = marker.getIntrinsicHeight();
        final int left = ctr.x - markerHotspot.x;
        final int right = left + markerWidth;
        final int top = ctr.y - markerHotspot.y;
        final int bottom = top + markerHeight;

        rect.set(left, top, right, bottom);
        return rect;
    }
}
