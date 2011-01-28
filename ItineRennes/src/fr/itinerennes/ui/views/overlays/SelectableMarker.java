package fr.itinerennes.ui.views.overlays;

import org.osmdroid.util.GeoPoint;

import android.graphics.drawable.Drawable;

/**
 * Base class for {@link FocusableOverlay} items.
 * 
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class SelectableMarker<D> extends Marker<D> {

    /**
     * Create an item for a {@link FocusableOverlay}.
     * 
     * @param title
     *            the title of the item
     * @param location
     *            coordinates defining the location of the marker on the map
     */
    public SelectableMarker(final String title, final GeoPoint location) {

        super(title, location);
    }

    /**
     * Should be implemented by subclasses to defines item behavior when it wins focus.
     * 
     * @return true will stop the event propagation and means the event has been completely handled
     */
    public boolean onFocus() {

        return false;
    }

    /**
     * Should be implemented by subclasses to defines item behavior when it looses focus.
     * 
     * @return true will stop the event propagation and means the event has been completely handled
     */
    public boolean onBlur() {

        return false;
    }

    @Override
    public Drawable getMarker(final int stateBitset) {

        return getDrawable();
    }
}
