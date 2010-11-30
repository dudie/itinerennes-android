package fr.itinerennes.ui.views.overlays;

import java.util.List;

import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import fr.itinerennes.R;
import fr.itinerennes.beans.Station;
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
            LOGGER.debug("onSingleTapUp");
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

}
