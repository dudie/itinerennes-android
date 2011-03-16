package fr.itinerennes.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import fr.itinerennes.business.service.MarkerProvider;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Station;
import fr.itinerennes.ui.activity.ItinerennesContext;
import fr.itinerennes.ui.views.overlays.old.SelectableMarker;

/**
 * @author Jérémie Huchet
 */
public class MarkerItemizedOverlayAdapter<T extends Station> implements
        ItemizedOverlayAdapter<SelectableMarker<T>> {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(MarkerItemizedOverlayAdapter.class);

    /** The itinerennes application context. */
    private final ItinerennesContext context;

    /** The station provider. */
    private final MarkerProvider<T> stationProvider;

    /**
     * Creates a map marker adapter to display stations markers retrieved using a
     * {@link MarkerProvider}.
     * 
     * @param context
     *            the itinerennes application context
     * @param stationProvider
     *            the station provider to use
     */
    public MarkerItemizedOverlayAdapter(final ItinerennesContext context,
            final MarkerProvider<T> stationProvider) {

        this.context = context;
        this.stationProvider = stationProvider;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.ItemizedOverlayAdapter#getMarkers(org.andnav.osm.util.BoundingBoxE6)
     */
    @Override
    public final List<SelectableMarker<T>> getItems(final BoundingBoxE6 bbox)
            throws GenericException {

        final List<SelectableMarker<T>> items;

        final List<T> stations = stationProvider.getStations(bbox);

        if (stations != null) {
            items = new ArrayList<SelectableMarker<T>>(stations.size());

            for (final T station : stations) {
                final SelectableMarker<T> item = new SelectableMarker<T>(station.getId(),
                        station.getGeoPoint());
                item.setData(station);
                item.setMarker(context.getResources().getDrawable(station.getIconDrawableId()));
                items.add(item);
            }
        } else {
            items = new ArrayList<SelectableMarker<T>>(0);
        }

        return items;
    }
}
