package fr.itinerennes.ui.views.overlays;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

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
