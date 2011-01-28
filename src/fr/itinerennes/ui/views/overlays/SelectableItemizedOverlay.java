package fr.itinerennes.ui.views.overlays;

import org.osmdroid.views.MapView;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.adapter.ItemizedOverlayAdapter;
import fr.itinerennes.ui.adapter.MapBoxAdapter;
import fr.itinerennes.ui.views.MapBoxView;

/**
 * An enhanced {@link OpenStreetMapViewItemizedOverlay} to handle focus of one of its elements.
 * 
 * @param <T>
 *            the type of the items of this overlay
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class SelectableItemizedOverlay<T extends SelectableMarker<D>, D> extends
        ITRItemizedOverlay<T> implements SelectableOverlay<D> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(SelectableItemizedOverlay.class);

    /** The adapter to use to display the focused item. */
    private final MapBoxAdapter<Marker<D>, D> boxDisplayAdaper;

    /** The target view where additional informations are displayed when an item is selected. */
    private final MapBoxView additionalInformationView;

    /**
     * Creates the focusable itemized overlay.
     * 
     * @param context
     *            the itinerennes application context
     * @param itemProviderAdapter
     *            the provider to use to populate the overlay
     * @param boxDisplayAdaper
     *            the adapter to use to display selected marker information
     */
    public SelectableItemizedOverlay(final ITRContext context,
            final ItemizedOverlayAdapter<T> itemProviderAdapter,
            final MapBoxAdapter<Marker<D>, D> boxDisplayAdaper,
            final MapBoxView additionalInformationView) {

        // items are set using setItems() / addItem() / removeItem()
        // OnItemGestureListener is implemented by this class and shouldn't be overridden as is
        // triggers more precise event helpers (onFocusHelper() and onBlurHelper())
        super(context, itemProviderAdapter);
        this.boxDisplayAdaper = boxDisplayAdaper;
        this.additionalInformationView = additionalInformationView;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.SelectableOverlay#onSelectStateChanged(boolean)
     */
    @Override
    public boolean onSelectStateChanged(final boolean selected) {

        if (selected) {
            if (focusedItemIndex != NOT_SET) {
                final T item = mItemList.get(focusedItemIndex);
                if (null != item) {
                    additionalInformationView.updateInBackground(boxDisplayAdaper, item);
                    item.onSelectStateChange(selected);
                }
            }
        } else {
            // this case should never occurs : if the overlays has loosed focus, then it should win
            // focus before loosing it again
            if (NOT_SET != prevFocusedItemIndex) {
                final T item = mItemList.get(prevFocusedItemIndex);
                if (null != item) {
                    additionalInformationView.setVisibility(View.GONE);
                    item.onSelectStateChange(selected);
                }
            }
            prevFocusedItemIndex = NOT_SET;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.SelectableOverlay#hasFocus()
     */
    @Override
    public final boolean hasFocus() {

        return focusedItemIndex != NOT_SET;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.SelectableOverlay#setSelected(boolean)
     */
    @Override
    public void setSelected(final boolean hasFocus) {

        if (hasFocus) {
            focusedItemIndex = prevFocusedItemIndex;
        } else {
            prevFocusedItemIndex = focusedItemIndex;
            focusedItemIndex = NOT_SET;
        }
    }

    /**
     * Workaround <code>onDraw()</code> method.
     * 
     * @see fr.itinerennes.ui.views.overlays.WrappableOverlay#onDrawOverlayFinished(android.graphics.Canvas,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final void onDrawOverlay(final Canvas canvas, final MapView mapView) {

        onDraw(canvas, mapView);
    }

    /**
     * Workaround <code>onDrawFinished()</code> method.
     * 
     * @see fr.itinerennes.ui.views.overlays.WrappableOverlay#onDrawOverlayFinished(android.graphics.Canvas,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final void onDrawOverlayFinished(final Canvas canvas, final MapView mapView) {

        onDrawFinished(canvas, mapView);

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay#onDrawItem(android.graphics.Canvas,
     *      int, android.graphics.Point)
     */
    @Override
    protected void onDrawItem(final Canvas canvas, final int index, final Point curScreenCoords) {

        if (NOT_SET != index /*
                              * TJHU bug item focusé non dessiné lorsque la carte est déplacée &&
                              * focusedItemIndex != index
                              */) {
            super.onDrawItem(canvas, index, curScreenCoords);
        } else if (focusedItemIndex == index) {
            this.curScreenCoords.set(curScreenCoords.x, curScreenCoords.y);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay#onDrawFinished(android.graphics.Canvas,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    protected void onDrawFinished(final Canvas canvas, final MapView osmv) {

        super.onDrawFinished(canvas, osmv);

        if (NOT_SET == focusedItemIndex) {
            return;
        }

        // get this item's preferred marker & hotspot
        final T item = this.mItemList.get(focusedItemIndex);

        final Drawable marker = item.getDrawable();
        final int[] originalState = marker.getState();
        marker.setState(new int[] { android.R.attr.state_pressed });

        final Rect rect = new Rect();
        getItemBoundingRetangle(item, rect, curScreenCoords);
        marker.draw(canvas);

        // restore original state
        marker.setState(originalState);
    }

    /**
     * <strong>from OSMDROID</strong> Finds the bounding rectangle for the object in current
     * projection.
     * 
     * @param item
     * @param rect
     * @return
     */
    private Rect getItemBoundingRetangle(final T item, final Rect rect, final Point ctr) {

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
