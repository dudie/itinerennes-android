package fr.itinerennes.ui.adapter;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;

import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.views.ItinerennesMapView;
import fr.itinerennes.ui.views.overlays.old.Marker;

/**
 * Classes implementing this interface are bridges between a {@link ItinerennesMapView} and the marker
 * displayed.
 * 
 * @param <T>
 *            a subclass of Marker
 * @author Jérémie Huchet
 */
public interface ItemizedOverlayAdapter<T extends Marker<?>> {

    /**
     * This method is called by the MapView when it wants to find the markers located in the given
     * bounding box.
     * 
     * @param bbox
     *            the requested bounding box
     * @return a list of markers located in the requested bounding box
     * @throws GenericException
     *             if an error occurs
     */
    List<T> getItems(final BoundingBoxE6 bbox) throws GenericException;
}
