package fr.itinerennes.ui.views;

import org.andnav.osm.events.DelayedMapListener;
import org.andnav.osm.views.OpenStreetMapView;
import org.andnav.osm.views.util.OpenStreetMapRendererFactory;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.util.AttributeSet;

import fr.itinerennes.ui.activity.ITRContext;

/**
 * The map view.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class MapView extends OpenStreetMapView {

    /**
     * Register the map quest render on class loading.
     */
    static {
        OpenStreetMapRendererFactory.addRenderer(new MapQuestRenderer());
    }

    /** Map listener event delayed listener constant value. */
    private static final long MAP_LISTENER_DELAY = 800;

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(MapView.class);

    /** The map controller. */
    private final MapViewController controller;

    /** The android context. */
    private final ITRContext context;

    /** The list of map listeners. */
    private final MapListenerWrapper mapListeners;

    /**
     * @param context
     */
    public MapView(final ITRContext context) {

        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public MapView(final Context context, final AttributeSet attrs) {

        super(context, attrs);
        this.context = (ITRContext) context;
        this.controller = new MapViewController(this.context, this);

        mapListeners = new MapListenerWrapper(3);
        setMapListener(new DelayedMapListener(mapListeners, MAP_LISTENER_DELAY));

    }

    @Override
    public MapViewController getController() {

        return controller;
    }

    public MapListenerWrapper getListeners() {

        return mapListeners;
    }

}
