package fr.itinerennes.ui.views.overlays;

import java.util.List;

/**
 * Interface to manage the map layer selection.
 * 
 * @author Olivier Boudet
 */
public interface ILayerSelector {

    /**
     * Gets the names of layer labels to display to the user.
     * 
     * @return a map with the layer type as the key and the layer label as the value
     */
    List<LayerDescriptor> getLayersDescriptors();

    /**
     * Show a layer.
     * 
     * @param type
     *            type of the layer to show.
     */
    void show(final String type);

    /**
     * Hide a layer.
     * 
     * @param type
     *            type of the layer to hide.
     */
    void hide(final String type);

    /**
     * Gets the marker type visibility ?
     * 
     * @param type
     *            the type of the marker to get the visibility, or null if the overlay manage only
     *            one type of marker
     * @return true if the marker type is visible
     */
    boolean isVisible(final String type);

}
