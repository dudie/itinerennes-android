package fr.itinerennes.ui.views.overlays;

import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

/**
 * Common overlay interface to make {@link OpenStreetMapViewOverlay} wrappable.
 * <p>
 * To wrap multiple overlays into a single {@link GroupOverlay}, you need to provide a class
 * extending {@link OpenStreetMapViewOverlay} and implementing this interface.
 * 
 * @author Jérémie Huchet
 */
public interface WrappableOverlay {

    /**
     * @param canvas
     *            the canvas to draw into
     * @param mapView
     *            the open street map view
     * @see OpenStreetMapViewOverlay#onManagedDraw(Canvas, OpenStreetMapView)
     */
    void onManagedDraw(Canvas canvas, OpenStreetMapView mapView);

    /**
     * Workaround method to make {@link OpenStreetMapViewOverlay} wrappable.
     * <p>
     * Implementation should just make a call to
     * {@link OpenStreetMapViewOverlay#onDraw(Canvas, OpenStreetMapView)}
     * 
     * @param canvas
     *            the canvas to draw into
     * @param mapView
     *            the open street map view
     */
    void onDrawOverlay(Canvas canvas, OpenStreetMapView mapView);

    /**
     * Workaround method to make {@link OpenStreetMapViewOverlay} wrappable.
     * <p>
     * Implementation should just make a call to
     * {@link OpenStreetMapViewOverlay#onDrawFinished(Canvas, OpenStreetMapView)}
     * 
     * @param canvas
     *            the canvas to draw into
     * @param mapView
     *            the open street map view
     */
    void onDrawOverlayFinished(final Canvas canvas, final OpenStreetMapView mapView);

    /**
     * @param keyCode
     *            the key code
     * @param event
     *            the key event detected
     * @param mapView
     *            the open street map view
     * @return true if the event has been completely handled, this causes the event propagation to
     *         be stopped
     * @see OpenStreetMapViewOverlay#onKeyDown(int, KeyEvent, OpenStreetMapView)
     */
    boolean onKeyDown(final int keyCode, KeyEvent event, final OpenStreetMapView mapView);

    /**
     * @param keyCode
     *            the key code
     * @param event
     *            the key event detected
     * @param mapView
     *            the open street map view
     * @return true if the event has been completely handled, this causes the event propagation to
     *         be stopped
     * @see OpenStreetMapViewOverlay#onKeyUp(int, KeyEvent, OpenStreetMapView)
     */
    boolean onKeyUp(final int keyCode, KeyEvent event, final OpenStreetMapView mapView);

    /**
     * @param event
     *            the touch event detected
     * @param mapView
     *            the open street map view
     * @return true if the event has been completely handled, this causes the event propagation to
     *         be stopped
     * @see OpenStreetMapViewOverlay#onTouchEvent(MotionEvent, OpenStreetMapView)
     */
    boolean onTouchEvent(final MotionEvent event, final OpenStreetMapView mapView);

    /**
     * @param event
     *            the trackball event detected
     * @param mapView
     *            the open street map view
     * @return true if the event has been completely handled, this causes the event propagation to
     *         be stopped
     * @see OpenStreetMapViewOverlay#onTrackballEvent(MotionEvent, OpenStreetMapView)
     */
    boolean onTrackballEvent(final MotionEvent event, final OpenStreetMapView mapView);

    /**
     * @param event
     *            the single tap up event detected
     * @param mapView
     *            the open street map view
     * @return true if the event has been completely handled, this causes the event propagation to
     *         be stopped
     * @see OpenStreetMapViewOverlay#onSingleTapUp(MotionEvent, OpenStreetMapView)
     */
    boolean onSingleTapUp(MotionEvent event, OpenStreetMapView mapView);

    /**
     * @param event
     *            the long press event detected
     * @param mapView
     *            the open street map view
     * @return true if the event has been completely handled, this causes the event propagation to
     *         be stopped
     * @see OpenStreetMapViewOverlay#onLongPress(MotionEvent, OpenStreetMapView)
     */
    boolean onLongPress(MotionEvent event, OpenStreetMapView mapView);
}
