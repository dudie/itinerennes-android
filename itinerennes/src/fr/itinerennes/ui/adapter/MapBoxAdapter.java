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

import android.view.View;

import fr.itinerennes.ui.views.overlays.OverlayItem;

/**
 * Common base interface of implementation for an adapter that can be used to fill the MapBox.
 * <p>
 * See {@link DisplayMapBoxTask} for other details about the box loading.
 * 
 * @param <R>
 *            the type of data the adapter uses
 * @author Jérémie Huchet
 */
public interface MapBoxAdapter<R> {

    /**
     * This method should initializes the view displaying additional informations for the given
     * item. Execution must be quick as it is executed by the UI thread.
     * 
     * @param item
     *            the item for which the view must be generated
     * @return the view to display in the map box
     */
    View getView(OverlayItem item);

    /**
     * Called before the {@link #doInBackground(View, Object)} begins.
     * 
     * @param view
     *            the view you returned in {@link #getView(Object)}
     */
    void onStartLoading(View view);

    /**
     * This method allow you to do some tasks in a background thread. If you use some global
     * variables in the adapter, be sure to reinitializes them.
     * 
     * @param view
     *            the view you returned in {@link #getView(Object)}
     * @param item
     *            the item for which this adapter generates a view
     * @return some data helping you to fill the view
     */
    R doInBackground(View view, OverlayItem item);

    /**
     * This method is called after the method {@link #doInBackground(View, Object)} finished.
     * 
     * @param view
     *            the view you returned in {@link #getView(Object)}
     * @param item
     *            the item for which this adapter generates a view
     */
    void updateView(View view, R item);

    /**
     * Called after the {@link #updateView(View, Object)}.
     * 
     * @param view
     *            the view you returned in {@link #getView(Object)}
     */
    void onStopLoading(View view);

}
