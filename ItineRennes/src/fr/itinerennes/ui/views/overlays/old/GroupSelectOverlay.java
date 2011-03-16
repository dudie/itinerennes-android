package fr.itinerennes.ui.views.overlays.old;

import org.osmdroid.views.MapView;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.content.Context;
import android.view.MotionEvent;

/**
 * An OSM overlay wrapping multiple overlays and allowing only one of them to be focused.
 * <p>
 * Delegates every {@link OpenStreetMapViewOverlay} method calls to each wrapped overlay.
 * 
 * @param <T>
 *            a class implementing {@link SelectableOverlay}. <strong> To ensure normal behavior,
 *            you should also provide a subclass of {@link OpenStreetMapViewOverlay}</strong>
 * @author Jérémie Huchet
 */
public class GroupSelectOverlay<T extends SelectableOverlay<?>> extends GroupOverlay<T> {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(GroupSelectOverlay.class);

    /**
     * Create the group focus overlay.
     * 
     * @param context
     *            the context
     */
    public GroupSelectOverlay(final Context context) {

        super(context);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Additionnaly, it triggers focus events ({@link SelectableOverlay#onBlur()},
     * {@link SelectableOverlay#onFocus()}).
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onSingleTapUp(android.view.MotionEvent,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final boolean onSingleTapUp(final MotionEvent event, final MapView osmView) {

        // true if onSingleTapUp event propagation should be stopped
        boolean stopSingleTapUpPropagation = false;

        T overlayWinningFocus = null;
        T overlayLoosingFocus = null;
        T overlayKeepingFocus = null;

        for (final T overlay : overlays) {
            final boolean hasFocusBeforeSingleTapUp = overlay.hasFocus();
            stopSingleTapUpPropagation &= overlay.onSingleTapUp(event, osmView);
            final boolean hasFocusAfterSingleTapUp = overlay.hasFocus();

            // the overlay has won focus
            if (!hasFocusBeforeSingleTapUp && hasFocusAfterSingleTapUp) {
                overlayWinningFocus = overlay;
            }
            // the overlay has loosed focus
            if (hasFocusBeforeSingleTapUp && !hasFocusAfterSingleTapUp) {
                overlayLoosingFocus = overlay;
            }
            // the overlay has kept the focus
            if (hasFocusBeforeSingleTapUp && hasFocusAfterSingleTapUp) {
                overlayKeepingFocus = overlay;
            }
        }

        // workaround to avoid multiple item selection (see ITR-43)
        // give priority to the overlay keeping focus (see ITR-43)
        if (overlayKeepingFocus != null) {
            // set NOT_FOCUSED overlays not keeping focus (see ITR-43)
            for (final T overlay : overlays) {
                if (!overlay.equals(overlayKeepingFocus)) {
                    overlay.setSelected(false);
                }
            }
            overlayKeepingFocus.onSelectStateChanged(true);
        } else {
            // set NOT_FOCUSED overlays not winning focus and not loosing focus (see ITR-43)
            for (final T overlay : overlays) {
                if (!overlay.equals(overlayWinningFocus) && !overlay.equals(overlayLoosingFocus)) {
                    overlay.setSelected(false);
                }
            }
            // trigger onBlur before onFocus !!
            if (overlayLoosingFocus != null) {
                overlayLoosingFocus.onSelectStateChanged(false);
            }

            if (overlayWinningFocus != null) {
                overlayWinningFocus.onSelectStateChanged(true);
            }
        }

        osmView.postInvalidate();
        return stopSingleTapUpPropagation;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onLongPress(android.view.MotionEvent,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    public final boolean onLongPress(final MotionEvent event, final MapView osmv) {

        // TJHU à implémenter pour gérer le focus lorsque clic long ?
        return super.onLongPress(event, osmv);
    }

}
