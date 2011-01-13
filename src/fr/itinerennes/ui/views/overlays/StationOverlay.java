package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.MotionEvent;
import android.view.ViewGroup;

import fr.itinerennes.R;
import fr.itinerennes.model.Station;

/**
 * Contains a list of items displayed on the map.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class StationOverlay<T extends StationOverlayItem> extends
        OpenStreetMapViewItemizedOverlay<T> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(StationOverlay.class);

    /** The selected index. */
    private int selectedIndex = NOT_SET;

    /** The current screen coordinates. */
    private final Point curScreenCoords = new Point();

    /** The type of this overlay. */
    private final int type;

    /**
     * Constructor.
     * 
     * @param ctx
     *            the context
     * @param items
     *            the items
     * @param onItemGestureListener
     *            The OnItemGestureListener used to trigger actions on items.
     * @param type
     *            The type of the overlay. Can be {@link Station#TYPE_BIKE},{@link Station#TYPE_BUS}
     *            or {@link Station#TYPE_SUBWAY}.
     */
    public StationOverlay(final Context ctx, final List<T> items,
            final OnItemGestureListener<T> onItemGestureListener, final int type) {

        super(ctx, items, onItemGestureListener);
        this.type = type;
    }

    /**
     * Listener to single tap on the overlay.
     * 
     * @see OpenStreetMapViewItemizedOverlay#onSingleTapUp(MotionEvent, OpenStreetMapView)
     */
    @Override
    public boolean onSingleTapUp(final MotionEvent event, final OpenStreetMapView mapView) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSingleTapUp.start - x={}, y={}", event.getX(), event.getY());
        }

        /*
         * When a single tap if intercepted, the focused layout becomes hidden and its content is
         * removed.
         */
        if (selectedIndex != NOT_SET) {

            unFocusItem(mapView);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSingleTapUp.end");
        }

        return super.onSingleTapUp(event, mapView);
    }

    /**
     * Returns the type of the overlay. Can be {@link Station#TYPE_BIKE},{@link Station#TYPE_BUS} or
     * {@link Station#TYPE_SUBWAY}.
     * 
     * @return the type of the overlay.
     */
    public final int getType() {

        return type;
    }

    /**
     * Append a list of items to the existing list.
     * 
     * @param list
     *            list of items to append
     */
    public void addItems(final List<T> list) {

        synchronized (this.mItemList) {

            this.mItemList.addAll(list);
        }

    }

    /**
     * Removes all items of the given type.
     * 
     * @param type
     *            type of items to remove
     */
    public void removeItems(final int type) {

        final List<T> newList = duplicateList(this.mItemList);

        final T selectedItem = (selectedIndex != NOT_SET) ? newList.get(selectedIndex) : null;

        synchronized (newList) {
            for (final StationOverlayItem item : newList) {
                if (item.getStation().getType() == type) {
                    this.mItemList.remove(item);
                }
            }
            updateSelectedItemIndex(selectedItem);
        }
    }

    /**
     * Removes all items not contained in the given bbox.
     * 
     * @param bbox
     */
    public void removeUnvisibleItems(final BoundingBoxE6 bbox) {

        final List<T> newList = duplicateList(this.mItemList);

        final T selectedItem = (selectedIndex != NOT_SET) ? newList.get(selectedIndex) : null;

        synchronized (newList) {
            for (final StationOverlayItem item : newList) {
                if (!bbox.contains(item.getStation().getGeoPoint())) {
                    this.mItemList.remove(item);
                }
            }
            updateSelectedItemIndex(selectedItem);
        }
    }

    /**
     * Search the given item in the items list and sets its index as the selected index.
     * 
     * @param item
     *            item to search in the item list
     */
    private void updateSelectedItemIndex(final T item) {

        if (selectedIndex != NOT_SET && item != null) {
            selectedIndex = (this.mItemList.lastIndexOf(item) > -1) ? this.mItemList
                    .lastIndexOf(item) : NOT_SET;
        }

    }

    /**
     * Hide the box on the map which shows information about stations.
     * 
     * @param mapView
     *            the map on which the box is displayed
     */
    public void unFocusItem(final OpenStreetMapView mapView) {

        final ViewGroup rootLayout = (ViewGroup) mapView.getRootView();
        final ViewGroup focusedBox = (ViewGroup) rootLayout.findViewById(R.id.focused_box);
        focusedBox.setVisibility(ViewGroup.GONE);
        focusedBox.removeAllViews();
        unSetFocusedItem();
        mapView.postInvalidate();
    }

    /**
     * Unset selected index.
     */
    public void unSetFocusedItem() {

        selectedIndex = NOT_SET;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay#onDrawItem(android.graphics.Canvas,
     *      int, android.graphics.Point)
     */
    @Override
    protected void onDrawItem(final Canvas canvas, final int index, final Point curScreenCoords) {

        if (index != selectedIndex) {
            super.onDrawItem(canvas, index, curScreenCoords);
        } else if (NOT_SET != selectedIndex) {
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
    protected void onDrawFinished(final Canvas canvas, final OpenStreetMapView osmv) {

        super.onDrawFinished(canvas, osmv);

        if (NOT_SET == selectedIndex) {
            return;
        }

        // get this item's preferred marker & hotspot
        final T item = this.mItemList.get(selectedIndex);

        final Drawable marker = (item
                .getMarker(OpenStreetMapViewOverlayItem.ITEM_STATE_FOCUSED_MASK) == null) ? this.mDefaultItem
                .getMarker(OpenStreetMapViewOverlayItem.ITEM_STATE_FOCUSED_MASK) : item
                .getMarker(OpenStreetMapViewOverlayItem.ITEM_STATE_FOCUSED_MASK);

        final Drawable[] layers = new Drawable[2];
        layers[0] = osmv.getContext().getResources().getDrawable(R.drawable.marker_selected_border);
        layers[1] = marker;

        final LayerDrawable layerDrawable = new LayerDrawable(layers);

        final Rect rect = new Rect();
        getItemBoundingRetangle(item, rect, curScreenCoords);
        // draw it
        layerDrawable.setBounds(rect);
        layerDrawable.draw(canvas);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay#onSingleTapUpHelper(int,
     *      org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem)
     */
    @Override
    protected boolean onSingleTapUpHelper(final int index, final T item) {

        selectedIndex = index;
        return super.onSingleTapUpHelper(index, item);
    }

    /**
     * Gets the bounding rectangle of an item.
     * 
     * @param item
     *            item to calculate bounds
     * @param rect
     *            Rectangle in which save bounds
     * @param ctr
     * @return the bounding rectangle of the item
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

    /**
     * Copy all elements of a list to a new one.
     * 
     * @param source
     *            the list to duplicate
     * @return a new list with all elements of the source list
     */
    private List<T> duplicateList(List<T> source) {

        final List<T> newList = new ArrayList<T>();
        Collections.copy(newList, source);

        return newList;
    }
}
