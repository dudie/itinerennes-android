package fr.itinerennes.ui.tasks;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.os.AsyncTask;
import fr.itinerennes.R;
import fr.itinerennes.beans.BikeStation;
import fr.itinerennes.beans.BoundingBox;
import fr.itinerennes.beans.Station;
import fr.itinerennes.business.facade.BikeService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.views.MapView;
import fr.itinerennes.ui.views.overlays.StationOverlay;
import fr.itinerennes.ui.views.overlays.StationOverlayItem;

/**
 * A class derivating from ASyncTask to refresh a bike overlay in background.
 * 
 * @author Olivier Boudet
 */
public class RefreshBikeOverlayTask extends AsyncTask<BoundingBox, Void, Void> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(RefreshBikeOverlayTask.class);

    /** The android context. */
    private final Context context;

    /** The map view on which update bike overlay. */
    private final MapView map;

    /**
     * Constructor.
     * 
     * @param ctx
     *            An android context
     * @param map
     *            The map view on which update bike overlay
     */
    public RefreshBikeOverlayTask(final Context ctx, final MapView map) {

        this.context = ctx;
        this.map = map;
    }

    /**
     * Fetch in background the list of bike stations and creates an overlay.
     * 
     * @param params
     *            Bounding box used to refresh the overlay
     * @return Void Returns nothing
     */
    @Override
    protected final Void doInBackground(final BoundingBox... params) {

        try {
            final List<StationOverlayItem> bikeStations = getBikeStationOverlayItems();

            final StationOverlay<StationOverlayItem> bikeOverlay = new StationOverlay<StationOverlayItem>(
                    context, bikeStations, map.getOnItemGestureListener(), Station.TYPE_BIKE);

            map.refreshOverlay(bikeOverlay, Station.TYPE_BIKE);
        } catch (final GenericException e) {
            LOGGER.error("error while trying to fetch bus stations from WFS.", e);
        }

        return null;
    }

    /**
     * Gets all bike stations from Keolis API and returns a list of station overlay items.
     * 
     * @return list of station overlay items
     * @throws GenericException
     *             network exception during request
     */
    private List<StationOverlayItem> getBikeStationOverlayItems() throws GenericException {

        final List<BikeStation> bikeStations = BikeService.getAllStations();
        final List<StationOverlayItem> overlayItems = new ArrayList<StationOverlayItem>();

        for (final BikeStation station : bikeStations) {
            final StationOverlayItem item = new StationOverlayItem(station);
            item.setMarker(context.getResources().getDrawable(R.drawable.icon_velo));

            overlayItems.add(item);
        }
        return overlayItems;

    }
}
