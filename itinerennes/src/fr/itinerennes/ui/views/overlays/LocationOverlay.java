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

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.ToggleButton;
import fr.itinerennes.Conf;
import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.ItinerennesMapView;

/**
 * Overlay displaying the current location.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */
public final class LocationOverlay extends MyLocationNewOverlay {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocationOverlay.class);

    /** The context. */
    private final ItineRennesActivity context;

    /** The map on which this location overlay is attached. */
    private final ItinerennesMapView map;

    /** Delay for desactivate follow location on map move event. */
    private static final long MAP_MOVE_DELAY = 500;

    /**
     * Constructor.
     * 
     * @param ctx
     *            the context
     * @param mapView
     *            the map view containing this overlay
     */
    public LocationOverlay(final Context ctx, final MapView mapView) {

        super(ctx, mapView);
        this.context = (ItineRennesActivity) ctx;
        this.map = (ItinerennesMapView) mapView;
    }

    /**
     * Toggle FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    public void toggleFollowLocation() {

        if (isFollowLocationEnabled()) {
            disableFollowLocation();
        } else {
            enableFollowLocation();
        }
    }

    /**
     * Disable FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    @Override
    public void disableFollowLocation() {

        super.disableFollowLocation();
        disableMyLocation();
//        ((ToggleButton) this.context.findViewById(R.id.mylocation_button)).setChecked(false);
    }

    /**
     * Enable FollowLocation and EnableMyLocation flags for the MyLocationOverlay.
     */
    @Override
    public void enableFollowLocation() {

        enableMyLocation();
        super.enableFollowLocation();
        map.getController().setZoom(Conf.MAP_DEFAULT_ZOOM);

    }

    @Override
    public boolean onTouchEvent(final MotionEvent event, final MapView mapView) {

        if (event.getAction() == MotionEvent.ACTION_MOVE
                && (SystemClock.uptimeMillis() - event.getDownTime() >= MAP_MOVE_DELAY)) {

            if (isFollowLocationEnabled()) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Disabling follow location because of a touch event on the map");
                }
                disableFollowLocation();
            }
        }
        return false;
    }

}
