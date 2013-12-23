package fr.itinerennes.ui.adapter;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

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
import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.api.client.ItineRennesApiClient;
import fr.itinerennes.api.client.model.Route;
import fr.itinerennes.api.client.model.StopWithRoutes;
import fr.itinerennes.ui.activity.BusStopActivity_;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.LineImageView;
import fr.itinerennes.ui.views.event.ToggleStarListener;
import fr.itinerennes.ui.views.overlays.OverlayItem;
import fr.itinerennes.ui.views.overlays.StopOverlayItem;

/**
 * @author Jérémie Huchet
 */
public class BusStationBoxAdapter implements MapBoxAdapter<StopWithRoutes> {

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

                final Intent i = new Intent(context, BusStopActivity_.class);
                i.putExtra(BusStopActivity_.INTENT_STOP_ID, busStation.getId());
                i.putExtra(BusStopActivity_.INTENT_STOP_NAME, busStation.getLabel());
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
    public final StopWithRoutes doInBackground(final View view, final OverlayItem item) {

    	StopWithRoutes station = null;
        try {

            final ItineRennesApiClient obaClient = context.getApplicationContext()
                    .getItineRennesApiClient();

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
    public final void updateView(final View view, final StopWithRoutes station) {

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
