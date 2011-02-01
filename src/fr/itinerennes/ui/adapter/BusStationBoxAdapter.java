package fr.itinerennes.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.model.oba.Route;
import fr.itinerennes.model.oba.Stop;
import fr.itinerennes.ui.activity.BusStationActivity;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.views.event.ToggleStarListener;
import fr.itinerennes.ui.views.overlays.SelectableMarker;

/**
 * @author Jérémie Huchet
 */
public class BusStationBoxAdapter implements MapBoxAdapter<SelectableMarker<BusStation>> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusStationBoxAdapter.class);

    /** The itinerennes context. */
    private final ITRContext context;

    /** The layout inflater. */
    private final LayoutInflater inflater;

    /** The bus station for which informations are displayed. */
    private Stop station = null;

    /** The icons for each route stopping at this stop. */
    private List<Drawable> routesIcons;

    /**
     * Creates the bus station adapter for map box.
     * 
     * @param context
     *            the context
     */
    public BusStationBoxAdapter(final ITRContext context) {

        this.context = context;
        inflater = context.getLayoutInflater();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getView(java.lang.Object)
     */
    @Override
    public final View getView(final SelectableMarker<BusStation> item) {

        final View busView = inflater.inflate(R.layout.map_box_bus, null);
        ((TextView) busView.findViewById(R.id.map_box_title)).setText(item.getData().getName());
        busView.findViewById(R.id.line_icon_container).setVisibility(View.GONE);

        busView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                final Intent i = new Intent(context, BusStationActivity.class);
                // TOBO on ajoute '1_' temporairement il faut utiliser le agencyId a la place
                final String stopId = String.format("%s%s",
                        ItineRennesConstants.OBA_AGENCY_ID_PREFIX, item.getData().getId());
                i.putExtra(BusStationActivity.INTENT_STOP_ID, stopId);
                i.putExtra(BusStationActivity.INTENT_STOP_NAME, item.getData().getName());
                context.startActivity(i);
            }
        });

        final ToggleButton star = (ToggleButton) busView.findViewById(R.id.map_box_toggle_bookmark);
        star.setChecked(context.getBookmarksService().isStarred(BusStation.class.getName(),
                item.getData().getId()));
        star.setOnCheckedChangeListener(new ToggleStarListener(context, BusStation.class.getName(),
                item.getData().getId(), item.getData().getName()));

        return busView;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#onStartLoading(android.view.View)
     */
    @Override
    public final void onStartLoading(final View view) {

        view.findViewById(R.id.map_box_progressbar).setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#doInBackground(android.view.View,
     *      java.lang.Object)
     */
    @Override
    public final void doInBackground(final View view, final SelectableMarker<BusStation> item) {

        station = null;
        routesIcons = new ArrayList<Drawable>();
        try {
            // TOBO on ajoute '1_' temporairement il faut utiliser le agencyId a la place
            final String stopId = String.format("%s%s", ItineRennesConstants.OBA_AGENCY_ID_PREFIX,
                    item.getData().getId());
            station = context.getOneBusAwayService().getStop(stopId);
            final List<Route> busRoutes = station.getRoutes();
            for (final Route route : busRoutes) {
                routesIcons.add(context.getLineIconService().getIconOrDefault(context,
                        route.getShortName()));
            }
        } catch (final GenericException e) {
            context.getExceptionHandler().handleException(e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateView(android.view.View, java.lang.Object)
     */
    @Override
    public final void updateView(final View view, final SelectableMarker<BusStation> item) {

        // updates the station name just to be sure
        if (station != null) {
            ((TextView) view.findViewById(R.id.map_box_title)).setText(station.getName());
        }
        // if some routes stop at this station (there should be at least one route)
        if (!routesIcons.isEmpty()) {
            // finds the icons container (an horizontal scrollable list)
            final LinearLayout iconsView = (LinearLayout) view
                    .findViewById(R.id.line_icon_container);
            // adds the icon for each route
            for (final Drawable icon : routesIcons) {
                final View imageContainer = inflater.inflate(R.layout.line_icon, null);
                final ImageView lineIcon = (ImageView) imageContainer
                        .findViewById(R.station.bus_line_icon);
                lineIcon.setImageDrawable(icon);
                iconsView.addView(imageContainer);
            }
            // set the list of routes icons visible only if it contains icons
            view.findViewById(R.id.line_icon_container).setVisibility(View.VISIBLE);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#onStopLoading(android.view.View)
     */
    @Override
    public final void onStopLoading(final View view) {

        view.findViewById(R.id.map_box_progressbar).setVisibility(View.GONE);
    }

}
