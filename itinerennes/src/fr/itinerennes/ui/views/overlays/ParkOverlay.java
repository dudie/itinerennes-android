package fr.itinerennes.ui.views.overlays;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import fr.dudie.keolis.model.RelayPark;
import fr.dudie.keolis.model.RelayParkState;
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.ItinerennesMapView;

/**
 * This overlay displays an icon for each car park.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class ParkOverlay extends LazyOverlay implements ILayerSelector {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkOverlay.class);

    /** Data refresh interval in milliseconds. */
    private static final long REFRESH_INTERVAL = 900 * 1000;

    /** The MapActivity context. */
    private final ItineRennesActivity context;

    /** The map view containing this overlay. */
    private final ItinerennesMapView map;

    /** The list of all parkings. */
    private final List<ParkOverlayItem> parks = new ArrayList<ParkOverlayItem>(4);

    /** Last overlay update time. */
    private long lastUpdate;

    /** This property references the task currently loading parks from keolis. */
    private AsyncTask<Void, Void, List<ParkOverlayItem>> refreshTask = null;

    /** Drawable used to show a park on the map. */
    private final Drawable icon;

    /** Flag to indicate if the park and ride markers are visible. */
    private boolean visible = true;

    /** From which availability rate we have to display an orange gauge. */
    private static final double ORANGE_LEVEL = 0.15;

    /**
     * Creates the marker overlay.
     * 
     * @param context
     *            the context
     * @param map
     *            the {@link MapView}
     */
    public ParkOverlay(final ItineRennesActivity context, final ItinerennesMapView map) {

        super(context);
        this.context = context;
        this.map = map;
        this.icon = context.getResources().getDrawable(R.drawable.icx_marker_park);

        final int leftRight = icon.getIntrinsicWidth() / 2;
        final int topBottom = icon.getIntrinsicHeight() / 2;
        final Rect bounds = new Rect(-leftRight, -topBottom, leftRight, topBottom);
        icon.setBounds(bounds);

    }

    /***
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.LazyOverlay#onMapMove(org.osmdroid.views.MapView)
     */
    @Override
    public final void onMapMove(final MapView source) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onMapMove");
        }

        refreshTask = new UpdateParksTask();
        refreshTask.execute();

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#draw(android.graphics.Canvas,
     *      org.osmdroid.views.MapView, boolean)
     */
    @Override
    protected final void draw(final Canvas c, final MapView osmv, final boolean shadow) {

        if (visible) {
            // on dessine les items seulement s'il ne s'agit pas du mode shadow
            if (!shadow && parks.size() > 0) {

                // on applique le niveau du drawable en fonction du niveau de zoom de la carte
                icon.setLevel(map.getZoomLevel());

                final Projection pj = osmv.getProjection();
                final Point point = new Point();

                for (final ParkOverlayItem park : parks) {
                    pj.toPixels(park.getLocation(), point);

                    drawItem(c, park, point, osmv);
                }

            }
        }
    }

    /**
     * Draws an item located at the provided screen coordinates to the canvas.
     * 
     * @param canvas
     *            what the item is drawn upon
     * @param item
     *            the item to be drawn
     * @param curScreenCoords
     *            the screen coordinates of the item
     * @param mapView
     *            the map view
     */
    protected final void drawItem(final Canvas canvas, final ParkOverlayItem item,
            final Point curScreenCoords, final MapView mapView) {

        int[] originalState = null;

        final int[] states = new int[2];
        int index = 0;

        if (item.getAvailable() == 0 || item.getState().equals(RelayParkState.CLOSED)) {
            states[index++] = R.attr.state_park_red;
        } else if (item.getAvailable() < item.getCapacity() * ORANGE_LEVEL) {
            states[index++] = R.attr.state_park_orange;
        }

        if (index > 0) {
            originalState = icon.getState();
            icon.setState(states);

        }

        // draw it
        Overlay.drawAt(canvas, icon, curScreenCoords.x, curScreenCoords.y, false, 0f);

        if (originalState != null) {
            // restore original state
            icon.setState(originalState);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#onSingleTapConfirmed(MotionEvent, MapView)
     */
    @Override
    public final boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSingleTapConfirmed.start - event={}", e);
        }

        final boolean eventHandled;

        final ParkOverlayItem park = checkItemPresence(e, mapView);
        if (park != null) {
            onSingleTapUpMarker(park, mapView);
            mapView.postInvalidate();

            eventHandled = true;
        } else {
            ((ItinerennesMapView) mapView).getMapBoxController().hide();
            mapView.postInvalidate();

            eventHandled = false;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onSingleTapConfirmed.end - eventHandled={}", eventHandled);
        }

        return eventHandled;
    }

    /**
     * Called when an item is single tapped up on the overlay.
     * 
     * @param park
     *            the park tapped up
     * @param mapView
     *            the map view containing the overlay
     */
    private final void onSingleTapUpMarker(final ParkOverlayItem park, final MapView mapView) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("onSingleTapUpMarker.start - %s", park));
        }

        ((ItinerennesMapView) mapView).getMapBoxController().show(park);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("onSingleTapUpMarker.end - %s", park));
        }
    }

    /**
     * Check if an touched point of the screen is over an item of this overlay.
     * 
     * @param event
     *            event triggered
     * @param mapView
     *            map view containing the overlay
     * @return if found, the RelayPark under the touch point
     */
    private ParkOverlayItem checkItemPresence(final MotionEvent event, final MapView mapView) {

        final Projection pj = mapView.getProjection();
        final int eventX = (int) event.getX();
        final int eventY = (int) event.getY();

        final Point itemPoint = new Point();
        for (final ParkOverlayItem park : parks) {

            pj.toPixels(park.getLocation(), itemPoint);

            if (icon.getBounds().contains(eventX - itemPoint.x, eventY - itemPoint.y)) {
                return park;
            }

        }
        return null;
    }

    /**
     * AsyncTask to fetch car park informations from Keolis and redraw the park overlay.
     * 
     * @author Olivier Boudet
     */
    private class UpdateParksTask extends AsyncTask<Void, Void, List<ParkOverlayItem>> {

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected List<ParkOverlayItem> doInBackground(final Void... params) {

            final List<ParkOverlayItem> newParks = new ArrayList<ParkOverlayItem>(4);

            if (System.currentTimeMillis() - lastUpdate > (REFRESH_INTERVAL)) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("ParkOverlay : refreshing data from keolis");
                }

                try {
                    for (final RelayPark park : context.getApplicationContext().getKeolisClient()
                            .getAllRelayParks()) {
                        final ParkOverlayItem item = new ParkOverlayItem();
                        item.setType(TypeConstants.TYPE_CAR_PARK);
                        item.setLabel(park.getName());
                        item.setLocation(new GeoPoint(park.getLatitude(), park.getLongitude()));
                        item.setAvailable(park.getCarParkAvailable());
                        item.setCapacity(park.getCarParkCapacity());
                        item.setState(park.getState());
                        newParks.add(item);
                    }

                } catch (final IOException e) {
                    LOGGER.warn("Update of relay parks failed.");
                }
            }

            return newParks;

        }

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final List<ParkOverlayItem> newParks) {

            if (newParks != null && newParks.size() > 0 && refreshTask == this) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("ParkOverlay : refreshing map");
                }

                lastUpdate = System.currentTimeMillis();
                parks.clear();
                parks.addAll(newParks);
            }

            map.postInvalidate();

        }
    }

    /**
     * Show markers of a given type.
     * 
     * @param type
     *            type to show
     */
    @Override
    public final void show(final String type) {

        visible = true;
        onMapMove(map);
    }

    /**
     * Hide markers of a given type.
     * 
     * @param type
     *            type to hide
     */
    @Override
    public final void hide(final String type) {

        visible = false;
        onMapMove(map);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.ILayerSelector#getLayersDescriptors()
     */
    @Override
    public final List<LayerDescriptor> getLayersDescriptors() {

        final List<LayerDescriptor> labels = new ArrayList<LayerDescriptor>();

        labels.add(new LayerDescriptor(this, context.getString(R.string.overlay_park),
                TypeConstants.TYPE_CAR_PARK, visible));

        return labels;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.ILayerSelector#isVisible(java.lang.String)
     */
    @Override
    public final boolean isVisible(final String type) {

        return visible;
    }
}
