package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.andnav.osm.events.MapListener;
import org.andnav.osm.events.ScrollEvent;
import org.andnav.osm.events.ZoomEvent;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.graphics.Canvas;

import fr.itinerennes.ui.adapter.BusTimeAdapter;
import fr.itinerennes.ui.adapter.ItemizedOverlayAdapter;
import fr.itinerennes.ui.tasks.UpdateOverlayTask;
import fr.itinerennes.ui.views.MapListenerWrapper;
import fr.itinerennes.ui.views.MapView;

/**
 * An enhanced itemized overlay which can use an {@link ItemizedOverlayAdapter} to update itself
 * when receiving map events.
 * <p>
 * It needs to ba attached as a {@link MapListener} to the {@link OpenStreetMapView}. You can use a
 * {@link MapListenerWrapper} to register multiple map listeners to a same map view.
 * 
 * @see MapListenerWrapper
 * @param <T>
 *            the type of the items of this overlay
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class ItemizedOverlay<T extends OverlayItem<D>, D> extends
        OpenStreetMapViewItemizedOverlay<T> implements MapListener {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BusTimeAdapter.class);

    /** The adapter to use to provide items. */
    private final ItemizedOverlayAdapter<T, D> adapter;

    /** The async task in charge for managing overlay updates. */
    private UpdateOverlayTask<T, D> overlayUpdater = null;

    /**
     * Creates the itemized overlay.
     * 
     * @param context
     *            a context
     * @param adapter
     *            the adapter this overlay should use to update its content when the map moves
     */
    public ItemizedOverlay(final Context context, final ItemizedOverlayAdapter<T, D> adapter) {

        super(context, new ArrayList<T>(), null);
        this.adapter = adapter;
    }

    /**
     * Removes all items from the overlay.
     */
    public void clear() {

        super.mItemList.clear();
    }

    /**
     * Adds the given items to the overlay.
     * 
     * @param items
     *            the items to add to the overlay
     */
    public void addAll(final List<T> items) {

        super.mItemList.addAll(items);
    }

    @Override
    public void onDraw(final Canvas canvas, final OpenStreetMapView mapView) {

        super.onDraw(canvas, mapView);
    }

    @Override
    protected void onDrawFinished(final Canvas c, final OpenStreetMapView osmv) {

        super.onDrawFinished(c, osmv);
    }

    /**
     * When this overlay is linked to a {@link MapView}, this method is triggered when the map
     * scrolls.
     * <p>
     * When it occurs, the overlay use its adapter and update its content.
     * 
     * @param event
     *            the scroll event
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public final boolean onScroll(final ScrollEvent event) {

        updateOverlay(event.getSource());
        return false;
    }

    /**
     * When this overlay is linked to a {@link MapView}, this method is triggered when the map zoom
     * changes.
     * <p>
     * When it occurs, the overlay use its adapter and update its content.
     * 
     * @param event
     *            the zoom event
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public final boolean onZoom(final ZoomEvent event) {

        updateOverlay(event.getSource());
        return false;
    }

    /**
     * Creates and starts an {@link UpdateOverlayTask} to refresh the overlay's content. If a
     * previous task was working, it is canceled and then the new one starts.
     * 
     * @param osmView
     *            the open street map view
     */
    protected void updateOverlay(final OpenStreetMapView osmView) {

        if (null != overlayUpdater) {
            overlayUpdater.cancel(true);
        }
        overlayUpdater = new UpdateOverlayTask<T, D>(osmView, this, adapter);
        overlayUpdater.execute(osmView.getVisibleBoundingBoxE6());
    }
}
