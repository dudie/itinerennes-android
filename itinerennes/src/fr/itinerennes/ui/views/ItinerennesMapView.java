package fr.itinerennes.ui.views;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.util.List;

import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;

import android.content.Context;
import android.util.AttributeSet;

import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.overlays.LazyOverlay;
import fr.itinerennes.ui.views.overlays.LocationOverlay;
import fr.itinerennes.ui.views.overlays.ParkOverlay;
import fr.itinerennes.ui.views.overlays.StopOverlay;

/**
 * The map view.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class ItinerennesMapView extends MapView {

    /** Map listener event delayed listener constant value. */
    private static final long MAP_LISTENER_DELAY = 200;

    /** The map controller. */
    private final MapViewController controller;

    /** The android context. */
    private final ItineRennesActivity context;

    /** The list of map listeners. */
    private final MapListenerWrapper mapListeners;

    /** The overlay containing stop markers. */
    private final StopOverlay stopOverlay;

    /** The overlay containing park and ride markers. */
    private final ParkOverlay parkOverlay;

    /** The overlay displaying the current location. */
    private final LocationOverlay myLocationOverlay;

    /** The next index to use to add the next overlay. */
    private final int nextOverlayIndex;

    /** The map box controller. */
    private final MapBoxController mapBoxController;

    static {
        final ITileSource tileSource = new XYTileSource("Itinerennes", null, 0,
                19, 256, ".png", new String[] {
                        "http://otile1.mqcdn.com/tiles/1.0.0/map/",
                        "http://otile2.mqcdn.com/tiles/1.0.0/map/",
                        "http://otile3.mqcdn.com/tiles/1.0.0/map/",
                        "http://otile4.mqcdn.com/tiles/1.0.0/map/" });
        TileSourceFactory.addTileSource(tileSource);

    }

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     */
    public ItinerennesMapView(final ItineRennesActivity context) {

        this(context, null);
    }

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     * @param attrs
     *            the set of attributes
     */
    public ItinerennesMapView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
        this.context = (ItineRennesActivity) context;
        this.controller = new MapViewController(this.context, this);

        this.mapBoxController = new MapBoxController(this);

        mapListeners = new MapListenerWrapper(3);
        super.setMapListener(new DelayedMapListener(mapListeners, MAP_LISTENER_DELAY));

        stopOverlay = new StopOverlay(this.context, this);
        myLocationOverlay = new LocationOverlay(this.context, this);

        parkOverlay = new ParkOverlay(this.context, this);
        this.addOverlay(parkOverlay);
        this.addOverlay(stopOverlay);
        super.getOverlays().add(myLocationOverlay);

        // next index to add an overlay is just before the index of the location overlay
        nextOverlayIndex = super.getOverlays().size() - 1;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.MapView#getController()
     */
    @Override
    public final MapViewController getController() {

        return controller;
    }

    /**
     * Adds a listener to the map.
     * 
     * @param listener
     *            the listener to add
     */
    public final void addListener(final MapListener listener) {

        mapListeners.add(listener);
    }

    /**
     * Removes a listener from the map.
     * 
     * @param listener
     *            the listener to remove
     */
    public final void removeListener(final MapListener listener) {

        mapListeners.remove(listener);
    }

    /**
     * Gets the Stop Overlay.
     * 
     * @return the stop Overlay
     */
    public final StopOverlay getStopOverlay() {

        return stopOverlay;
    }

    /**
     * Gets the Park Overlay.
     * 
     * @return the park and ride Overlay
     */
    public final ParkOverlay getParkOverlay() {

        return parkOverlay;
    }

    /**
     * Gets the myLocationOverlay.
     * 
     * @return the myLocationOverlay
     */
    public final LocationOverlay getMyLocationOverlay() {

        return myLocationOverlay;
    }

    /**
     * Adds an overlay to the map.
     * 
     * @param overlay
     *            the overlay to add
     */
    public final void addOverlay(final LazyOverlay overlay) {

        this.addListener(overlay);
        super.getOverlays().add(nextOverlayIndex, overlay);
    }

    /**
     * Removes an overlay from the map.
     * 
     * @param overlay
     *            the overlay to remove
     */
    public final void removeOverlay(final LazyOverlay overlay) {

        this.removeListener(overlay);
        super.getOverlays().remove(overlay);
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated use {@link #addListener(MapListener)} instead
     * @see org.osmdroid.views.MapView#setMapListener(org.osmdroid.events.MapListener)
     */
    @Deprecated
    @Override
    public final void setMapListener(final MapListener listener) {

    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated use {@link #addOverlay(LazyOverlay)} and {@link #removeOverlay(LazyOverlay)}
     *             instead
     * @see org.osmdroid.views.MapView#getOverlays()
     */
    @Deprecated
    @Override
    public final List<Overlay> getOverlays() {

        return null;
    }

    /**
     * Gets the mapBoxController.
     * 
     * @return the mapBoxController
     */
    public final MapBoxController getMapBoxController() {

        return mapBoxController;
    }

}
