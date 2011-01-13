package fr.itinerennes.ui.tasks;

import java.util.ArrayList;
import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.os.AsyncTask;

import fr.itinerennes.business.facade.StationProvider;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Station;
import fr.itinerennes.ui.views.MapView;
import fr.itinerennes.ui.views.overlays.StationOverlay;
import fr.itinerennes.ui.views.overlays.StationOverlayItem;

/**
 * A class derivating from ASyncTask to refresh a bus overlay in background.
 * 
 * @author Olivier Boudet
 */
public class BuildOverlayTask extends
        AsyncTask<BoundingBoxE6, Void, StationOverlay<StationOverlayItem>> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BuildOverlayTask.class);

    /** The android context. */
    private final Context context;

    /** The map view on which update bus overlay. */
    private final MapView map;

    /** The type of station refreshed. */
    private final int type;

    /** The station provider to use to retrieve the stations to display. */
    private final StationProvider<Station> stationProvider;

    private final StationOverlay<StationOverlayItem> overlay;

    /**
     * Constructor.
     * 
     * @param ctx
     *            An android context
     * @param map
     *            The map view on which update bus overlay
     * @param stationProvider
     *            the station provider to use to retrieve the stations to display
     * @param type
     *            the type of station refreshed
     */
    public BuildOverlayTask(final Context ctx, final MapView map,
            final StationProvider<Station> stationProvider, final int type,
            final StationOverlay<StationOverlayItem> overlay) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("BuildOverlayTask.create - type={}", type);
        }
        this.context = ctx;
        this.map = map;
        this.stationProvider = stationProvider;
        this.type = type;
        this.overlay = overlay;
    }

    /**
     * Fetch in background the list of bus stations within the bounding box and creates an overlay.
     * 
     * @param params
     *            Bounding box used to refresh the overlay
     * @return an overlay containing items located in the bounding box
     */
    @Override
    protected final StationOverlay<StationOverlayItem> doInBackground(final BoundingBoxE6... params) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.start - type={}, bbox={}", type, null != params
                    && params.length > 0 ? params[0].toString() : null);
        }

        List<Station> stations = null;
        try {
            stations = stationProvider.getStations(params[0]);
        } catch (final GenericException e) {
            LOGGER.error("error while trying to fetch stations.", e);
        }

        if (null != stations) {
            final List<StationOverlayItem> overlayItems = new ArrayList<StationOverlayItem>();

            for (final Station station : stations) {
                final StationOverlayItem item = new StationOverlayItem(station);
                item.setMarker(context.getResources().getDrawable(station.getIconDrawableId()));

                overlayItems.add(item);
            }
            overlay.removeUnvisibleItems(map.getVisibleBoundingBoxE6());
            overlay.addItems(overlayItems);

        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.end - type={}, {} stations", type,
                    null != stations ? stations.size() : 0);
        }
        return overlay;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(final StationOverlay<StationOverlayItem> overlay) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPostExecute.start - type={}", type);
        }

        map.postInvalidate();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPostExecute.end - type={}", type);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#onCancelled()
     */
    @Override
    protected void onCancelled() {

        super.onCancelled();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCancelled.start/end - type={}", type);
        }
    }
}
