package fr.itinerennes.ui.views;

import java.util.HashMap;

import org.andnav.osm.events.MapListener;
import org.andnav.osm.events.ScrollEvent;
import org.andnav.osm.events.ZoomEvent;
import org.andnav.osm.util.BoundingBoxE6;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.overlay.OpenStreetMapViewItemizedOverlay.OnItemGestureListener;
import org.andnav.osm.views.overlay.OpenStreetMapViewOverlay;
import org.andnav.osm.views.util.IOpenStreetMapRendererInfo;
import org.andnav.osm.views.util.OpenStreetMapTileProvider;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import fr.itinerennes.beans.Station;
import fr.itinerennes.business.facade.BikeService;
import fr.itinerennes.business.facade.BusService;
import fr.itinerennes.business.facade.StationProvider;
import fr.itinerennes.database.DatabaseHelper;
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

    /** Boolean indicating if the focused item layout is visible. */
    boolean focused;

    private final HashMap<Integer, BuildOverlayTask> tasks = new HashMap<Integer, BuildOverlayTask>();

    /** The station providers. */
    private final StationProvider[] stationProviders;

    /**
     * @param context
     */
    public MapView(final Context context) {

        super(context);
        this.context = context;
        this.controller = new MapViewController(this);
        this.stationProviders = new StationProvider[2];
        final DatabaseHelper dbHelper = new DatabaseHelper(context);
        stationProviders[0] = new BikeService(dbHelper.getWritableDatabase());
        stationProviders[1] = new BusService();
    }

    /**
     * @param context
     * @param attrs
     */
    public MapView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        this.controller = new MapViewController(this);
        this.stationProviders = new StationProvider[2];
        final DatabaseHelper dbHelper = new DatabaseHelper(context);
        stationProviders[0] = new BikeService(dbHelper.getWritableDatabase());
        stationProviders[1] = new BusService();
    }

    /**
     * @param context
     * @param aRendererInfo
     */
    public MapView(final Context context, final IOpenStreetMapRendererInfo aRendererInfo) {

        super(context, aRendererInfo);
        this.context = context;
        this.controller = new MapViewController(this);
        this.stationProviders = new StationProvider[2];
        final DatabaseHelper dbHelper = new DatabaseHelper(context);
        stationProviders[0] = new BikeService(dbHelper.getWritableDatabase());
        stationProviders[1] = new BusService();
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
        this.stationProviders = new StationProvider[2];
        final DatabaseHelper dbHelper = new DatabaseHelper(context);
        stationProviders[0] = new BikeService(dbHelper.getWritableDatabase());
        stationProviders[1] = new BusService();
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
        this.stationProviders = new StationProvider[2];
        final DatabaseHelper dbHelper = new DatabaseHelper(context);
        stationProviders[0] = new BikeService(dbHelper.getWritableDatabase());
        stationProviders[1] = new BusService();
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
            executeBuildOverlayTask(Station.TYPE_BUS);
            if (tasks.get(Station.TYPE_BIKE) == null) {
                executeBuildOverlayTask(Station.TYPE_BIKE);
            }
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
            executeBuildOverlayTask(Station.TYPE_BUS);
            if (tasks.get(Station.TYPE_BIKE) == null) {
                executeBuildOverlayTask(Station.TYPE_BIKE);
            }
        }
        return true;
    }

    /**
     * Sets the focused flag which indicate if the item layout is visible
     * 
     * @param focused
     *            focused or not
     */
    public void setItemLayoutFocused(final boolean focused) {

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
            final OnItemGestureListener<StationOverlayItem> onItemGestureListener) {

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
     * Verify if the task for the passed overlay type exists and executes a new one. If a previous
     * task is not finished, it is canceled.
     * 
     * @param type
     */
    private void executeBuildOverlayTask(final int type) {

        BuildOverlayTask task = tasks.get(type);
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
            LOGGER.debug("cancelling previous BuildOverlayTask");
        }
        final BoundingBoxE6 bbox = this.getVisibleBoundingBoxE6();
        task = new BuildOverlayTask(this.context, this, stationProviders[type], type);
        tasks.put(type, task);
        tasks.get(type).execute(bbox);
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
    public synchronized void refreshOverlay(
            final StationOverlay<StationOverlayItem> stationOverlay, final int type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("refreshOverlay");
        }

        for (final OpenStreetMapViewOverlay overlay : this.getOverlays()) {
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
