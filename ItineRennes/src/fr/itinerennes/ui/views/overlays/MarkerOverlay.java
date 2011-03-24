package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.views.MapView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.view.MotionEvent;

/**
 * @author Jérémie Huchet
 */
public class MarkerOverlay extends LazyOverlay {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerOverlay.class);

    /** The list containing visible types of markers. */
    private final List<String> visibleMarkerTypes = new ArrayList<String>(3);

    /** The async task in charge of updating the map box informations. */
    private AsyncTask<?, ?, ?> mapBoxDisplayer;

    /** The list of all displayed markers. */
    private List<MarkerOverlayItem> markers;

    /**
     * Creates the marker overlay.
     * 
     * @param context
     *            the context
     */
    public MarkerOverlay(final Context context) {

        super(context);
        // TJHU Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.LazyOverlay#onMapMove(org.osmdroid.views.MapView)
     */
    @Override
    protected void onMapMove(final MapView source) {

        // TOBO rafraichir la liste de marqueurs
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#onSingleTapUp(android.view.MotionEvent,
     *      org.osmdroid.views.MapView)
     */
    @Override
    public final boolean onSingleTapUp(final MotionEvent e, final MapView mapView) {

        // TOBO voir si un marqueur a été touché
        // si touché, déléguer vers onSingleMarkerTapUp() et retourner true
        return false;
    }

    public final void onSingleTapUpMarker(final MarkerOverlayItem marker, final MapView mapView) {

        // TOBO marquer le marqueur comme sélectionné + lancer la tâche d'affichage des infos dans
        // la mapbox
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#draw(android.graphics.Canvas,
     *      org.osmdroid.views.MapView, boolean)
     */
    @Override
    protected void draw(final Canvas c, final MapView osmv, final boolean shadow) {

        // TJHU Auto-generated method stub

    }
}
