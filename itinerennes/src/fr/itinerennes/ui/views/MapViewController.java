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

import org.osmdroid.tileprovider.tilesource.ITileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.itinerennes.ui.activity.ItineRennesActivity;

/**
 * A controller for {@link ITRMapView}. Provides convenience methods to manipulates the map
 * displayed to the user screen.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public final class MapViewController extends MapController {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MapViewController.class);

    /** The map view. */
    private final ItinerennesMapView map;

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     * @param map
     *            the map view to which associate the controller
     */
    public MapViewController(final ItineRennesActivity context, final ItinerennesMapView map) {

        super(map);
        this.map = map;

    }

    /**
     * Change the tile provider of the map. Does nothing if no {@link ITileSource} exists for the
     * given name.
     * 
     * @param tileSourceName
     *            the name of the tile source to set
     * @return true if the tile source have been changed or false if no tile provider matches the
     *         given name
     */
    public boolean setTileSource(final String tileSourceName) {

        ITileSource tileSrc = null;
        try {
            tileSrc = TileSourceFactory.getTileSource(tileSourceName);
            map.setTileSource(tileSrc);
            LOGGER.info("switching to '{}' tile source", tileSourceName);
            return true;
        } catch (final Exception e) {
            LOGGER.error("can't load tile source '{}'", e);
            return false;
        }
    }
}
