package fr.itinerennes.ui.views;

import org.osmdroid.views.MapController;

import fr.itinerennes.ui.activity.ItineRennesActivity;

/**
 * A controller for {@link ITRMapView}. Provides convenience methods to manipulates the map
 * displayed to the user screen.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class MapViewController extends MapController {

    /**
     * @param map
     *            the map view to which associate the controller
     */
    public MapViewController(final ItineRennesActivity context, final ItinerennesMapView map) {

        super(map);

    }

}
