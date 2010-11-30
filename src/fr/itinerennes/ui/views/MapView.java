package fr.itinerennes.ui.views;

import org.andnav.osm.events.MapListener;
import org.andnav.osm.events.ScrollEvent;
import org.andnav.osm.events.ZoomEvent;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
import org.andnav.osm.views.util.IOpenStreetMapRendererInfo;
import org.andnav.osm.views.util.OpenStreetMapTileProvider;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.util.AttributeSet;
import fr.itinerennes.beans.BoundingBox;
import fr.itinerennes.ui.tasks.RefreshBusOverlayTask;
import fr.itinerennes.ui.views.overlays.StationOverlay;
import fr.itinerennes.ui.views.overlays.StationOverlayItem;

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

    /** The android context. */
    private Context context;

    /** OnItemGestureListener. */
    private OnItemGestureListener<StationOverlayItem> onItemGestureListener;

    /** Boolean indicating if the focused item layout is visible. */
    boolean focused;

    /**
     * @param context
     */
    public MapView(final Context context) {

        super(context);
        this.context = context;
        this.controller = new MapViewController(this);
    }

    /**
     * @param context
     * @param attrs
     */
    public MapView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        // TJHU Auto-generated constructor stub
    }

    /**
     * @param context
     * @param aRendererInfo
     */
    public MapView(final Context context, final IOpenStreetMapRendererInfo aRendererInfo) {

        super(context, aRendererInfo);
        this.context = context;
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
        this.context = context;
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
        this.context = context;
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
            LOGGER.debug("onScroll");
        }

        if (this.isShown()) {
            final BoundingBox bbox = new BoundingBox(this.getVisibleBoundingBoxE6());
            new RefreshBusOverlayTask(this.context, this).execute(bbox);
        }
        return true;
    }

    /**
     * Called when the user zoom in or out the map.
     * 
     * @see org.andnav.osm.events.MapListener#onZoom(org.andnav.osm.events.ZoomEvent)
     */
    @Override
    public boolean onZoom(final ZoomEvent event) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onZoom");
        }

        if (this.isShown()) {
            BoundingBox bbox = new BoundingBox(this.getVisibleBoundingBoxE6());
            new RefreshBusOverlayTask(this.context, this).execute(bbox);
        }
        return true;
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

    /**
     * Sets the OnItemGestureListener to use with overlays.
     * 
     * @param onItemGestureListener
     *            the listener to use with overlays.
     */
    public void setOnItemGestureListener(
            OnItemGestureListener<StationOverlayItem> onItemGestureListener) {

        this.onItemGestureListener = onItemGestureListener;

    }

    /**
     * Gets the OnItemGestureListener to use with overlays.
     * 
     * @return the OnItemGestureListener
     */
    public OnItemGestureListener<StationOverlayItem> getOnItemGestureListener() {

        return this.onItemGestureListener;

    }

    /**
     * Synchronized method to refresh an overlay on the map. If an overlay of the same type already
     * exists on the map, it will be replaced.
     * 
     * @param stationOverlay
     *            Overlay to add on the map
     * @param type
     *            The type of the overlay to replace.
     */
    public synchronized void refreshOverlay(StationOverlay<StationOverlayItem> stationOverlay,
            int type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("refreshOverlay");
        }

        for (OpenStreetMapViewOverlay overlay : this.getOverlays()) {
            if (overlay instanceof StationOverlay) {
                if (((StationOverlay<StationOverlayItem>) overlay).getType() == type) {
                    this.getOverlays().remove(overlay);
                }
            }
        }
        if (stationOverlay != null) {
            this.getOverlays().add(stationOverlay);
        }

        this.postInvalidate();
    }
}
