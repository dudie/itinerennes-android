package fr.itinerennes.ui.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.AsyncTask.Status;
import android.view.View;

import fr.itinerennes.R;
import fr.itinerennes.keolis.model.BikeStation;
import fr.itinerennes.keolis.model.SubwayStation;
import fr.itinerennes.onebusaway.model.Stop;
import fr.itinerennes.ui.activity.ItinerennesContext;
import fr.itinerennes.ui.adapter.BikeStationBoxAdapter;
import fr.itinerennes.ui.adapter.BusStationBoxAdapter;
import fr.itinerennes.ui.adapter.SubwayStationBoxAdapter;
import fr.itinerennes.ui.tasks.DisplayMapBoxTask;
import fr.itinerennes.ui.views.overlays.MarkerOverlayItem;

/**
 * Manage displaying of the map box additional informations view.
 * 
 * @author Jérémie Huchet
 */
public class MapBoxController {

    // TJHU attacher une instance de cette classe dans le contexte de mapactivity

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MapBoxController.class);

    /** The itinerennes context. */
    private final ItinerennesContext context;

    /** The map box view. */
    private MapBoxView mapBox;

    /** The current selected item. */
    private MarkerOverlayItem selectedItem;

    /**
     * The task used to fill the map box view with additional information in background for a bike
     * station.
     */
    private DisplayMapBoxTask<BikeStation> bikeMapBoxDisplayer = null;

    /**
     * The task used to fill the map box view with additional information in background for a bus
     * station.
     */
    private DisplayMapBoxTask<Stop> busMapBoxDisplayer = null;

    /**
     * The task used to fill the map box view with additional information in background for a bus
     * station.
     */
    private DisplayMapBoxTask<SubwayStation> subwayMapBoxDisplayer = null;

    /**
     * @param mapBox
     */
    public MapBoxController(final ItinerennesContext context) {

        this.context = context;

    }

    public void show(final MarkerOverlayItem item) {

        if (this.mapBox == null) {
            this.mapBox = (MapBoxView) this.context.findViewById(R.id.map_box);
        }

        cancelAll();

        if (item.getType().equals("BIKE")) {
            bikeMapBoxDisplayer = new DisplayMapBoxTask<BikeStation>(mapBox,
                    new BikeStationBoxAdapter(context), item);
            bikeMapBoxDisplayer.execute();
        } else if (item.getType().equals("BUS")) {
            busMapBoxDisplayer = new DisplayMapBoxTask<Stop>(mapBox, new BusStationBoxAdapter(
                    context), item);
            busMapBoxDisplayer.execute();
        } else if (item.getType().equals("SUBWAY")) {
            subwayMapBoxDisplayer = new DisplayMapBoxTask<SubwayStation>(mapBox,
                    new SubwayStationBoxAdapter(context), item);
            subwayMapBoxDisplayer.execute();
        }

        selectedItem = item;

    }

    public void hide() {

        cancelAll();
        selectedItem = null;

        if (this.mapBox != null) {
            this.mapBox.setVisibility(View.GONE);
        }
    }

    private void cancelAll() {

        if (bikeMapBoxDisplayer != null && bikeMapBoxDisplayer.getStatus() != Status.FINISHED) {
            bikeMapBoxDisplayer.cancel(true);
        }

        if (busMapBoxDisplayer != null && busMapBoxDisplayer.getStatus() != Status.FINISHED) {
            busMapBoxDisplayer.cancel(true);
        }

        if (subwayMapBoxDisplayer != null && subwayMapBoxDisplayer.getStatus() != Status.FINISHED) {
            subwayMapBoxDisplayer.cancel(true);
        }
    }

    /**
     * Gets the selected item.
     * 
     * @return the selected item
     */
    public MarkerOverlayItem getSelectedItem() {

        return selectedItem;
    }
}
