package fr.itinerennes.ui.tasks;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.views.MapView;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.os.AsyncTask;

import fr.itinerennes.ui.adapter.ItemizedOverlayAdapter;
import fr.itinerennes.ui.views.overlays.ITRItemizedOverlay;
import fr.itinerennes.ui.views.overlays.ITROverlayItem;

/**
 * A class derivating from ASyncTask to refresh an overlay in background.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */
public class UpdateOverlayTask<T extends ITROverlayItem<D>, D> extends
        AsyncTask<BoundingBoxE6, Void, List<T>> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(UpdateOverlayTask.class);

    /** The map view to redraw. */
    private final MapView osmView;

    /** The map overlay to update. */
    private final ITRItemizedOverlay<T, D> overlay;

    /** The map marker adapter to use. */
    private final ItemizedOverlayAdapter<T, D> mapMarkerAdapter;

    /**
     * Constructor.
     * 
     * @param overlay
     *            the itemized overlay to update
     * @param mapMarkerAdapter
     *            the map marker adapter to use
     */
    public UpdateOverlayTask(final MapView osmView, final ITRItemizedOverlay<T, D> overlay,
            final ItemizedOverlayAdapter<T, D> mapMarkerAdapter) {

        this.osmView = osmView;
        this.overlay = overlay;
        this.mapMarkerAdapter = mapMarkerAdapter;
    }

    /**
     * Fetch in background the list of bus stations within the bounding box and creates an overlay.
     * 
     * @param params
     *            Bounding box used to refresh the overlay
     * @return an overlay containing items located in the bounding box
     */
    @Override
    protected final List<T> doInBackground(final BoundingBoxE6... params) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{}.doInBackground.start - bbox={}", Thread.currentThread().getName(),
                    null != params && params.length > 0 ? params[0].toString() : null);
        }

        final List<T> items = mapMarkerAdapter.getItems(params[0]);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{}.doInBackground.end - {} stations", Thread.currentThread().getName(),
                    null != items ? items.size() : 0);
        }

        return items;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected final void onPostExecute(final List<T> items) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPostExecute.start - {} overlay items", null != items ? items.size() : 0);
        }

        overlay.onReplaceItems(osmView, items);
        osmView.postInvalidate();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPostExecute.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#onCancelled()
     */
    @Override
    protected final void onCancelled() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCancelled.start/end");
        }
    }
}
