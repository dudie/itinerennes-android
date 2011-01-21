package fr.itinerennes.ui.views.overlays;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

/**
 * Base class for {@link ItemizedOverlay} items.
 * 
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class ITROverlayItem<D> extends OverlayItem {

    /** The bundled data with this item. */
    private D data;

    /**
     * Creates an item for an {@link ITROverlayItem}.
     * 
     * @param id
     *            an identifier for the item
     * @param location
     *            the location of the item
     */
    public ITROverlayItem(final String id, final GeoPoint location) {

        super(id, null, location);
    }

    /**
     * Gets the identifier of the item.
     * 
     * @return the identifier of the item
     */
    public final String getId() {

        return mTitle;

    }

    /**
     * Gets the bundled data with this item.
     * 
     * @return the bundled data with this item
     */
    public final D getData() {

        return data;
    }

    /**
     * Sets the bundled data of this item.
     * 
     * @param data
     *            the bundled data to set to this item
     */
    public final void setData(final D data) {

        this.data = data;
    }

    /**
     * @deprecated Shouldn't be used as the constructor does not initialize this field.
     * @return null
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem#getTitle()
     */
    @Override
    @Deprecated
    public final String getTitle() {

        return super.getTitle();
    }

    /**
     * @deprecated Shouldn't be used as the constructor does not initialize this field.
     * @return null
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem#getKey()
     */
    @Override
    @Deprecated
    public final long getKey() {

        return super.getKey();
    }

    /**
     * @deprecated Shouldn't be used as the constructor does not initialize this field.
     * @return null
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlayItem#getSnippet()
     */
    @Override
    @Deprecated
    public final String getSnippet() {

        return super.getSnippet();
    }
}
