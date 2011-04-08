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
import android.os.AsyncTask;
import android.view.MotionEvent;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
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

    /** This property references the task currently loading the current displayed bounding box. */
    private AsyncTask<Void, Void, List<MarkerOverlayItem>> refreshTask = null;

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
        visibleMarkerTypes.add(TypeConstants.TYPE_BIKE);
        visibleMarkerTypes.add(TypeConstants.TYPE_BUS);
        visibleMarkerTypes.add(TypeConstants.TYPE_SUBWAY);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.LazyOverlay#onMapMove(org.osmdroid.views.MapView)
     */
    @Override
    protected final void onMapMove(final MapView source) {

        refreshTask = new AsyncTask<Void, Void, List<MarkerOverlayItem>>() {

            /**
             * Retrieves the markers for the currently displayed bounding boxes.
             * 
             * @see android.os.AsyncTask#doInBackground(Params[])
             */
            @Override
            protected List<MarkerOverlayItem> doInBackground(final Void... params) {

                final List<MarkerOverlayItem> newMarkers = new ArrayList<MarkerOverlayItem>();
                for (final String type : visibleMarkerTypes) {
                    newMarkers.addAll(context.getMarkerService().getMarkers(
                            source.getBoundingBox(), type));
                }
                return newMarkers;
            }

            @Override
            protected void onPostExecute(final List<MarkerOverlayItem> newMarkers) {

                if (refreshTask == this) {
                    LOGGER.debug("refreshing map");
                    markers.clear();
                    markers.addAll(newMarkers);
                    source.postInvalidate();
                } else {
                    LOGGER.debug("NOT refreshing map");
                }
            }
        };
        refreshTask.execute();
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
            LOGGER.debug("onSingleTapUp.start - event={}", e);
        }

        final boolean eventHandled;

        final MarkerOverlayItem marker = checkItemPresence(e, mapView);
        if (marker != null) {
            onSingleTapUpMarker(marker, mapView);
            mapView.postInvalidate();

            eventHandled = true;
        } else {
            ((ItinerennesMapView) mapView).getMapBoxController().hide();
            mapView.postInvalidate();

            eventHandled = false;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSingleTapUp.end - eventHandled={}", eventHandled);
        }

        return eventHandled;
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
            final Point point = new Point();

            // first draw markers
            for (final MarkerOverlayItem marker : markers) {
                pj.toMapPixels(marker.getLocation(), point);

                drawItem(c, marker, point, osmv);
            }

            // then if a marker is selected draw if over the others
            final MarkerOverlayItem selectedItem = ((ItinerennesMapView) osmv)
                    .getMapBoxController().getSelectedItem();
            if (null != selectedItem && markers.contains(selectedItem)) {
                pj.toMapPixels(selectedItem.getLocation(), point);
                drawItem(c, selectedItem, point, osmv);
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

        final int[] states = new int[3];
        int index = 0;

        if (map.getZoomLevel() < ItineRennesConstants.CONFIG_MINIMUM_ZOOM_ITEMS) {
            states[index++] = R.attr.state_low_zoom;
        }

        final MarkerOverlayItem selectedItem = ((ItinerennesMapView) mapView).getMapBoxController()
                .getSelectedItem();
        if (selectedItem != null && selectedItem.getId().equals(item.getId())) {

            states[index++] = android.R.attr.state_pressed;
        }

        if (item.isBookmarked()) {
            states[index++] = R.attr.state_bookmarked;
        }

        drawable.setState(states);

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
