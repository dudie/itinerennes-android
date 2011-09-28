package fr.itinerennes.ui.views.overlays;

import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.PathOverlay;

import android.content.Context;

import fr.itinerennes.utils.MapUtils;

/**
 * Overlay displaying polylines.
 * 
 * @author Jérémie Huchet
 */
public class PolylineOverlay extends PathOverlay {

    /**
     * Constructor.
     * 
     * @param color
     *            color of the line
     * @param context
     *            the context
     */
    public PolylineOverlay(final int color, final Context context) {

        super(color, context);
    }

    /**
     * Sets the polyline to display.
     * 
     * @param encoded
     *            the polyline
     */
    public final void setPolyline(final String encoded) {

        final List<GeoPoint> points = MapUtils.decodePolyline(encoded);
        for (final GeoPoint point : points) {
            this.addPoint(point);
        }
    }

}
