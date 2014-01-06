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

/**
 * @author Olivier Boudet
 */
public class LayerDescriptor {

    /** The label of the layer. */
    private String label;

    /** The visibility of the layer. */
    private final boolean visible;

    /** The overlay containing this layer. */
    private final ILayerSelector overlay;

    /** The type of marker for this layer. */
    private String type;

    /**
     * Constructor.
     * 
     * @param overlay
     *            the overlay containing the layer
     * @param label
     *            the label
     * @param type
     *            the type of the layer
     * @param visible
     *            the visibility
     */
    public LayerDescriptor(final ILayerSelector overlay, final String label, final String type,
            final boolean visible) {

        this.overlay = overlay;
        this.label = label;
        this.visible = visible;
        this.type = type;
    }

    /**
     * Gets the overlay.
     * 
     * @return the overlay
     */
    public final ILayerSelector getOverlay() {

        return overlay;
    }

    /**
     * Gets the label.
     * 
     * @return the label
     */
    public final String getLabel() {

        return label;
    }

    /**
     * Sets the label.
     * 
     * @param label
     *            the label to set
     */
    public final void setLabel(final String label) {

        this.label = label;
    }

    /**
     * Gets the visible.
     * 
     * @return the visible
     */
    public final boolean isVisible() {

        return visible;
    }

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public final String getType() {

        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the type to set
     */
    public final void setType(final String type) {

        this.type = type;
    }

}
