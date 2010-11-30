package fr.itinerennes.ui.tasks;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.os.AsyncTask;
import fr.itinerennes.R;
import fr.itinerennes.beans.BoundingBox;
import fr.itinerennes.beans.BusStation;
import fr.itinerennes.beans.Station;
import fr.itinerennes.business.facade.BusService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.views.MapView;
import fr.itinerennes.ui.views.overlays.StationOverlay;
import fr.itinerennes.ui.views.overlays.StationOverlayItem;

public class RefreshBusOverlayTask extends AsyncTask<BoundingBox, Void, Void> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(RefreshBusOverlayTask.class);

    /** The android context. */
    private Context context;

    /** The map view on which update bus overlay. */
    private MapView map;

    /**
     * Constructor.
     * 
     * @param ctx
     *            An android context
     * @param map
     *            The map view on which update bus overlay
     * @param listener
     *            Listener used by the bus overlay to trigger item taps
     */
    public RefreshBusOverlayTask(Context ctx, MapView map) {

        this.context = ctx;
        this.map = map;
    }

    /**
     * Fetch in background the list of bus stations within the bounding box and creates an overlay.
     */
    @Override
    protected Void doInBackground(BoundingBox... params) {

        try {
            final List<StationOverlayItem> busStations = getBusStationOverlayItemsFromBbox(params[0]);

            final StationOverlay<StationOverlayItem> busOverlay = new StationOverlay<StationOverlayItem>(
                    context, busStations, map.getOnItemGestureListener(), Station.TYPE_BUS);

            map.refreshOverlay(busOverlay, Station.TYPE_BUS);

        } catch (final GenericException e) {
            LOGGER.error("error while trying to fetch bus stations from WFS.", e);
        }
        return null;
    }

    /**
     * Gets all bus stations within the passed bbox from WFS API and returns a list of station
     * overlay items.
     * 
     * @return list of station overlay items
     * @throws GenericException
     */
    private List<StationOverlayItem> getBusStationOverlayItemsFromBbox(BoundingBox bbox)
            throws GenericException {

        final List<BusStation> busStations = BusService.getBusStationsFromBbox(bbox);
        final List<StationOverlayItem> overlayItems = new ArrayList<StationOverlayItem>();

        for (BusStation station : busStations) {
            final StationOverlayItem item = new StationOverlayItem(station);
            item.setMarker(context.getResources().getDrawable(R.drawable.icon_bus));

            overlayItems.add(item);
        }
        return overlayItems;

    }
}
