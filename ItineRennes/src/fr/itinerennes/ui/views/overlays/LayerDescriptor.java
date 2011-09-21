package fr.itinerennes.ui.views.overlays;

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
