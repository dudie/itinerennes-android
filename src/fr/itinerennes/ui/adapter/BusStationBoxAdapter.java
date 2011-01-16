package fr.itinerennes.ui.adapter;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import fr.itinerennes.R;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.ui.activity.BusStationActivity;
import fr.itinerennes.ui.views.overlays.OverlayItem;

/**
 * @author Jérémie Huchet
 */
public class BusStationBoxAdapter implements MapBoxAdapter<OverlayItem<BusStation>, BusStation> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusStationBoxAdapter.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxTitle(fr.itinerennes.ui.views.overlays.OverlayItem)
     */
    @Override
    public final String getBoxTitle(final OverlayItem<BusStation> item) {

        return item.getData().getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxIcon(fr.itinerennes.ui.views.overlays.OverlayItem)
     */
    @Override
    public final int getBoxIcon(final OverlayItem<BusStation> item) {

        return R.drawable.bus_marker_icon_focusable;
    }

    /**
     * Not used for Bus stations, nothing to display.
     * <p>
     * {@inheritDoc}
     * 
     * @return null
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxDetailsView(java.lang.Object)
     */
    @Override
    public final View getBoxDetailsView(final Context context, final OverlayItem<BusStation> item) {

        // there nothing to display
        return null;
    }

    /**
     * The bus stations does not need to load something in background.
     * 
     * @return null
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#backgroundLoad(fr.itinerennes.ui.views.overlays.OverlayItem)
     */
    @Override
    public final BusStation backgroundLoad(final OverlayItem<BusStation> item) {

        // there nothing to preload, everything is in the overlay
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the name of the bus station
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxDetailsTitle(fr.itinerennes.ui.views.overlays.OverlayItem,
     *      java.lang.Object)
     */
    @Override
    public final String getBoxTitle(final OverlayItem<BusStation> item, final BusStation data) {

        return getBoxTitle(item);
    }

    /**
     * {@inheritDoc}
     * 
     * @return the bus icon
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxIcon(java.lang.Object)
     */
    @Override
    public final int getBoxIcon(final OverlayItem<BusStation> item, final BusStation data) {

        return R.drawable.bus_marker_icon;
    }

    /**
     * Not used for Bus stations, nothing to display.
     * <p>
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateBoxDetailsView(android.view.View,
     *      fr.itinerennes.ui.views.overlays.OverlayItem, java.lang.Object)
     */
    @Override
    public void updateBoxDetailsView(final View boxDetailsView, final OverlayItem<BusStation> item,
            final BusStation data) {

        // TJHU Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#beforeStartActivity(java.lang.Object)
     */
    @Override
    public final void beforeStartActivity(final OverlayItem<BusStation> item) {

    }

    /**
     * Returns an intent that launch the {@link BusStationActivity} for the current station.
     * 
     * @return the intent to lauch the {@link BusStationActivity}
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getOnClickIntent(android.content.Context,
     *      java.lang.Object)
     */
    @Override
    public final Intent getOnClickIntent(final Context packageContext,
            final OverlayItem<BusStation> item) {

        final Intent myIntent = new Intent(packageContext, BusStationActivity.class);
        myIntent.putExtra("item", item.getData().getId());
        return myIntent;
    }

}
