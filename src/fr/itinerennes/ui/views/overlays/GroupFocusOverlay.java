package fr.itinerennes.ui.views.overlays;

import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.view.MotionEvent;

import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.views.MapBoxView;

/**
 * An OSM overlay wrapping multiple overlays and allowing only one of them to be focused.
 * <p>
 * Delegates every {@link OpenStreetMapViewOverlay} method calls to each wrapped overlay.
 * 
 * @param <T>
 *            a class implementing {@link FocusableOverlay}. <strong> To ensure normal behavior, you
 *            should also provide a subclass of {@link OpenStreetMapViewOverlay}</strong>
 * @author Jérémie Huchet
 */
public class GroupFocusOverlay<T extends FocusableOverlay<?>> extends GroupOverlay<T> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(GroupFocusOverlay.class);

    /** The view containing additional informations about the focused item. */
    private final MapBoxView focusedItemAdditionalInfo;

    /**
     * Creates the overlay wrapper.
     * 
     * @param context
     *            the itinerennes application context
     * @param focusedItemAdditionalInfo
     *            the view containing additional informations about the focused item
     */
    public GroupFocusOverlay(final ITRContext context, final MapBoxView focusedItemAdditionalInfo) {

        super(context);
        this.focusedItemAdditionalInfo = focusedItemAdditionalInfo;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Additionnaly, it triggers focus events ({@link FocusableOverlay#onBlur()},
     * {@link FocusableOverlay#onFocus()}).
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onSingleTapUp(android.view.MotionEvent,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final boolean onSingleTapUp(final MotionEvent event, final OpenStreetMapView osmView) {

        // true if onSingleTapUp event propagation should be stopped
        boolean stopSingleTapUpPropagation = false;
        // true if onBlur and onFocus event propagation should be stopped
        boolean stopBlurPropagation = false;
        boolean stopFocusPropagation = false;
        boolean stopKeepFocusPropagation = false;

        for (final T overlay : overlays) {
            final boolean hasFocusBeforeSingleTapUp = overlay.hasFocus();
            if (!stopSingleTapUpPropagation) {
                stopSingleTapUpPropagation = overlay.onSingleTapUp(event, osmView);
            }
            final boolean hasFocusAfterSingleTapUp = overlay.hasFocus();

            // the overlay has loosed focus
            if (!stopBlurPropagation && hasFocusBeforeSingleTapUp && !hasFocusAfterSingleTapUp) {
                stopBlurPropagation = overlay.onBlur(focusedItemAdditionalInfo);
            }
            // the overlay has won focus
            if (!stopFocusPropagation && !hasFocusBeforeSingleTapUp && hasFocusAfterSingleTapUp) {
                stopFocusPropagation = overlay.onFocus(focusedItemAdditionalInfo);
            }
            // the overlay has kept the focus
            if (!stopKeepFocusPropagation && hasFocusBeforeSingleTapUp && hasFocusAfterSingleTapUp) {
                stopKeepFocusPropagation = overlay.onKeepFocus(focusedItemAdditionalInfo);
            }
        }

        return stopSingleTapUpPropagation;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onLongPress(android.view.MotionEvent,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final boolean onLongPress(final MotionEvent event, final OpenStreetMapView osmv) {

        // TJHU à implémenter pour gérer le focus lorsque clic long ?
        return super.onLongPress(event, osmv);
    }

}
