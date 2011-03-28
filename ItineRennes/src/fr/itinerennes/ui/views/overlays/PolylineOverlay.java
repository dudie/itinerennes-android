package fr.itinerennes.ui.views.overlays;

import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.PathOverlay;

import android.content.Context;

import fr.itinerennes.utils.MapUtils;

public class PolylineOverlay extends PathOverlay {

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
