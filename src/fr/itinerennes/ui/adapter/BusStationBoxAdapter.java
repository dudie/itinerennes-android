package fr.itinerennes.ui.adapter;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.onebusaway.client.IOneBusAwayClient;
import fr.itinerennes.onebusaway.model.Route;
import fr.itinerennes.onebusaway.model.Stop;
import fr.itinerennes.ui.activity.BusStopActivity;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.event.ToggleStarListener;
import fr.itinerennes.ui.views.overlays.MarkerOverlayItem;

/**
 * @author Jérémie Huchet
 */
public class BusStationBoxAdapter implements MapBoxAdapter<Stop> {

    /** The itinerennes context. */
    private final ItineRennesActivity context;

    /** The layout inflater. */
    private final LayoutInflater inflater;

    /** The bus station for which informations are displayed. */
    private Stop station = null;

    /**
     * Creates the bus station adapter for map box.
     * 
     * @param context
     *            the context
     */
    public BusStationBoxAdapter(final ItineRennesActivity context) {

        this.context = context;
        inflater = context.getLayoutInflater();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getView(java.lang.Object)
     */
    @Override
    public final View getView(final MarkerOverlayItem item) {

        final View busView = inflater.inflate(R.layout.vw_mapbox_bus, null);
        ((TextView) busView.findViewById(R.id.map_box_title)).setText(item.getLabel());
        busView.findViewById(R.id.line_icon_container).setVisibility(View.GONE);

        busView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                final Intent i = new Intent(context, BusStopActivity.class);
                i.putExtra(BusStopActivity.INTENT_STOP_ID, item.getId());
                i.putExtra(BusStopActivity.INTENT_STOP_NAME, item.getLabel());
                context.startActivity(i);
            }
        });

        final ToggleButton star = (ToggleButton) busView.findViewById(R.id.map_box_toggle_bookmark);
        star.setChecked(context.getApplicationContext().getBookmarksService()
                .isStarred(TypeConstants.TYPE_BUS, item.getId()));
        star.setOnCheckedChangeListener(new ToggleStarListener(context, TypeConstants.TYPE_BUS,
                item.getId(), item.getLabel()));

        final ImageView handistar = (ImageView) busView.findViewById(R.id.map_box_wheelchair);
        if (context.getApplicationContext().getAccessibilityService()
                .isAccessible(item.getId(), TypeConstants.TYPE_BUS)) {
            handistar.setVisibility(View.VISIBLE);
        }
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
     * @return
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#doInBackground(android.view.View,
     *      java.lang.Object)
     */
    @Override
    public final Stop doInBackground(final View view, final MarkerOverlayItem item) {

        station = null;
        try {

            final IOneBusAwayClient obaClient = context.getApplicationContext()
                    .getOneBusAwayClient();

            station = obaClient.getStop(item.getId());

        } catch (final IOException e) {
            context.getApplicationContext().getExceptionHandler().handleException(e);
        }

        return station;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateView(android.view.View, java.lang.Object)
     */
    @Override
    public final void updateView(final View view, final Stop item) {

        if (station != null) {
            // updates the station name just to be sure
            ((TextView) view.findViewById(R.id.map_box_title)).setText(station.getName());

            final LinearLayout iconsView = (LinearLayout) view
                    .findViewById(R.id.line_icon_container);

            final List<Route> busRoutes = station.getRoutes();
            if (!busRoutes.isEmpty()) {
                for (final Route route : busRoutes) {

                    final View imageContainer = inflater.inflate(R.layout.li_line_icon, null);
                    final ImageView lineIcon = (ImageView) imageContainer
                            .findViewById(R.station.bus_line_icon);
                    lineIcon.setImageDrawable(context.getApplicationContext().getLineIconService()
                            .getIconOrDefault(context, route.getShortName()));
                    iconsView.addView(imageContainer);
                }
                // set the list of routes icons visible only if it contains icons
                view.findViewById(R.id.line_icon_container).setVisibility(View.VISIBLE);
            }
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
