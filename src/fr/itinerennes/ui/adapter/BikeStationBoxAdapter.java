package fr.itinerennes.ui.adapter;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.business.service.BikeService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BikeStation;
import fr.itinerennes.ui.views.overlays.ITROverlayItem;

/**
 * @author Jérémie Huchet
 */
public class BikeStationBoxAdapter implements
        MapBoxAdapter<ITROverlayItem<BikeStation>, BikeStation> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BikeStationBoxAdapter.class);

    /** The bike service. */
    private final BikeService bikeService;

    /**
     * Creates the bike station adapter for map box.
     * 
     * @param bikeService
     *            the bike service
     */
    public BikeStationBoxAdapter(final BikeService bikeService) {

        this.bikeService = bikeService;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxTitle(fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final String getBoxTitle(final ITROverlayItem<BikeStation> item) {

        return item.getData().getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxIcon(fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final int getBoxIcon(final ITROverlayItem<BikeStation> item) {

        return R.drawable.bike_marker_icon;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxDetailsView(fr.itinerennes.ui.views.overlays.Marker,
     *      java.lang.Object)
     */
    @Override
    public final View getBoxDetailsView(final Context context,
            final ITROverlayItem<BikeStation> item) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        final LinearLayout bikeInfo = (LinearLayout) inflater.inflate(R.layout.bike_station_box,
                null);
        bikeInfo.setVisibility(View.VISIBLE);
        return bikeInfo;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#backgroundLoad(fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final BikeStation backgroundLoad(final ITROverlayItem<BikeStation> item)
            throws GenericException {

        final BikeStation upToDateStation = bikeService.getFreshStation(item.getData().getId());
        return upToDateStation;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxTitle(fr.itinerennes.ui.views.overlays.Marker,
     *      java.lang.Object)
     */
    @Override
    public final String getBoxTitle(final ITROverlayItem<BikeStation> item, final BikeStation data) {

        final String title;
        if (data == null) {
            title = getBoxTitle(item);
        } else {
            title = data.getName();
        }
        return title;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxIcon(fr.itinerennes.ui.views.overlays.Marker,
     *      java.lang.Object)
     */
    @Override
    public final int getBoxIcon(final ITROverlayItem<BikeStation> item, final BikeStation data) {

        return getBoxIcon(item);
    }

    /**
     * Updates the bike station details with the retrieved values (available bikes/slots).
     * <p>
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateBoxDetailsView(android.view.View,
     *      fr.itinerennes.ui.views.overlays.ITROverlayItem, java.lang.Object)
     */
    @Override
    public final void updateBoxDetailsView(final View bikeInfo,
            final ITROverlayItem<BikeStation> item, final BikeStation bikeStation) {

        if (bikeStation == null) {
            // an error occurred during backgroundLoad()
            bikeInfo.setVisibility(View.GONE);
            return;
        }

        final TextView availablesSlots = (TextView) bikeInfo.findViewById(R.id.available_slots);
        availablesSlots.setText(String.valueOf(bikeStation.getAvailableSlots()));

        final TextView availablesBikes = (TextView) bikeInfo.findViewById(R.id.available_bikes);
        availablesBikes.setText(String.valueOf(bikeStation.getAvailableBikes()));

        final ProgressBar gauge = (ProgressBar) bikeInfo.findViewById(R.id.bike_station_gauge);
        gauge.setMax(bikeStation.getAvailableBikes() + bikeStation.getAvailableSlots());
        gauge.setIndeterminate(false);
        gauge.setProgress(bikeStation.getAvailableBikes());
        gauge.setSecondaryProgress(bikeStation.getAvailableBikes()
                + bikeStation.getAvailableSlots());

    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#beforeStartActivity(fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final void beforeStartActivity(final ITROverlayItem<BikeStation> item) {

        // nothing to do
    }

    /**
     * No intent has to be load on box click.
     * <p>
     * {@inheritDoc}
     * 
     * @return null
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getOnClickIntent(android.content.Context,
     *      fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final Intent getOnClickIntent(final Context packageContext,
            final ITROverlayItem<BikeStation> item) {

        return null;
    }

}
