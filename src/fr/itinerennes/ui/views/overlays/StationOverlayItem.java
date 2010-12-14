package fr.itinerennes.ui.views.overlays;

import org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem;

import fr.itinerennes.model.Station;
import fr.itinerennes.ui.views.MapView;

/**
 * Represents a station displayed on the {@link MapView} inside a {@link StationOverlay}.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class StationOverlayItem extends OpenStreetMapViewOverlayItem {

    /** The station denoted by this item. */
    private Station station;

    /**
     * Creates the station overlay item.
     * 
     * @param the
     *            station denoted by this item
     */
    public StationOverlayItem(final Station station) {

        super(station.getName(), null, station.getGeoPoint());
        this.station = station;
    }

    /**
     * Gets the station associated to this overlay item.
     * 
     * @return the station
     */
    public Station getStation() {

        return station;
    }

}
