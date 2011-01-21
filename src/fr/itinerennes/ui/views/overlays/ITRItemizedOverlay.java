package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.graphics.Point;
import android.view.MotionEvent;

import fr.itinerennes.ui.adapter.ItemizedOverlayAdapter;
import fr.itinerennes.ui.tasks.UpdateOverlayTask;
import fr.itinerennes.ui.views.ITRMapView;
import fr.itinerennes.ui.views.MapListenerWrapper;

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
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class ITRItemizedOverlay<T extends ITROverlayItem<D>, D> extends ItemizedOverlay<T>
        implements MapListener, ItemizedOverlay.OnItemGestureListener<T> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(ItemizedOverlay.class);

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
    protected ItemizedOverlayAdapter<T, D> adapter;

    /** The async task in charge for managing overlay updates. */
    private UpdateOverlayTask<T, D> overlayUpdater = null;

    /**
     * Creates the itemized overlay.
     * 
     * @param context
     *            a context
     * @param adapter
     *            the adapter this overlay should use to update its content when the map moves
     */
    public ITRItemizedOverlay(final Context context, final ItemizedOverlayAdapter<T, D> adapter) {

        super(context, new ArrayList<T>(), null);
        this.adapter = adapter;
        super.mOnItemGestureListener = this;
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
        mapView.postInvalidate();
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
        // it will be used to retrieve its new index in nthe new item list
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

        if (null != overlayUpdater) {
            overlayUpdater.cancel(true);
        }
        overlayUpdater = new UpdateOverlayTask<T, D>(osmView, this, adapter);
        overlayUpdater.execute(osmView.getBoundingBox());
    }
}
