package fr.itinerennes.ui.views.overlays.old;

import org.osmdroid.util.GeoPoint;

import android.graphics.drawable.Drawable;

/**
 * Base class for {@link SelectableOverlay} items.
 * 
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class SelectableMarker<D> extends Marker<D> {

    /**
     * Create an item for a {@link SelectableOverlay}.
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
     * Should be implemented by subclasses to defines item behavior when its selected state changes.
     * 
     * @param selected
     *            the new selected state of the item
     * @return true will stop the event propagation and means the event has been completely handled
     */
    public boolean onSelectStateChange(final boolean selected) {

        return false;
    }

    @Override
    public Drawable getMarker(final int stateBitset) {

        return getDrawable();
    }
}
