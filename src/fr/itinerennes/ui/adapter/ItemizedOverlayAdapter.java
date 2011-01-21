package fr.itinerennes.ui.adapter;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Marker;

import fr.itinerennes.ui.views.ITRMapView;
import fr.itinerennes.ui.views.overlays.ITROverlayItem;

/**
 * Classes implementing this interface are bridges between a {@link ITRMapView} and the
 * {@link Marker} displayed.
 * 
 * @param <T>
 *            the type of the items of this overlay
 * @param <T>
 *            the type of overlay items this adapter returns
 * @author Jérémie Huchet
 */
public interface ItemizedOverlayAdapter<T extends ITROverlayItem<D>, D> {

    /**
     * This method is called by the MapView when it wants to find the markers located in the given
     * bounding box.
     * 
     * @param bbox
     *            the requested bounding box
     * @return a list of markers located in the requested bounding box
     */
    List<T> getItems(final BoundingBoxE6 bbox);
}
