package fr.itinerennes.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.andnav.osm.events.MapListener;
import org.andnav.osm.events.ScrollEvent;
import org.andnav.osm.events.ZoomEvent;
import org.andnav.osm.util.BoundingBoxE6;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener;
import org.andnav.osm.views.util.IOpenStreetMapRendererInfo;
import org.andnav.osm.views.util.OpenStreetMapTileProvider;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.facade.StationProvider;
import fr.itinerennes.model.Station;
import fr.itinerennes.ui.tasks.BuildOverlayTask;
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
    private final MapViewController controller;

    /** The android context. */
    private final Context context;

    /** OnItemGestureListener. */
    private OnItemGestureListener<StationOverlayItem> onItemGestureListener;

    private final HashMap<Integer, BuildOverlayTask> tasks = new HashMap<Integer, BuildOverlayTask>();

    /** Array of stations providers. */
    private StationProvider[] stationProviders;

    private StationOverlay overlay;

    public StationOverlay getOverlay() {

        return overlay;
    }

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
        this.controller = new MapViewController(this);
    }

    /**
     * @param context
     * @param aRendererInfo
     */
    public MapView(final Context context, final IOpenStreetMapRendererInfo aRendererInfo) {

        super(context, aRendererInfo);
        this.context = context;
        this.controller = new MapViewController(this);
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
        this.controller = new MapViewController(this);
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
        this.controller = new MapViewController(this);
    }

    /**
     * Called when the user scrolls the map. Deactivate the location following.
     * 
     * @return true
     * @see org.andnav.osm.events.MapListener#onScroll(org.andnav.osm.events.ScrollEvent)
     */
    @Override
    public final boolean onScroll(final ScrollEvent event) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onScroll");
        }

        if (overlay != null) {
            overlay.unFocusItem(this);
        }

        if (this.isShown()) {
            executeBuildOverlayTask(Station.TYPE_BUS);
            executeBuildOverlayTask(Station.TYPE_BIKE);
            executeBuildOverlayTask(Station.TYPE_SUBWAY);
        }
        return true;
    }

    /**
     * Called when the user zoom in or out the map.
     * 
     * @return true
     * @see org.andnav.osm.events.MapListener#onZoom(org.andnav.osm.events.ZoomEvent)
     */
    @Override
    public final boolean onZoom(final ZoomEvent event) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onZoom");
        }

        if (this.isShown()) {
            if (event.getZoomLevel() < ItineRennesConstants.CONFIG_MINIMUM_ZOOM_ITEMS) {
                removeStationOverlay();
            } else {
                executeBuildOverlayTask(Station.TYPE_BUS);
                executeBuildOverlayTask(Station.TYPE_BIKE);
                executeBuildOverlayTask(Station.TYPE_SUBWAY);
            }
        }
        return true;
    }

    /**
     * Sets the stations providers available.
     * 
     * @param stationProviders
     *            the station providers
     */
    public final void setStationProviders(final StationProvider[] stationProviders) {

        this.stationProviders = stationProviders;
    }

    /**
     * Sets the OnItemGestureListener to use with overlays.
     * 
     * @param onItemGestureListener
     *            the listener to use with overlays.
     */
    public final void setOnItemGestureListener(
            final OnItemGestureListener<StationOverlayItem> onItemGestureListener) {

        this.onItemGestureListener = onItemGestureListener;

    }

    /**
     * Gets the OnItemGestureListener to use with overlays.
     * 
     * @return the OnItemGestureListener
     */
    public final OnItemGestureListener<StationOverlayItem> getOnItemGestureListener() {

        return this.onItemGestureListener;

    }

    /**
     * Verify if the task for the passed overlay type exists and executes a new one. If a previous
     * task is not finished, it is canceled.
     * 
     * @param type
     */
    private void executeBuildOverlayTask(final int type) {

        if (this.overlay == null) {
            this.overlay = new StationOverlay<StationOverlayItem>(context,
                    new ArrayList<StationOverlayItem>(), getOnItemGestureListener(), type);
            this.getOverlays().add(overlay);
        }
        BuildOverlayTask task = tasks.get(type);
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("cancelling previous BuildOverlayTask");
            }
        }
        final BoundingBoxE6 bbox = this.getVisibleBoundingBoxE6();
        task = new BuildOverlayTask(this.context, this, stationProviders[type], type, this.overlay);
        tasks.put(type, task);
        tasks.get(type).execute(bbox);
    }

    /**
     * Removes the station overlay from the map view.
     */
    private synchronized void removeStationOverlay() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("removeStationOverlay.start");
        }

        this.getOverlays().remove(overlay);
        overlay.unFocusItem(this);
        overlay = null;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("removeStationOverlay.end");
        }
    }

    /**
     * Cancels all BuildOverlayTask that are running.
     */
    public final synchronized void cancelTasks() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("cancelTasks.start");
        }

        for (final Entry<Integer, BuildOverlayTask> task : tasks.entrySet()) {
            task.getValue().cancel(true);
        }
        tasks.clear();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("cancelTasks.end");
        }
    }
}
