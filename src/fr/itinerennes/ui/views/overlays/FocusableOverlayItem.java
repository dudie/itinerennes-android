package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;

import org.andnav.osm.util.GeoPoint;

import android.graphics.drawable.Drawable;

/**
 * Base class for {@link FocusableOverlay} items.
 * 
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class FocusableOverlayItem<D> extends OverlayItem<D> {

    /**
     * Create an item for a {@link FocusableOverlay}.
     * 
     * @param title
     *            the title of the item
     * @param location
     *            coordinates defining the location of the marker on the map
     */
    public FocusableOverlayItem(final String title, final GeoPoint location) {

        super(title, location);
    }

    /**
     * Should be implemented by subclasses to defines item behavior when it wins focus.
     * 
     * @return true will stop the event propagation and means the event has been completely handled
     */
    public boolean onFocus() {

        final int[] actualStates = getDrawable().getState();
        final ArrayList<Integer> newStates = new ArrayList<Integer>(actualStates.length + 1);
        for (final int state : actualStates) {
            if (state != android.R.attr.state_pressed) {
                newStates.add(state);
            }
        }
        newStates.add(android.R.attr.state_pressed);
        final int[] newStatesArray = new int[newStates.size()];
        for (int i = 0; i < newStatesArray.length; i++) {
            newStatesArray[i] = newStates.get(i);
        }
        getDrawable().setState(newStatesArray);
        return false;
    }

    /**
     * Should be implemented by subclasses to defines item behavior when it looses focus.
     * 
     * @return true will stop the event propagation and means the event has been completely handled
     */
    public boolean onBlur() {

        final int[] actualStates = getDrawable().getState();
        final ArrayList<Integer> newStates = new ArrayList<Integer>(actualStates.length);
        for (final int state : actualStates) {
            if (state != android.R.attr.state_pressed) {
                newStates.add(state);
            }
        }
        final int[] newStatesArray = new int[newStates.size()];
        for (int i = 0; i < newStatesArray.length; i++) {
            newStatesArray[i] = newStates.get(i);
        }
        getDrawable().setState(newStatesArray);
        return false;
    }

    @Override
    public Drawable getMarker(final int stateBitset) {

        return getDrawable();
    }
}
