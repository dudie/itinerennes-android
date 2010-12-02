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
import fr.itinerennes.beans.BusStation;
import fr.itinerennes.beans.Station;
import fr.itinerennes.business.facade.BikeService;
import fr.itinerennes.business.facade.BusService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.views.MapView;
import fr.itinerennes.ui.views.overlays.StationOverlay;
import fr.itinerennes.ui.views.overlays.StationOverlayItem;

/**
 * A class derivating from ASyncTask to refresh a bus overlay in background.
 * 
 * @author Olivier Boudet
 */
public class BuildOverlayTask extends AsyncTask<BoundingBox, Void, Void> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BuildOverlayTask.class);

    /** The android context. */
    private final Context context;

    /** The map view on which update bus overlay. */
    private final MapView map;

    /** The type of the overlay to refresh */
    private final int type;

    /**
     * Constructor.
     * 
     * @param ctx
     *            An android context
     * @param map
     *            The map view on which update bus overlay
     */
    public BuildOverlayTask(final Context ctx, final MapView map, final int type) {

        this.context = ctx;
        this.map = map;
        this.type = type;
    }

    /**
     * Fetch in background the list of bus stations within the bounding box and creates an overlay.
     * 
     * @param params
     *            Bounding box used to refresh the overlay
     * @return Void Returns nothing
     */
    @Override
    protected final Void doInBackground(final BoundingBox... params) {

        try {
            List<StationOverlayItem> stations = null;
            switch (type) {
            case Station.TYPE_BUS:
                stations = getBusStationOverlayItemsFromBbox(params[0]);
                break;
            case Station.TYPE_BIKE:
                stations = getBikeStationOverlayItems();
                break;
            default:
                break;
            }

            final StationOverlay<StationOverlayItem> overlay = new StationOverlay<StationOverlayItem>(
                    context, stations, map.getOnItemGestureListener(), type);

            map.refreshOverlay(overlay, type);

        } catch (final GenericException e) {
            LOGGER.error("error while trying to fetch stations.", e);
        }
        return null;
    }

    /**
     * Gets all bus stations within the passed bbox from WFS API and returns a list of station
     * overlay items.
     * 
     * @return list of station overlay items
     * @throws GenericException
     *             network exception during request
     * @param bbox
     *            Bounding Box used to refresh the overlay
     */
    private List<StationOverlayItem> getBusStationOverlayItemsFromBbox(final BoundingBox bbox)
            throws GenericException {

        final List<BusStation> busStations = BusService.getBusStationsFromBbox(bbox);
        final List<StationOverlayItem> overlayItems = new ArrayList<StationOverlayItem>();

        for (final BusStation station : busStations) {
            final StationOverlayItem item = new StationOverlayItem(station);
            item.setMarker(context.getResources().getDrawable(R.drawable.icon_bus));

            overlayItems.add(item);
        }
        return overlayItems;

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
