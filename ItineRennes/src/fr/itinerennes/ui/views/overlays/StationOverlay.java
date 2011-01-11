package fr.itinerennes.ui.views.overlays;

import java.util.List;

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
import android.view.MotionEvent;
import android.view.ViewGroup;

import fr.itinerennes.R;
import fr.itinerennes.model.Station;
import fr.itinerennes.ui.views.MapView;

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
        if (((MapView) mapView).isItemLayoutFocused()) {
            final ViewGroup rootLayout = (ViewGroup) mapView.getRootView();
            final ViewGroup focusedBox = (ViewGroup) rootLayout.findViewById(R.id.focused_box);
            focusedBox.setVisibility(ViewGroup.GONE);
            focusedBox.removeAllViews();
            ((MapView) mapView).setItemLayoutFocused(false);
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

    @Override
    protected void onDrawItem(final Canvas canvas, final int index, final Point curScreenCoords) {

        if (index != selectedIndex) {
            super.onDrawItem(canvas, index, curScreenCoords);
        } else if (NOT_SET != selectedIndex) {
            this.curScreenCoords.set(curScreenCoords.x, curScreenCoords.y);
        }
    }

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

        final Rect rect = new Rect();
        getItemBoundingRetangle(item, rect, curScreenCoords);
        // draw it
        marker.setBounds(rect);
        marker.draw(canvas);
    }

    @Override
    protected boolean onSingleTapUpHelper(final int index, final T item) {

        selectedIndex = index;
        return super.onSingleTapUpHelper(index, item);
    }

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
