package fr.itinerennes.ui.adapter;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import fr.itinerennes.R;
import fr.itinerennes.model.SubwayStation;
import fr.itinerennes.ui.views.overlays.ITROverlayItem;

/**
 * @author Jérémie Huchet
 */
public class SubwayStationBoxAdapter implements
        MapBoxAdapter<ITROverlayItem<SubwayStation>, SubwayStation> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(SubwayStationBoxAdapter.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxTitle(fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final String getBoxTitle(final ITROverlayItem<SubwayStation> item) {

        return item.getData().getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxIcon(fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final int getBoxIcon(final ITROverlayItem<SubwayStation> item) {

        return R.drawable.subway_marker_icon;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxDetailsView(android.content.Context,
     *      fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final View getBoxDetailsView(final Context context, final ITROverlayItem<SubwayStation> item) {

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#backgroundLoad(fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final SubwayStation backgroundLoad(final ITROverlayItem<SubwayStation> item) {

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxTitle(fr.itinerennes.ui.views.overlays.Marker,
     *      java.lang.Object)
     */
    @Override
    public final String getBoxTitle(final ITROverlayItem<SubwayStation> item, final SubwayStation data) {

        return getBoxTitle(item);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxIcon(fr.itinerennes.ui.views.overlays.Marker,
     *      java.lang.Object)
     */
    @Override
    public final int getBoxIcon(final ITROverlayItem<SubwayStation> item, final SubwayStation data) {

        return getBoxIcon(item);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateBoxDetailsView(android.view.View,
     *      fr.itinerennes.ui.views.overlays.ITROverlayItem, java.lang.Object)
     */
    @Override
    public final void updateBoxDetailsView(final View boxDetailsView,
            final ITROverlayItem<SubwayStation> item, final SubwayStation data) {

        // TJHU Auto-generated method stub

    }

    /**
     * Not used for subway stations, nothing to display.
     * 
     * @return null
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#beforeStartActivity(fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public void beforeStartActivity(final ITROverlayItem<SubwayStation> item) {

        // nothing is done before
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getOnClickIntent(android.content.Context,
     *      fr.itinerennes.ui.views.overlays.Marker)
     */
    @Override
    public final Intent getOnClickIntent(final Context packageContext,
            final ITROverlayItem<SubwayStation> item) {

        return null;
    }
}
