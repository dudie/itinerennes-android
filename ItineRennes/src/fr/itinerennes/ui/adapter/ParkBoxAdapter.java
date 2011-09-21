package fr.itinerennes.ui.adapter;

import java.io.IOException;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import fr.dudie.keolis.model.RelayPark;
import fr.dudie.keolis.model.RelayParkState;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.overlays.OverlayItem;
import fr.itinerennes.ui.views.overlays.ParkOverlayItem;

/**
 * @author Olivier Boudet
 */
public class ParkBoxAdapter implements MapBoxAdapter<RelayPark> {

    /** The itinerennes context. */
    private final ItineRennesActivity context;

    /** The layout inflater. */
    private final LayoutInflater inflater;

    /**
     * Creates the park adapter for map box.
     * 
     * @param context
     *            the context
     */
    public ParkBoxAdapter(final ItineRennesActivity context) {

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

        final ParkOverlayItem park = (ParkOverlayItem) item;

        final View parkView = inflater.inflate(R.layout.vw_mapbox_park, null);
        ((TextView) parkView.findViewById(R.park_map_box.map_box_title)).setText(park.getLabel());

        return parkView;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#onStartLoading(android.view.View)
     */
    @Override
    public final void onStartLoading(final View view) {

        view.findViewById(R.park_map_box.map_box_progressbar).setVisibility(View.VISIBLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#doInBackground(android.view.View,
     *      java.lang.Object)
     */
    @Override
    public final RelayPark doInBackground(final View view, final OverlayItem item) {

        RelayPark carPark = null;

        try {
            for (final RelayPark park : context.getApplicationContext().getKeolisClient()
                    .getAllRelayParks()) {
                if (item.getLocation().getLatitudeE6() == park.getLatitude()
                        && item.getLocation().getLongitudeE6() == park.getLongitude()) {
                    carPark = park;
                }
            }

        } catch (final IOException e) {
            context.getApplicationContext().getExceptionHandler().handleException(e);
            return null;
        }

        return carPark;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateView(android.view.View, java.lang.Object)
     */
    @Override
    public final void updateView(final View view, final RelayPark upToDatePark) {

        final View parkDetails = view.findViewById(R.park_map_box.details);

        if (upToDatePark == null) {
            // an error occurred during backgroundLoad()
            parkDetails.setVisibility(View.GONE);
            return;
        } else {
            parkDetails.setVisibility(View.VISIBLE);
        }

        final TextView availablesSpaces = (TextView) parkDetails
                .findViewById(R.park_map_box.text_available_spaces);
        final ProgressBar gauge = (ProgressBar) parkDetails.findViewById(R.park_map_box.gauge);
        final TextView textClosed = (TextView) parkDetails.findViewById(R.park_map_box.text_closed);

        if (upToDatePark.getState().equals(RelayParkState.CLOSED)) {
            textClosed.setVisibility(View.VISIBLE);
        } else {

            availablesSpaces.setVisibility(View.VISIBLE);
            gauge.setVisibility(View.VISIBLE);

            final String availablesPlacesText = String.format("%s %s",
                    String.valueOf(upToDatePark.getCarParkAvailable()),
                    context.getString(R.string.available_spaces));
            availablesSpaces.setText(availablesPlacesText);

            gauge.setMax(upToDatePark.getCarParkCapacity());
            gauge.setIndeterminate(false);
            gauge.setProgress(upToDatePark.getCarParkCapacity()
                    - upToDatePark.getCarParkAvailable());
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#onStopLoading(android.view.View)
     */
    @Override
    public final void onStopLoading(final View view) {

        view.findViewById(R.park_map_box.map_box_progressbar).setVisibility(View.GONE);
    }

}
