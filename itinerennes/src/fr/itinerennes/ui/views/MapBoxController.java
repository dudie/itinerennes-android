package fr.itinerennes.ui.views;

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

import org.osmdroid.views.MapView;
import org.osmdroid.views.MapView.LayoutParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import fr.dudie.keolis.model.BikeStation;
import fr.dudie.keolis.model.RelayPark;
import fr.dudie.keolis.model.SubwayStation;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.api.client.model.StopWithRoutes;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.adapter.BikeStationBoxAdapter;
import fr.itinerennes.ui.adapter.BusStationBoxAdapter;
import fr.itinerennes.ui.adapter.MapBoxAdapter;
import fr.itinerennes.ui.adapter.ParkBoxAdapter;
import fr.itinerennes.ui.adapter.SubwayStationBoxAdapter;
import fr.itinerennes.ui.views.overlays.OverlayItem;

/**
 * Manage displaying of the map box additional informations view.
 * 
 * @author Jérémie Huchet
 */
public final class MapBoxController {

    /** The map view. */
    private final MapView map;

    /** The itinerennes context. */
    private final ItineRennesActivity context;

    /** On display animation. */
    private final Animation fadeIn;

    /** On hide animation. */
    private final Animation fadeOut;

    /** The current selected item. */
    private OverlayItem selectedItem;

    /**
     * The task used to fill the map box view with additional information in background for a bike
     * station.
     */
    private DisplayMapBoxTask<BikeStation> bikeMapBoxDisplayer = null;

    /**
     * The task used to fill the map box view with additional information in background for a bus
     * station.
     */
    private DisplayMapBoxTask<StopWithRoutes> busMapBoxDisplayer = null;

    /**
     * The task used to fill the map box view with additional information in background for a subway
     * station.
     */
    private DisplayMapBoxTask<SubwayStation> subwayMapBoxDisplayer = null;

    /**
     * The task used to fill the map box view with additional information in background for car
     * park.
     */
    private DisplayMapBoxTask<RelayPark> parkMapBoxDisplayer = null;

    /**
     * Constructs the map box controller.
     * 
     * @param map
     *            the map view
     */
    public MapBoxController(final MapView map) {

        this.map = map;
        this.context = (ItineRennesActivity) map.getContext();
        fadeIn = AnimationUtils.loadAnimation(map.getContext(), android.R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(map.getContext(), android.R.anim.fade_out);
    }

    /**
     * Displays a map box containing informations about the given item.
     * 
     * @param item
     *            the item
     */
    public void show(final OverlayItem item) {

        cancelAll();
        hide();

        selectedItem = item;

        if (item.getType().equals(TypeConstants.TYPE_BIKE)) {
            bikeMapBoxDisplayer = new DisplayMapBoxTask<BikeStation>(map,
                    new BikeStationBoxAdapter(context), item);
            bikeMapBoxDisplayer.execute();
        } else if (item.getType().equals(TypeConstants.TYPE_BUS)) {
            busMapBoxDisplayer = new DisplayMapBoxTask<StopWithRoutes>(map,
                    new BusStationBoxAdapter(context), item);
            busMapBoxDisplayer.execute();
        } else if (item.getType().equals(TypeConstants.TYPE_SUBWAY)) {
            subwayMapBoxDisplayer = new DisplayMapBoxTask<SubwayStation>(map,
                    new SubwayStationBoxAdapter(context), item);
            subwayMapBoxDisplayer.execute();
        } else if (item.getType().equals(TypeConstants.TYPE_CAR_PARK)) {
            parkMapBoxDisplayer = new DisplayMapBoxTask<RelayPark>(map,
                    new ParkBoxAdapter(context), item);
            parkMapBoxDisplayer.execute();
        }
    }

    /**
     * Hides the map box.
     */
    public void hide() {

        cancelAll();

        final View mapBox = map.findViewWithTag(selectedItem);
        if (null != mapBox) {
            mapBox.startAnimation(fadeOut);
            map.removeView(mapBox);
        }

        selectedItem = null;
    }

    /**
     * Cancel all current loading tasks not finished.
     */
    private void cancelAll() {

        if (bikeMapBoxDisplayer != null && bikeMapBoxDisplayer.getStatus() != Status.FINISHED) {
            bikeMapBoxDisplayer.cancel(true);
        }

        if (busMapBoxDisplayer != null && busMapBoxDisplayer.getStatus() != Status.FINISHED) {
            busMapBoxDisplayer.cancel(true);
        }

        if (subwayMapBoxDisplayer != null && subwayMapBoxDisplayer.getStatus() != Status.FINISHED) {
            subwayMapBoxDisplayer.cancel(true);
        }
    }

    /**
     * Gets the selected item.
     * 
     * @return the selected item
     */
    public OverlayItem getSelectedItem() {

        return selectedItem;
    }

    /**
     * @param <D>
     *            the type of the bundled data with the item
     * @author Jérémie Huchet
     */
    private class DisplayMapBoxTask<D> extends AsyncTask<Void, Void, D> {

        /** The event logger. */
        private final Logger LOGGER = LoggerFactory.getLogger(DisplayMapBoxTask.class);

        /** The map view where the additional informations are added. */
        private final MapView map;

        /** The {@link MapBoxAdapter} used to display the map box informations. */
        private final MapBoxAdapter<D> adapter;

        /** The marker displayed. */
        private final OverlayItem item;

        /** The box view to display inside the map. */
        private View boxView;

        /**
         * Creates the task which will display the map box.
         * 
         * @param map
         *            the map view where the additional informations are added
         * @param adapter
         *            the map box adapter
         * @param item
         *            the metadata of the marker displayed
         */
        public DisplayMapBoxTask(final MapView map, final MapBoxAdapter<D> adapter,
                final OverlayItem item) {

            super();
            this.map = map;
            this.adapter = adapter;
            this.item = item;
        }

        /**
         * Active the loading icon.
         * 
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onPreExecute.start - item={}", item);
            }

            boxView = adapter.getView(item);
            boxView.setTag(item);
            boxView.startAnimation(fadeIn);

            final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT, item.getLocation(), LayoutParams.BOTTOM_CENTER, 0, 0);
            boxView.setLayoutParams(params);

            map.addView(boxView);

            adapter.onStartLoading(boxView);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onPreExecute.end - item={}", item);
            }
        }

        /**
         * Uses the adapter to load some data in background.
         * <p>
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#doInBackground(Params[])
         */
        @Override
        protected D doInBackground(final Void... params) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("doInBackground.start - item={}", item);
            }

            D result = null;
            try {
                result = adapter.doInBackground(boxView, item);
            } catch (final Exception e) {
                // TJHU Gestion d'erreur : async task perdue
                // on fait un catch Exception pour éviter de faire planter l'application si l'async
                // task se vautre alors que l'activité est en pause
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.error("async task failed", e);
                }
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("doInBackground.end - item={}", item);
            }

            return result;
        }

        /**
         * Updates the map box view with the loaded data and deactive the loadinf icon.
         * <p>
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(final D data) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onPostExecute.start - data={}", data);
            }
            adapter.updateView(boxView, data);
            adapter.onStopLoading(boxView);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onPostExecute.end - data={}", data);
            }
        }

        /**
         * {@inheritDoc}
         * 
         * @see android.os.AsyncTask#onCancelled()
         */
        @Override
        protected void onCancelled() {

            super.onCancelled();

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("onCancelled.start/stop - isCancelled={}", isCancelled());
            }
        }
    }
}
