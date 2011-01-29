package fr.itinerennes.ui.views.overlays;

/**
 * Overlays implementing this interface may be shown and hidden.
 * 
 * @author Jérémie Huchet
 */
public interface ToggleableOverlay {

    /**
     * Gets the localized name of the overlay.
     * 
     * @return the localized name of the overlay
     */
    String getLocalizedName();

    /**
     * Gets the enabled state of the overlay.
     * 
     * @return true if this overlay is enabled or false
     */
    boolean isEnabled();

    /**
     * Set the enabled state of the overlay.
     * 
     * @param enabled
     *            true if this overlay must be enabled
     */
    void setEnabled(boolean enabled);
}
