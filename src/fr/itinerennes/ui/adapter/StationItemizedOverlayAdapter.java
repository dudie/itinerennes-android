package fr.itinerennes.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.business.service.StationProvider;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Station;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.views.overlays.FocusableOverlayItem;

/**
 * @author Jérémie Huchet
 */
public class StationItemizedOverlayAdapter<T extends Station> implements
        ItemizedOverlayAdapter<FocusableOverlayItem<T>, T> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(StationItemizedOverlayAdapter.class);

    /** The itinerennes application context. */
    private final ITRContext context;

    /** The station provider. */
    private final StationProvider<T> stationProvider;

    /**
     * Creates a map marker adapter to display stations markers retrieved using a
     * {@link StationProvider}.
     * 
     * @param context
     *            the itinerennes application context
     * @param stationProvider
     *            the station provider to use
     */
    public StationItemizedOverlayAdapter(final ITRContext context,
            final StationProvider<T> stationProvider) {

        this.context = context;
        this.stationProvider = stationProvider;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.ItemizedOverlayAdapter#getMarkers(org.andnav.osm.util.BoundingBoxE6)
     */
    @Override
    public final List<FocusableOverlayItem<T>> getItems(final BoundingBoxE6 bbox)
            throws GenericException {

        final List<FocusableOverlayItem<T>> items;

        final List<T> stations = stationProvider.getStations(bbox);

        if (stations != null) {
            items = new ArrayList<FocusableOverlayItem<T>>(stations.size());

            for (final T station : stations) {
                final FocusableOverlayItem<T> item = new FocusableOverlayItem<T>(station.getId(),
                        station.getGeoPoint());
                item.setData(station);
                item.setMarker(context.getResources().getDrawable(station.getIconDrawableId()));
                items.add(item);
            }
        } else {
            items = new ArrayList<FocusableOverlayItem<T>>(0);
        }

        return items;
    }
}
