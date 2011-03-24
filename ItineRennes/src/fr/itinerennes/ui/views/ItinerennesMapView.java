package fr.itinerennes.ui.views;

import java.util.List;

import org.osmdroid.events.DelayedMapListener;
import org.osmdroid.events.MapListener;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.util.AttributeSet;
import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ItinerennesContext;
import fr.itinerennes.ui.activity.MapActivity;
import fr.itinerennes.ui.views.overlays.EventGarbageOverlay;
import fr.itinerennes.ui.views.overlays.LazyOverlay;
import fr.itinerennes.ui.views.overlays.LocationOverlay;
import fr.itinerennes.ui.views.overlays.MarkerOverlay;

/**
 * The map view.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class ItinerennesMapView extends MapView {

    /** Map listener event delayed listener constant value. */
    private static final long MAP_LISTENER_DELAY = 800;

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(ItinerennesMapView.class);

    /** The map controller. */
    private final MapViewController controller;

    /** The android context. */
    private final ItinerennesContext context;

    /** The list of map listeners. */
    private final MapListenerWrapper mapListeners;

    /** The overlay containing all the markers. */
    private final MarkerOverlay markerOverlay;

    /** The overlay collecting unhandled events. */
    private final EventGarbageOverlay garbageOverlay;

    /** The overlay displaying the current location. */
    private final LocationOverlay myLocationOverlay;

    /** The next index to use to add the next overlay. */
    private final int nextOverlayIndex;

    /**
     * @param context
     */
    public ItinerennesMapView(final MapActivity context) {

        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ItinerennesMapView(final MapActivity context, final AttributeSet attrs) {

        super(context, attrs);
        this.context = context;
        this.controller = new MapViewController(this.context, this);

        mapListeners = new MapListenerWrapper(3);
        super.setMapListener(new DelayedMapListener(mapListeners, MAP_LISTENER_DELAY));

        garbageOverlay = new EventGarbageOverlay(context);
        markerOverlay = new MarkerOverlay(context);
        myLocationOverlay = new LocationOverlay(context, this,
                (ToggleButton) context.findViewById(R.id.mylocation_button));

        super.getOverlays().add(garbageOverlay);
        this.addOverlay(markerOverlay);
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
     * Gets the markerOrverlay.
     * 
     * @return the markerOrverlay
     */
    public final MarkerOverlay getMarkerOverlay() {

        return markerOverlay;
    }

    /**
     * Gets the garbageOverlay.
     * 
     * @return the garbageOverlay
     */
    public final EventGarbageOverlay getGarbageOverlay() {

        return garbageOverlay;
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

        this.addListener(markerOverlay);
        super.getOverlays().add(nextOverlayIndex, overlay);
    }

    /**
     * Removes an overlay from the map.
     * 
     * @param overlay
     *            the overlay to remove
     */
    public final void removeOverlay(final LazyOverlay overlay) {

        this.removeListener(markerOverlay);
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
}
