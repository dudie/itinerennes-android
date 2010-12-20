package fr.itinerennes.ui.tasks;

import java.util.ArrayList;
import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.os.AsyncTask;

import fr.itinerennes.R;
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
    private final StationProvider stationProvider;

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
            final StationProvider stationProvider, final int type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("BuildOverlayTask.create - type={}", type);
        }
        this.context = ctx;
        this.map = map;
        this.stationProvider = stationProvider;
        this.type = type;
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
            LOGGER.debug("doInBackground.start - bbox={}",
                    null != params && params.length > 0 ? params[0].toString() : null);
        }

        List<Station> stations = null;
        try {
            stations = stationProvider.getStations(params[0]);
        } catch (final GenericException e) {
            LOGGER.error("error while trying to fetch stations.", e);
        }

        final StationOverlay<StationOverlayItem> overlay;

        if (null != stations) {
            final List<StationOverlayItem> overlayItems = new ArrayList<StationOverlayItem>();

            for (final Station station : stations) {
                final StationOverlayItem item = new StationOverlayItem(station);

                switch (type) {
                case Station.TYPE_BUS:
                    item.setMarker(context.getResources().getDrawable(R.drawable.icon_bus));
                    break;
                case Station.TYPE_BIKE:
                    item.setMarker(context.getResources().getDrawable(R.drawable.icon_bike));
                    break;
                case Station.TYPE_SUBWAY:
                    item.setMarker(context.getResources().getDrawable(R.drawable.icon_subway_16));
                    break;
                default:
                    // TJHU faire une icone par défaut et l'associer au marqueur
                    break;
                }

                overlayItems.add(item);
            }
            overlay = new StationOverlay<StationOverlayItem>(context, overlayItems,
                    map.getOnItemGestureListener(), type);

        } else {
            overlay = null;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.end - {} stations", null != stations ? stations.size() : 0);
        }
        return overlay;
    }

    @Override
    protected void onPostExecute(final StationOverlay<StationOverlayItem> overlay) {

        map.refreshOverlay(overlay, type);

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
    // private List<StationOverlayItem> getBusStationOverlayItemsFromBbox(final BoundingBoxE6 bbox)
    // throws GenericException {
    //
    // final List<BusStation> busStations = BusService.getBusStationsFromBbox(bbox);
    // final List<StationOverlayItem> overlayItems = new ArrayList<StationOverlayItem>();
    //
    // for (final BusStation station : busStations) {
    // final StationOverlayItem item = new StationOverlayItem(station);
    // item.setMarker(context.getResources().getDrawable(R.drawable.icon_bus));
    //
    // overlayItems.add(item);
    // }
    // return overlayItems;
    //
    // }

    /**
     * Gets all bike stations from Keolis API and returns a list of station overlay items.
     * 
     * @return list of station overlay items
     * @throws GenericException
     *             network exception during request
     */
    // private List<StationOverlayItem> getBikeStationOverlayItems() throws GenericException {
    //
    // final List<BikeStation> bikeStations = BikeStationProvider.getAllStations();
    // final List<StationOverlayItem> overlayItems = new ArrayList<StationOverlayItem>();
    //
    // for (final BikeStation station : bikeStations) {
    // final StationOverlayItem item = new StationOverlayItem(station);
    // item.setMarker(context.getResources().getDrawable(R.drawable.icon_velo));
    //
    // overlayItems.add(item);
    // }
    // return overlayItems;
    //
    // }
}
