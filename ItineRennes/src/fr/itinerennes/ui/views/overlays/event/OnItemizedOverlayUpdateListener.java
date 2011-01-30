package fr.itinerennes.ui.views.overlays.event;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;

import fr.itinerennes.ui.views.overlays.Marker;

/**
 * @author Jérémie Huchet
 */
public interface OnItemizedOverlayUpdateListener {

    void onContentUpdated(BoundingBoxE6 oldBbox, BoundingBoxE6 newBbox, List<Marker<?>> items);
}
