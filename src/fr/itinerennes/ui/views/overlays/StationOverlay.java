package fr.itinerennes.ui.views.overlays;

import java.util.List;

import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Contains a list of items displayed on the map.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class StationOverlay<T extends StationOverlayItem> extends
        OpenStreetMapViewItemizedOverlay<T> {

    /** The bitmap used to draw the station on the map. */
    private Bitmap icon;

    /**
     * @param ctx
     *            the context
     * @param items
     *            the items
     */
    public StationOverlay(final Context ctx, final List<T> items) {

        super(ctx, items, new OnItemGestureListener<T>());
    }

    /**
     * The
     * {@link org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener}
     * which displays the tooltip over the selected station.
     * 
     * @author Jérémie Huchet
     * @author Olivier Boudet
     * @param <T>
     *            the type of {@link StationOverlayItem} handled
     */
    public static class OnItemGestureListener<T> implements
            OpenStreetMapViewItemizedOverlay.OnItemGestureListener<T> {

        /**
         * Triggered when the user single tap on an item of this overlay.
         * 
         * @param index
         *            the index of the taped item
         * @param item
         *            the taped item
         */
        @Override
        public boolean onItemSingleTapUp(final int index, final T item) {

            // TJHU Auto-generated method stub
            return false;
        }

        /**
         * Triggered when the user do a long press on an item of this overlay.
         * 
         * @param index
         *            the index of the taped item
         * @param item
         *            the taped item
         */
        @Override
        public boolean onItemLongPress(final int index, final T item) {

            // TJHU Auto-generated method stub
            return false;
        }
    }
}
