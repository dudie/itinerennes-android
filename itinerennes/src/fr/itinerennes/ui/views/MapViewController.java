package fr.itinerennes.ui.views;

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
