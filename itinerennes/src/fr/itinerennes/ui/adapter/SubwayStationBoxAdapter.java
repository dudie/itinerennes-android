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

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import fr.dudie.keolis.model.SubwayStation;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.event.ToggleStarListener;
import fr.itinerennes.ui.views.overlays.OverlayItem;
import fr.itinerennes.ui.views.overlays.StopOverlayItem;

/**
 * @author Jérémie Huchet
 */
public class SubwayStationBoxAdapter implements MapBoxAdapter<SubwayStation> {

    /** The itinerennes context. */
    private final ItineRennesActivity context;

    /** The layout inflater. */
    private final LayoutInflater inflater;

    /**
     * Creates the subway station adapter for map box.
     * 
     * @param context
     *            the context
     */
    public SubwayStationBoxAdapter(final ItineRennesActivity context) {

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

        final StopOverlayItem subwayStation = (StopOverlayItem) item;

        final View subwayView = inflater.inflate(R.layout.vw_mapbox_subway, null);
        ((TextView) subwayView.findViewById(R.id.map_box_title)).setText(subwayStation.getLabel());

        final ToggleButton star = (ToggleButton) subwayView
                .findViewById(R.id.map_box_toggle_bookmark);
        star.setChecked(context.getApplicationContext().getBookmarksService()
                .isStarred(TypeConstants.TYPE_SUBWAY, subwayStation.getId()));
        star.setOnCheckedChangeListener(new ToggleStarListener(context, TypeConstants.TYPE_SUBWAY,
                subwayStation.getId(), subwayStation.getLabel()));

        return subwayView;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#onStartLoading(android.view.View)
     */
    @Override
    public final void onStartLoading(final View view) {

        // nothing to load
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#doInBackground(android.view.View,
     *      java.lang.Object)
     */
    @Override
    public final SubwayStation doInBackground(final View view, final OverlayItem item) {

        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateView(android.view.View, java.lang.Object)
     */
    @Override
    public final void updateView(final View view, final SubwayStation item) {

        // nothing to load
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#onStopLoading(android.view.View)
     */
    @Override
    public final void onStopLoading(final View view) {

        // nothing to load
    }

}
