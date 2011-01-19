package fr.itinerennes.ui.views;

import org.andnav.osm.views.OpenStreetMapViewController;

import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.views.overlays.MapOverlayHelper;

/**
 * A controller for {@link MapView}. Provides convenience methods to manipulates the map displayed
 * to the user screen.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class MapViewController extends OpenStreetMapViewController {

    /** The map overlay helper to use with the map associated with this controller. */
    private final MapOverlayHelper mapOverlayHelper;

    /**
     * @param map
     *            the map view to which associate the controller
     */
    public MapViewController(final ITRContext context, final MapView map) {

        super(map);

        mapOverlayHelper = new MapOverlayHelper(context, map);
    }

    /**
     * Gets a reference to the map overlay helper.
     * 
     * @return a reference to the map overlay helper
     */
    public MapOverlayHelper getMapOverlayHelper() {

        return mapOverlayHelper;
    }

}
