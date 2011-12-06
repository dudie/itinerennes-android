package fr.itinerennes.ui.adapter;

import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import fr.dudie.onebusaway.client.IOneBusAwayClient;
import fr.dudie.onebusaway.model.Route;
import fr.dudie.onebusaway.model.Stop;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.ui.activity.BusStopActivity;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.LineImageView;
import fr.itinerennes.ui.views.event.ToggleStarListener;
import fr.itinerennes.ui.views.overlays.OverlayItem;
import fr.itinerennes.ui.views.overlays.StopOverlayItem;

/**
 * @author Jérémie Huchet
 */
public class BusStationBoxAdapter implements MapBoxAdapter<Stop> {

    /** The itinerennes context. */
    private final ItineRennesActivity context;

    /** The layout inflater. */
    private final LayoutInflater inflater;

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
    public final View getView(final OverlayItem item) {

        final StopOverlayItem busStation = (StopOverlayItem) item;

        final View busView = inflater.inflate(R.layout.vw_mapbox_bus, null);
        ((TextView) busView.findViewById(R.id.map_box_title)).setText(busStation.getLabel());
        busView.findViewById(R.id.line_icon_container).setVisibility(View.GONE);

        busView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {

                final Intent i = new Intent(context, BusStopActivity.class);
                i.putExtra(BusStopActivity.INTENT_STOP_ID, busStation.getId());
                i.putExtra(BusStopActivity.INTENT_STOP_NAME, busStation.getLabel());
                context.startActivity(i);
            }
        });

        final ToggleButton star = (ToggleButton) busView.findViewById(R.id.map_box_toggle_bookmark);
        star.setChecked(context.getApplicationContext().getBookmarksService()
                .isStarred(TypeConstants.TYPE_BUS, busStation.getId()));
        star.setOnCheckedChangeListener(new ToggleStarListener(context, TypeConstants.TYPE_BUS,
                busStation.getId(), busStation.getLabel()));

        final ImageView handistar = (ImageView) busView.findViewById(R.id.map_box_wheelchair);
        if (context.getApplicationContext().getAccessibilityService()
                .isAccessible(busStation.getId(), TypeConstants.TYPE_BUS)) {
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
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#doInBackground(android.view.View,
     *      java.lang.Object)
     */
    @Override
    public final Stop doInBackground(final View view, final OverlayItem item) {

        Stop station = null;
        try {

            final IOneBusAwayClient obaClient = context.getApplicationContext()
                    .getOneBusAwayClient();

            station = obaClient.getStop(((StopOverlayItem) item).getId());

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
    public final void updateView(final View view, final Stop station) {

        if (station != null) {
            // updates the station name just to be sure
            ((TextView) view.findViewById(R.id.map_box_title)).setText(station.getName());

            final ViewGroup iconsView = (ViewGroup) view.findViewById(R.id.line_icon_container);

            final List<Route> busRoutes = station.getRoutes();
            if (!busRoutes.isEmpty()) {
                for (final Route route : busRoutes) {

                    final LineImageView lineIcon = new LineImageView(context);
                    lineIcon.setLine(route.getShortName());
                    lineIcon.fitToHeight(24);
                    lineIcon.setPadding(2, 0, 2, 0);
                    iconsView.addView(lineIcon);
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
