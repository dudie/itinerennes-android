package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.views.MapView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Canvas;

/**
 * @author Jérémie Huchet
 */
public class MarkerOverlay extends LazyOverlay {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarkerOverlay.class);

    private final List<String> visibleMarkerTypes = new ArrayList<String>(3);

    private List<Marker> markers;

    public MarkerOverlay(final Context ctx) {

        super(ctx);
        // TJHU Auto-generated constructor stub
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#onDraw(android.graphics.Canvas,
     *      org.osmdroid.views.MapView)
     */
    @Override
    protected void onDraw(final Canvas c, final MapView osmv) {

        // TJHU Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osmdroid.views.overlay.Overlay#onDrawFinished(android.graphics.Canvas,
     *      org.osmdroid.views.MapView)
     */
    @Override
    protected void onDrawFinished(final Canvas c, final MapView osmv) {

        // TJHU Auto-generated method stub

    }

    @Override
    public void onZoom() {

        // TJHU Auto-generated method stub

    }

    @Override
    public void onScroll() {

        // TJHU Auto-generated method stub

    }
}
