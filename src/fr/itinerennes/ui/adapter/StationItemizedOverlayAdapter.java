package fr.itinerennes.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;

import fr.itinerennes.business.service.StationProvider;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Station;
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
    private final Context context;

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
    public StationItemizedOverlayAdapter(final Context context,
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
    public final List<FocusableOverlayItem<T>> getItems(final BoundingBoxE6 bbox) {

        // TJHU attention aux NPE a cause de l'exception si pas de réseau
        // TJHU utiliser un error handler pour notifier les exceptions
        List<T> stations = null;
        try {
            stations = stationProvider.getStations(bbox);
        } catch (final GenericException e) {
            LOGGER.error("unable to retrieve stations", e);
        }
        final List<FocusableOverlayItem<T>> items = new ArrayList<FocusableOverlayItem<T>>(
                stations.size());
        for (final T station : stations) {
            final FocusableOverlayItem<T> item = new FocusableOverlayItem<T>(station.getId(),
                    station.getGeoPoint());
            item.setData(station);
            item.setMarker(context.getResources().getDrawable(station.getIconDrawableId()));
            items.add(item);
        }
        return items;
    }
}
