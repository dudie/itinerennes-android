package fr.itinerennes.ui.tasks;

import java.util.ArrayList;
import java.util.List;

import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
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

public class RefreshBikeOverlayTask extends
        AsyncTask<BoundingBox, Void, StationOverlay<StationOverlayItem>> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(RefreshBikeOverlayTask.class);

    /** The android context */
    private Context context;

    /** The map view on which update bike overlay */
    private MapView map;

    /** Listener used by the bike overlay to trigger item taps. */
    private OnItemGestureListener<StationOverlayItem> listener;

    /**
     * Constructor
     * 
     * @param ctx
     *            An android context
     * @param map
     *            The map view on which update bike overlay
     * @param listener
     *            Listener used by the bus overlay to trigger item taps
     */
    public RefreshBikeOverlayTask(Context ctx, MapView map,
            OnItemGestureListener<StationOverlayItem> listener) {

        this.context = ctx;
        this.map = map;
        this.listener = listener;
    }

    @Override
    protected StationOverlay<StationOverlayItem> doInBackground(BoundingBox... params) {

        try {
            List<StationOverlayItem> bikeStations = getBikeStationOverlayItems();

            return new StationOverlay<StationOverlayItem>(context, bikeStations, listener,
                    Station.TYPE_BUS);
        } catch (GenericException e) {
            LOGGER.error("error while trying to fetch bus stations from WFS.");
        }

        return null;
    }

    @Override
    protected void onPostExecute(StationOverlay<StationOverlayItem> result) {

        for (OpenStreetMapViewOverlay overlay : this.map.getOverlays()) {
            if (overlay instanceof StationOverlay) {
                if (((StationOverlay) overlay).getType() == Station.TYPE_BIKE) {
                    this.map.getOverlays().remove(overlay);
                }
            }
        }
        this.map.getOverlays().add(result);
        this.map.postInvalidate();
    }

    /**
     * Gets all bike stations from Keolis API and returns a list of station overlay items.
     * 
     * @return list of station overlay items
     * @throws GenericException
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
