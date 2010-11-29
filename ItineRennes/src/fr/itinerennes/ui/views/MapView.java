package fr.itinerennes.ui.views;

import org.andnav.osm.events.MapListener;
import org.andnav.osm.events.ScrollEvent;
import org.andnav.osm.events.ZoomEvent;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.util.IOpenStreetMapRendererInfo;
import org.andnav.osm.views.util.OpenStreetMapTileProvider;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.util.AttributeSet;

/**
 * The map view.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class MapView extends OpenStreetMapView implements MapListener {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(MapView.class);

    /** The map controller. */
    private MapViewController controller;

    /** True if the focused item layout is visible */
    boolean focused;

    /**
     * @param context
     */
    public MapView(final Context context) {

        super(context);
        this.controller = new MapViewController(this);
    }

    /**
     * @param context
     * @param attrs
     */
    public MapView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
        // TJHU Auto-generated constructor stub
    }

    /**
     * @param context
     * @param aRendererInfo
     */
    public MapView(final Context context, final IOpenStreetMapRendererInfo aRendererInfo) {

        super(context, aRendererInfo);
        // TJHU Auto-generated constructor stub
    }

    /**
     * @param context
     * @param aRendererInfo
     * @param aTileProvider
     */
    public MapView(final Context context, final IOpenStreetMapRendererInfo aRendererInfo,
            final OpenStreetMapTileProvider aTileProvider) {

        super(context, aRendererInfo, aTileProvider);
        // TJHU Auto-generated constructor stub
    }

    /**
     * @param context
     * @param aRendererInfo
     * @param aMapToShareTheTileProviderWith
     */
    public MapView(final Context context, final IOpenStreetMapRendererInfo aRendererInfo,
            final OpenStreetMapView aMapToShareTheTileProviderWith) {

        super(context, aRendererInfo, aMapToShareTheTileProviderWith);
        // TJHU Auto-generated constructor stub
    }

    /**
     * Called when the user scrolls the map. Deactivate the location following.
     * 
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public boolean onScroll(final ScrollEvent event) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("MapView.onScroll");
        }

        return false;
    }

    /**
     * Called when the user zoom in or out the map.
     * 
     * @see org.andnav.osm.events.MapListener#onZoom(org.andnav.osm.events.ZoomEvent)
     */
    @Override
    public boolean onZoom(final ZoomEvent event) {

        return false;
    }

    /**
     * Sets the focused flag which indicate if the item layout is visible
     * 
     * @param focused
     *            focused or not
     */
    public void setItemLayoutFocused(boolean focused) {

        this.focused = focused;
    }

    /**
     * Gets the focused flag which indicate if the item layout is visible
     * 
     * @param focused
     *            focused or not
     */
    public boolean isItemLayoutFocused() {

        return this.focused;
    }
}
