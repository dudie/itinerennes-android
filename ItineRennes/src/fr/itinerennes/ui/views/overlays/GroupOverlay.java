package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Wraps multiple overlays.
 * 
 * @param <T>
 *            a class implementing {@link WrappableOverlay}. <strong> To ensure normal behavior, you
 *            should also provide a subclass of {@link OpenStreetMapViewOverlay}</strong>
 * @author Jérémie Huchet
 */
public class GroupOverlay<T extends WrappableOverlay> extends Overlay {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(GroupOverlay.class);

    /** A list of focusable overlays. */
    protected final List<T> overlays;

    /**
     * Creates the overlay wrapper.
     * 
     * @param context
     *            a context
     */
    public GroupOverlay(final Context context) {

        super(context);
        overlays = new ArrayList<T>();
    }

    /**
     * Adds an overlay to the group.
     * 
     * @param overlay
     *            the overlay to add
     */
    public final void addOverlay(final T overlay) {

        overlays.add(overlay);
    }

    /**
     * Removes an overlay from the group.
     * 
     * @param overlay
     *            the overlay to remove
     */
    public final void removeOverlay(final T overlay) {

        overlays.remove(overlay);
    }

    /**
     * Gets the list of overlays grouped in this overlay.
     * 
     * @return the overlays grouped in this overlay
     */
    public final List<T> getOverlays() {

        return overlays;
    }

    /**
     * Removes all overlays from the group.
     */
    public final void clear() {

        overlays.clear();
    }

    /**
     * Delegates {@link OpenStreetMapViewOverlay#onDraw(Canvas, OpenStreetMapView)} to all the
     * grouped overlays.
     * 
     * @see fr.itinerennes.ui.views.overlays.BaseOverlay#onDraw(android.graphics.Canvas,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    protected final void onDraw(final Canvas c, final MapView osmv) {

        for (final T overlay : overlays) {

            overlay.onDrawOverlay(c, osmv);
        }
    }

    /**
     * Delegates {@link OpenStreetMapViewOverlay#onDrawFinished(Canvas, OpenStreetMapView)} to all
     * the grouped overlays.
     * 
     * @see fr.itinerennes.ui.views.overlays.BaseOverlay#onDrawFinished(android.graphics.Canvas,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    protected final void onDrawFinished(final Canvas c, final MapView osmv) {

        for (final T overlay : overlays) {

            overlay.onDrawOverlayFinished(c, osmv);
        }
    }

    /**
     * Delegates {@link OpenStreetMapViewOverlay#onKeyDown(int, KeyEvent, OpenStreetMapView)} to all
     * the grouped overlays.
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onKeyDown(int,
     *      android.view.KeyEvent, org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final boolean onKeyDown(final int keyCode, final KeyEvent event, final MapView mapView) {

        for (final T overlay : overlays) {

            if (overlay.onKeyDown(keyCode, event, mapView)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delegates {@link OpenStreetMapViewOverlay#onKeyUp(int, KeyEvent, OpenStreetMapView)} to all
     * the grouped overlays.
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onKeyUp(int,
     *      android.view.KeyEvent, org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final boolean onKeyUp(final int keyCode, final KeyEvent event, final MapView mapView) {

        for (final T overlay : overlays) {

            if (overlay.onKeyUp(keyCode, event, mapView)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delegates {@link OpenStreetMapViewOverlay#onSingleTapUp(MotionEvent, OpenStreetMapView)} to
     * all the grouped overlays.
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onSingleTapUp(android.view.MotionEvent,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public boolean onSingleTapUp(final MotionEvent event, final MapView osmView) {

        for (final T overlay : overlays) {

            if (overlay.onSingleTapUp(event, osmView)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delegates {@link OpenStreetMapViewOverlay#onLongPress(MotionEvent, OpenStreetMapView)} to all
     * the grouped overlays.
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onLongPress(android.view.MotionEvent,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public boolean onLongPress(final MotionEvent event, final MapView osmv) {

        for (final T overlay : overlays) {

            if (overlay.onLongPress(event, osmv)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delegates {@link OpenStreetMapViewOverlay#onTouchEvent(MotionEvent, OpenStreetMapView)} to
     * all the grouped overlays.
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onTouchEvent(android.view.MotionEvent,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final boolean onTouchEvent(final MotionEvent event, final MapView mapView) {

        for (final T overlay : overlays) {

            if (overlay.onTouchEvent(event, mapView)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delegates {@link OpenStreetMapViewOverlay#onTrackballEvent(MotionEvent, OpenStreetMapView)}
     * to all the grouped overlays.
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onTrackballEvent(android.view.MotionEvent,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final boolean onTrackballEvent(final MotionEvent event, final MapView mapView) {

        for (final T overlay : overlays) {

            if (overlay.onTrackballEvent(event, mapView)) {
                return true;
            }
        }
        return false;
    }
}
