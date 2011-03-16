package fr.itinerennes.ui.views.overlays.old;

import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.PathOverlay;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.content.Context;

import fr.itinerennes.utils.MapUtils;

public class PolylineOverlay extends PathOverlay {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(PolylineOverlay.class);

    public PolylineOverlay(final int color, final Context context) {

        super(color, context);
    }

    public final void setPolyline(final String encoded) {

        final List<GeoPoint> points = MapUtils.decodePolyline(encoded);
        for (final GeoPoint point : points) {
            this.addPoint(point);
        }
    }

}
