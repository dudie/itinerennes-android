package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.model.Marker;
import fr.itinerennes.ui.activity.ItinerennesContext;
import fr.itinerennes.ui.views.ItinerennesMapView;

/**
 * @author Jérémie Huchet
 */
public class MarkerOverlay extends LazyOverlay {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerOverlay.class);

    /** The MapActivity context. */
    private final ItinerennesContext context;

    /** The map view containing this overlay. */
    private final ItinerennesMapView map;

    /** The list containing visible types of markers. */
    private final List<String> visibleMarkerTypes = new ArrayList<String>(3);

    /** The list of all displayed markers. */
    private final List<MarkerOverlayItem> markers = new ArrayList<MarkerOverlayItem>(20);

    /**
     * Creates the marker overlay.
     * 
     * @param context
     *            the context
     */
    public MarkerOverlay(final ItinerennesContext context, final ItinerennesMapView map) {

        super(context);
        this.context = context;
        this.map = map;
        // TJHU Auto-generated constructor stub
        visibleMarkerTypes.add(ItineRennesConstants.MARKER_TYPE_BIKE);
        visibleMarkerTypes.add(ItineRennesConstants.MARKER_TYPE_BUS);
        visibleMarkerTypes.add(ItineRennesConstants.MARKER_TYPE_SUBWAY);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.LazyOverlay#onMapMove(org.osmdroid.views.MapView)
     */
    @Override
    protected synchronized void onMapMove(final MapView source) {

        markers.clear();

        for (final String type : visibleMarkerTypes) {
            final List<Marker> markersElements = this.context.getMarkerService().getMarkers(
                    source.getBoundingBox(), type);
            for (final Marker marker : markersElements) {
                final MarkerOverlayItem overlayItem = new MarkerOverlayItem();
                overlayItem.setId(marker.getId());
                overlayItem.setLabel(marker.getLabel());
                overlayItem.setLocation(marker.getGeoPoint());
                overlayItem.setType(type);
                overlayItem.setIcon(this.context.getResources().getDrawable(
                        marker.getIconDrawableId()));

                markers.add(overlayItem);
            }
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#onSingleTapUp(android.view.MotionEvent,
     *      org.osmdroid.views.MapView)
     */
    @Override
    public final boolean onSingleTapUp(final MotionEvent e, final MapView mapView) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSingleTapUp.start");
        }

        final MarkerOverlayItem marker = checkItemPresence(e, mapView);
        if (marker != null) {
            onSingleTapUpMarker(marker, mapView);
            mapView.postInvalidate();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onSingleTapUp.end");
            }

            return true;
        } else {
            ((ItinerennesMapView) mapView).getMapBoxController().hide();
            mapView.postInvalidate();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSingleTapUp.end");
        }

        return false;
    }

    /**
     * Called when an item is single tapped up on the overlay.
     * 
     * @param marker
     *            the marker tapped up
     * @param mapView
     *            the map view containing the overlay
     */
    public final void onSingleTapUpMarker(final MarkerOverlayItem marker, final MapView mapView) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("onSingleTapUpMarker.start - %s", marker));
        }

        ((ItinerennesMapView) mapView).getMapBoxController().show(marker);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("onSingleTapUpMarker.end - %s", marker));
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#draw(android.graphics.Canvas,
     *      org.osmdroid.views.MapView, boolean)
     */
    @Override
    protected final void draw(final Canvas c, final MapView osmv, final boolean shadow) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("draw.start"));
        }

        // on dessine les items seulement s'il ne s'agit pas du mode shadow
        if (!shadow) {
            final Projection pj = osmv.getProjection();

            for (final MarkerOverlayItem marker : markers) {
                final Point point = pj.toMapPixels(marker.getLocation(), null);

                drawItem(c, marker, point, osmv);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("draw.end"));
        }

    }

    /**
     * Draws an item located at the provided screen coordinates to the canvas.
     * 
     * @param canvas
     *            what the item is drawn upon
     * @param item
     *            the item to be drawn
     * @param curScreenCoords
     *            the screen coordinates of the item
     * @param mapView
     *            the map view
     */
    protected final void drawItem(final Canvas canvas, final MarkerOverlayItem item,
            final Point curScreenCoords, final MapView mapView) {

        final Drawable drawable = item.getIcon();

        final int[] originalState = drawable.getState();

        if (map.getZoomLevel() < ItineRennesConstants.CONFIG_MINIMUM_ZOOM_ITEMS) {
            drawable.setState(new int[] { R.attr.state_low_zoom });
        }

        final MarkerOverlayItem selectedItem = ((ItinerennesMapView) mapView).getMapBoxController()
                .getSelectedItem();
        if (selectedItem != null && selectedItem.getId().equals(item.getId())) {

            drawable.setState(new int[] { android.R.attr.state_pressed });
        }

        final int left_right = drawable.getIntrinsicWidth() / 2;
        final int top_bottom = drawable.getIntrinsicHeight() / 2;
        final Rect bounds = new Rect(-left_right, -top_bottom, left_right, top_bottom);
        drawable.setBounds(bounds);

        // draw it
        Overlay.drawAt(canvas, drawable, curScreenCoords.x, curScreenCoords.y, false);

        // restore original state
        drawable.setState(originalState);
    }

    /**
     * Check if an touched point of the screen is over an item of this overlay.
     * 
     * @param event
     *            event triggered
     * @param mapView
     *            map view containing the overlay
     * @return if found, the MarkerOverlayItem under the touch point
     */
    private MarkerOverlayItem checkItemPresence(final MotionEvent event, final MapView mapView) {

        final Projection pj = mapView.getProjection();
        final int eventX = (int) event.getX();
        final int eventY = (int) event.getY();

        final Point touchPoint = pj.fromMapPixels(eventX, eventY, null);

        final Point itemPoint = new Point();
        for (final MarkerOverlayItem marker : markers) {
            final Drawable drawable = marker.getIcon();

            pj.toPixels(marker.getLocation(), itemPoint);

            if (drawable.getBounds().contains(touchPoint.x - itemPoint.x,
                    touchPoint.y - itemPoint.y)) {
                return marker;
            }

        }
        return null;
    }
}
