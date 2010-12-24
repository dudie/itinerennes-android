package fr.itinerennes.ui.views;

import org.andnav.osm.ResourceProxy;
import org.andnav.osm.tileprovider.CloudmadeException;
import org.andnav.osm.tileprovider.IOpenStreetMapTileProviderCallback;
import org.andnav.osm.tileprovider.IOpenStreetMapTileProviderCloudmadeTokenCallback;
import org.andnav.osm.tileprovider.OpenStreetMapTile;
import org.andnav.osm.views.util.OpenStreetMapRendererBase;

import fr.itinerennes.ItineRennesConstants;

public class MapQuestRenderer extends OpenStreetMapRendererBase {

    public MapQuestRenderer() {

        super("MapQuest", ItineRennesConstants.CONFIG_MINIMUM_ZOOM_MAP,
                ItineRennesConstants.CONFIG_MAXIMUM_ZOOM_MAP, 8, ".png",
                "http://otile1.mqcdn.com/tiles/1.0.0/osm/");
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.util.IOpenStreetMapRendererInfo#localizedName(org.andnav.osm.ResourceProxy)
     */
    @Override
    public String localizedName(final ResourceProxy proxy) {

        return name();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.util.IOpenStreetMapRendererInfo#getTileURLString(org.andnav.osm.tileprovider.OpenStreetMapTile,
     *      org.andnav.osm.tileprovider.IOpenStreetMapTileProviderCallback,
     *      org.andnav.osm.tileprovider.IOpenStreetMapTileProviderCloudmadeTokenCallback)
     */
    @Override
    public String getTileURLString(final OpenStreetMapTile aTile,
            final IOpenStreetMapTileProviderCallback aCallback,
            final IOpenStreetMapTileProviderCloudmadeTokenCallback aCloudmadeTokenCallback)
            throws CloudmadeException {

        return getBaseUrl() + aTile.getZoomLevel() + "/" + aTile.getX() + "/" + aTile.getY()
                + mImageFilenameEnding;
    }
}
