package fr.itinerennes.ui.views;

import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.views.MapView;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.content.Context;
import android.util.AttributeSet;

import fr.itinerennes.ui.activity.ITRContext;

/**
 * The map view.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class ITRMapView extends MapView {

    /** Map listener event delayed listener constant value. */
    private static final long MAP_LISTENER_DELAY = 800;

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(ITRMapView.class);

    /** The map controller. */
    private final MapViewController controller;

    /** The android context. */
    private final ITRContext context;

    /** The list of map listeners. */
    private final MapListenerWrapper mapListeners;

    /**
     * @param context
     */
    public ITRMapView(final ITRContext context) {

        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ITRMapView(final Context context, final AttributeSet attrs) {

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
