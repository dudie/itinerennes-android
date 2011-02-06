package fr.itinerennes.ui.views.overlays;

/**
 * Classes implementing this interface may receive events from a {@link GroupSelectOverlay} when an
 * item is focused.
 * 
 * @param <D>
 *            the type of data bundled with the overlay item
 * @author Jérémie Huchet
 */
public interface SelectableOverlay<D> extends WrappableOverlay {

    /**
     * Returns the focused state of this overlay.
     * 
     * @return true if the overlay has the focus, else false
     */
    boolean hasFocus();

    /**
     * Set the focus state of the overlay.
     * 
     * @param hasFocus
     *            true if the overlay has focus, else false
     */
    void setSelected(boolean hasFocus);

    /**
     * Triggered method by a {@link GroupSelectOverlay} when the selected state changes.
     * 
     * @param selected
     *            true if selected
     * @return true to stop the propagation of the event
     */
    boolean onSelectStateChanged(boolean selected);

}
