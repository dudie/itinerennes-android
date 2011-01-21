package fr.itinerennes.ui.views.overlays;

import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.Projection;
import org.osmdroid.views.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import fr.itinerennes.business.cache.GeoCacheProvider;

/**
 * An overlay to display various informations on the map.
 * 
 * @author Jérémie Huchet
 */
public class DebugOverlay extends Overlay {

    /** The paint. */
    private final Paint paint;

    /**
     * Default constructor.
     * 
     * @param ctx
     *            the context
     */
    public DebugOverlay(final Context ctx) {

        super(ctx);
        paint = new Paint();
        // paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        paint.setTextSize(10.0f);
        paint.setARGB(255, 255, 0, 0);
    }

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(DebugOverlay.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onDraw(android.graphics.Canvas,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    protected void onDraw(final Canvas c, final MapView osmv) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onDraw.start");
        }

        final Projection pj = osmv.getProjection();

        final Point start = new Point(), end = new Point();

        final int north = osmv.getMapCenterLatitudeE6() + osmv.getLatitudeSpan() / 2;
        final int south = osmv.getMapCenterLatitudeE6() - osmv.getLatitudeSpan() / 2;
        final int east = osmv.getMapCenterLongitudeE6() + osmv.getLongitudeSpan() / 2;
        final int west = osmv.getMapCenterLongitudeE6() - osmv.getLongitudeSpan() / 2;

        final BoundingBoxE6 screenBbox = GeoCacheProvider.normalize(new BoundingBoxE6(north, east,
                south, west));

        // horizontal lines
        for (int i = screenBbox.getLatSouthE6(); i < screenBbox.getLatNorthE6(); i = i + 1000) {
            final GeoPoint geoStart = new GeoPoint(i, screenBbox.getLonWestE6());
            final GeoPoint geoEnd = new GeoPoint(i, screenBbox.getLonEastE6());
            pj.toMapPixels(geoStart, start);
            pj.toMapPixels(geoEnd, end);
            c.drawLine(start.x, start.y, end.x, end.y, paint);
        }

        // vertical lines
        for (int i = screenBbox.getLonWestE6(); i < screenBbox.getLonEastE6(); i = i + 1000) {
            final GeoPoint geoStart = new GeoPoint(screenBbox.getLatNorthE6(), i);
            final GeoPoint geoEnd = new GeoPoint(screenBbox.getLatSouthE6(), i);
            pj.toMapPixels(geoStart, start);
            pj.toMapPixels(geoEnd, end);
            c.drawLine(start.x, start.y, end.x, end.y, paint);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onDraw.end");
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.andnav.osm.views.overlay.OpenStreetMapViewOverlay#onDrawFinished(android.graphics.Canvas,
     *      org.andnav.osm.views.OpenStreetMapView)
     */
    @Override
    protected void onDrawFinished(final Canvas arg0, final MapView arg1) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onDrawFinished.start/end");
        }
    }
}
