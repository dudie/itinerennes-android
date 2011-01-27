package fr.itinerennes.ui.adapter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import fr.itinerennes.R;
import fr.itinerennes.business.service.BusRouteService;
import fr.itinerennes.business.service.BusService;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusRoute;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.ui.activity.BusStationActivity;
import fr.itinerennes.ui.views.overlays.ITROverlayItem;

/**
 * @author Jérémie Huchet
 */
public class BusStationBoxAdapter implements MapBoxAdapter<ITROverlayItem<BusStation>, BusStation> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusStationBoxAdapter.class);

    /** The bus service. */
    private final BusService busService;

    /** The bus route service. */
    private final BusRouteService busRouteService;

    /** The line icon service. */
    private final LineIconService lineIconService;

    /**
     * Creates the bus station adapter for map box.
     * 
     * @param busService
     *            the bus service
     * @param busRouteService
     *            the bus route service
     */
    public BusStationBoxAdapter(final BusService busService, final BusRouteService busRouteService,
            final LineIconService lineIconService) {

        this.busService = busService;
        this.busRouteService = busRouteService;
        this.lineIconService = lineIconService;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxTitle(fr.itinerennes.ui.views.overlays.ITROverlayItem)
     */
    @Override
    public final String getBoxTitle(final ITROverlayItem<BusStation> item) {

        return item.getData().getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxIcon(fr.itinerennes.ui.views.overlays.ITROverlayItem)
     */
    @Override
    public final int getBoxIcon(final ITROverlayItem<BusStation> item) {

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
    public final View getBoxDetailsView(final Context context, final ITROverlayItem<BusStation> item) {

        final LayoutInflater inflater = LayoutInflater.from(context);
        final View busInfo = inflater.inflate(R.layout.line_icon_list, null);
        busInfo.setVisibility(View.GONE);
        return busInfo;
    }

    /**
     * Preload data with the services and pray the cache keep them...
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#backgroundLoad(fr.itinerennes.ui.views.overlays.ITROverlayItem)
     */
    @Override
    public final BusStation backgroundLoad(final ITROverlayItem<BusStation> item)
            throws GenericException {

        // TJHU on ne fait que mettre en cache, il vaudrait mieux passer directement les
        // informations !!! (au cas ou le cache expire ou que ce n'est pas bien mis en cache)
        BusStation station = null;
        List<BusRoute> busRoutes = null;
        station = busService.getStation(item.getData().getId());
        busRoutes = busRouteService.getStationRoutes(item.getData().getId());
        for (final BusRoute route : busRoutes) {
            lineIconService.getIcon(route.getId());
        }

        return station;
    }

    /**
     * {@inheritDoc}
     * 
     * @return the name of the bus station
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxDetailsTitle(fr.itinerennes.ui.views.overlays.ITROverlayItem,
     *      java.lang.Object)
     */
    @Override
    public final String getBoxTitle(final ITROverlayItem<BusStation> item, final BusStation data) {

        return getBoxTitle(item);
    }

    /**
     * {@inheritDoc}
     * 
     * @return the bus icon
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getBoxIcon(java.lang.Object)
     */
    @Override
    public final int getBoxIcon(final ITROverlayItem<BusStation> item, final BusStation data) {

        return R.drawable.bus_marker_icon;
    }

    /**
     * Not used for Bus stations, nothing to display.
     * <p>
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateBoxDetailsView(android.view.View,
     *      fr.itinerennes.ui.views.overlays.ITROverlayItem, java.lang.Object)
     */
    @Override
    public final void updateBoxDetailsView(final View busDetailsView,
            final ITROverlayItem<BusStation> item, final BusStation data) {

        final LinearLayout iconsView = (LinearLayout) busDetailsView
                .findViewById(R.id.line_icon_container);
        List<BusRoute> busRoutes;
        try {
            busRoutes = busRouteService.getStationRoutes(item.getData().getId());
            for (final BusRoute route : busRoutes) {

                final View imageContainer = LayoutInflater.from(busDetailsView.getContext())
                        .inflate(R.layout.line_icon, null);
                final ImageView lineIcon = (ImageView) imageContainer
                        .findViewById(R.station.bus_line_icon);
                lineIcon.setImageDrawable(lineIconService.getIconOrDefault(
                        busDetailsView.getContext(), route.getShortName()));
                iconsView.addView(imageContainer);
            }
        } catch (final GenericException e) {
            LOGGER.error("unable to retrieve bus routes and icons for station " + item.getData(), e);
        }
        busDetailsView.setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#beforeStartActivity(java.lang.Object)
     */
    @Override
    public final void beforeStartActivity(final ITROverlayItem<BusStation> item) {

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
            final ITROverlayItem<BusStation> item) {

        final Intent myIntent = new Intent(packageContext, BusStationActivity.class);
        myIntent.putExtra(BusStationActivity.INTENT_STOP_ID, item.getData().getId());
        myIntent.putExtra(BusStationActivity.INTENT_STOP_NAME, item.getData().getName());
        return myIntent;
    }

}
