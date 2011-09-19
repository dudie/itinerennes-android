package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.database.MarkerDao;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.ItinerennesMapView;
import fr.itinerennes.utils.ResourceResolver;

/**
 * This overlay displays a different icon for each type of marker.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class MarkerOverlay extends LazyOverlay {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerOverlay.class);

    /** The MapActivity context. */
    private final ItineRennesActivity context;

    /** The map view containing this overlay. */
    private final ItinerennesMapView map;

    /** The list containing visible types of markers. */
    private final List<String> visibleMarkerTypes = new ArrayList<String>(3);

    /** The map containing labels for types of markers. */
    private final HashMap<String, String> markerTypesLabel = new HashMap<String, String>(3);

    /** The list of all displayed markers. */
    private final LinkedHashMap<String, MarkerOverlayItem> markers = new LinkedHashMap<String, MarkerOverlayItem>(
            20);

    /** This property references the task currently loading the current displayed bounding box. */
    private AsyncTask<Void, Void, LinkedHashMap<String, MarkerOverlayItem>> refreshTask = null;

    /** Simple cache for markers icons. */
    private final HashMap<String, Drawable> markerIcons = new HashMap<String, Drawable>();

    /**
     * Creates the marker overlay.
     * 
     * @param context
     *            the context
     * @param map
     *            the {@link MapView}
     */
    public MarkerOverlay(final ItineRennesActivity context, final ItinerennesMapView map) {

        super(context);
        this.context = context;
        this.map = map;

        visibleMarkerTypes.add(TypeConstants.TYPE_BIKE);
        visibleMarkerTypes.add(TypeConstants.TYPE_BUS);
        visibleMarkerTypes.add(TypeConstants.TYPE_SUBWAY);

        markerTypesLabel.put(TypeConstants.TYPE_BUS, context.getString(R.string.overlay_bus));
        markerTypesLabel.put(TypeConstants.TYPE_BIKE, context.getString(R.string.overlay_bike));
        markerTypesLabel.put(TypeConstants.TYPE_SUBWAY, context.getString(R.string.overlay_subway));

    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.LazyOverlay#onMapMove(org.osmdroid.views.MapView)
     */
    @Override
    public final void onMapMove(final MapView source) {

        refreshTask = new AsyncTask<Void, Void, LinkedHashMap<String, MarkerOverlayItem>>() {

            /**
             * Retrieves the markers for the currently displayed bounding boxes.
             * 
             * @see android.os.AsyncTask#doInBackground(Params[])
             */
            @Override
            protected LinkedHashMap<String, MarkerOverlayItem> doInBackground(final Void... params) {

                final MarkerDao markerDao = context.getApplicationContext().getMarkerDao();
                final Cursor c = context.getApplicationContext().getMarkerDao()
                        .getMarkers(source.getBoundingBox(), visibleMarkerTypes);

                final LinkedHashMap<String, MarkerOverlayItem> markers = new LinkedHashMap<String, MarkerOverlayItem>();
                if (c != null && c.moveToFirst()) {
                    while (!c.isAfterLast()) {

                        final String markerId = c.getString(c.getColumnIndex(MarkersColumns.ID));

                        markers.put(markerId, markerDao.getMarkerOverlayItem(c));

                        c.moveToNext();
                    }
                    c.close();
                }

                return markers;
            }

            @Override
            protected void onPostExecute(final LinkedHashMap<String, MarkerOverlayItem> newMarkers) {

                if (refreshTask == this) {
                    LOGGER.debug("refreshing map");
                    markers.clear();
                    if (newMarkers != null) {
                        markers.putAll(newMarkers);
                    }

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
     * @see org.osmdroid.views.overlay.Overlay#onSingleTapConfirmed(MotionEvent, MapView)
     */
    @Override
    public final boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSingleTapConfirmed.start - event={}", e);
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
            LOGGER.debug("onSingleTapConfirmed.end - eventHandled={}", eventHandled);
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
    private void onSingleTapUpMarker(final MarkerOverlayItem marker, final MapView mapView) {

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

        // if (LOGGER.isDebugEnabled()) {
        // LOGGER.debug(String.format("draw.start"));
        // }

        // on dessine les items seulement s'il ne s'agit pas du mode shadow
        if (!shadow && markers.size() > 0) {
            final Projection pj = osmv.getProjection();
            final Point point = new Point();

            // first draw markers
            for (final MarkerOverlayItem marker : markers.values()) {
                pj.toMapPixels(marker.getLocation(), point);

                drawItem(c, marker, point, osmv);
            }

            // then if a marker is selected draw it over the others
            final MarkerOverlayItem selectedItem = ((ItinerennesMapView) osmv)
                    .getMapBoxController().getSelectedItem();
            if (null != selectedItem && markers.containsKey(selectedItem.getId())) {
                pj.toMapPixels(selectedItem.getLocation(), point);
                drawItem(c, selectedItem, point, osmv);
            }
        }

        // if (LOGGER.isDebugEnabled()) {
        // LOGGER.debug(String.format("draw.end"));
        // }
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

        if (!markerIcons.containsKey(item.getType())) {
            // TJHU set the default marker resource identifier
            final int iconId = ResourceResolver.getDrawableId(context,
                    String.format("icx_marker_%s", item.getType()), 0);
            markerIcons.put(item.getType(), context.getResources().getDrawable(iconId));
        }

        final Drawable drawable = markerIcons.get(item.getType());
        int[] originalState = null;

        final int[] states = new int[2];
        int index = 0;

        if (map.getZoomLevel() >= ItineRennesConstants.CONFIG_MINIMUM_ZOOM_ITEMS) {
            states[index++] = R.attr.state_high_zoom;
        }

        if (item.isBookmarked()) {
            states[index++] = R.attr.state_bookmarked;
        }

        if (index > 0) {
            originalState = drawable.getState();
            drawable.setState(states);

        }

        final int leftRight = drawable.getIntrinsicWidth() / 2;
        final int topBottom = drawable.getIntrinsicHeight() / 2;
        final Rect bounds = new Rect(-leftRight, -topBottom, leftRight, topBottom);
        drawable.setBounds(bounds);

        // draw it
        Overlay.drawAt(canvas, drawable, curScreenCoords.x, curScreenCoords.y, false);

        if (originalState != null) {
            // restore original state
            drawable.setState(originalState);
        }
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
        for (final MarkerOverlayItem marker : markers.values()) {
            final Drawable drawable = markerIcons.get(marker.getType());

            pj.toPixels(marker.getLocation(), itemPoint);

            if (drawable.getBounds().contains(touchPoint.x - itemPoint.x,
                    touchPoint.y - itemPoint.y)) {
                return marker;
            }

        }
        return null;
    }

    /**
     * Gets the markerTypesLabel.
     * 
     * @return the markerTypesLabel
     */
    public final HashMap<String, String> getMarkerTypesLabel() {

        return markerTypesLabel;
    }

    /**
     * Is a marker type visible ?
     * 
     * @param type
     *            the type of the marker to get its visibility
     * @return true if the marker type is visible
     */
    public final boolean isMarkerTypeVisible(final String type) {

        return visibleMarkerTypes.contains(type);
    }

    /**
     * Show markers of a given type.
     * 
     * @param type
     *            type to show
     */
    public final void show(final String type) {

        if (!visibleMarkerTypes.contains(type)) {
            visibleMarkerTypes.add(type);
        }

    }

    /**
     * Hide markers of a given type.
     * 
     * @param type
     *            type to hide
     */
    public final void hide(final String type) {

        visibleMarkerTypes.remove(type);

    }
}
