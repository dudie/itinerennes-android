package fr.itinerennes.ui.views.overlays;

import fr.itinerennes.ui.adapter.MapBoxAdapter;
import fr.itinerennes.ui.views.MapBoxView;

/**
 * Classes implementing this interface may receive events from a {@link GroupFocusOverlay} when an
 * item is focused.
 * 
 * @param <D>
 *            the type of data bundled with the overlay item
 * @author Jérémie Huchet
 */
public interface FocusableOverlay<D> extends WrappableOverlay {

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
    void setFocused(boolean hasFocus);

    /**
     * Triggered method by a {@link GroupFocusOverlay} when this overlay wins focus.
     * 
     * @param additionalInformationView
     *            the view containing additional informations about the focused item
     * @return true if the focus event was completely handled and its propagation should be stopped
     */
    boolean onFocus(MapBoxView additionalInformationView);

    /**
     * Triggered method by a {@link GroupFocusOverlay} when this overlay looses focus.
     * 
     * @param additionalInformationView
     *            the view containing additional informations about the focused item
     * @return true if the blur event was completely handled and its propagation should be stopped
     */
    boolean onBlur(MapBoxView additionalInformationView);

    /**
     * Triggered method by a {@link GroupFocusOverlay} when this overlay keeps the focus.
     * 
     * @param additionalInformationView
     *            the view containing additional informations about the focused item
     * @return true if the keep focus event was completely handled and its propagation should be
     *         stopped
     */
    boolean onKeepFocus(MapBoxView additionalInformationView);

    /**
     * Returns a map box adapter.
     * 
     * @return the map box adapter to use to display item additional information
     */
    MapBoxAdapter<OverlayItem<D>, D> getMapBoxAdapter();

}
