package fr.itinerennes.ui.views.overlays.event;

import java.util.List;

import fr.itinerennes.ui.views.overlays.Marker;

/**
 * @author Jérémie Huchet
 */
public interface OnItemizedOverlayUpdateListener<D> {

    <T extends Marker<D>> void onContentUpdated(final List<T> items);
}
