package fr.itinerennes.ui.views;

import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.util.IOpenStreetMapRendererInfo;
import org.andnav.osm.views.util.OpenStreetMapTileProvider;

import android.content.Context;
import android.util.AttributeSet;

/**
 * The map view.
 * 
 * @author Jérémie Huchet
 */
public class MapView extends OpenStreetMapView {

	/** The map controller. */
	private MapViewController controller;

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

}
