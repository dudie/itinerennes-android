package fr.itinerennes.ui.adapter;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BikeStation;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.views.event.ToggleStarListener;
import fr.itinerennes.ui.views.overlays.SelectableMarker;

/**
 * @author Jérémie Huchet
 */
public class BikeStationBoxAdapter implements MapBoxAdapter<SelectableMarker<BikeStation>> {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(BikeStationBoxAdapter.class);

    /** The itinerennes context. */
    private final ITRContext context;

    /** The layout inflater. */
    private final LayoutInflater inflater;

    /** The bike station with data refreshed less than 1 minute ago. */
    private BikeStation upToDateStation;

    /**
     * Creates the bus station adapter for map box.
     * 
     * @param context
     *            the context
     */
    public BikeStationBoxAdapter(final ITRContext context) {

        this.context = context;
        inflater = context.getLayoutInflater();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getView(java.lang.Object)
     */
    @Override
    public final View getView(final SelectableMarker<BikeStation> item) {

        final View bikeView = inflater.inflate(R.layout.map_box_bike, null);
        ((TextView) bikeView.findViewById(R.id.map_box_title)).setText(item.getData().getName());

        final ToggleButton star = (ToggleButton) bikeView
                .findViewById(R.id.map_box_toggle_bookmark);
        star.setChecked(context.getBookmarksService().isStarred(BikeStation.class.getName(),
                item.getData().getId()));
        star.setOnCheckedChangeListener(new ToggleStarListener(context,
                BikeStation.class.getName(), item.getData().getId(), item.getData().getName()));

        return bikeView;
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
    public final void doInBackground(final View view, final SelectableMarker<BikeStation> item) {

        try {
            upToDateStation = context.getBikeService().getFreshStation(item.getData().getId());
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
    public final void updateView(final View view, final SelectableMarker<BikeStation> item) {

        final View bikeStationDetails = view.findViewById(R.id.map_box_bike_details);

        if (upToDateStation == null) {
            // an error occurred during backgroundLoad()
            bikeStationDetails.setVisibility(View.GONE);
            return;
        } else {
            bikeStationDetails.setVisibility(View.VISIBLE);
        }

        final TextView availablesSlots = (TextView) bikeStationDetails
                .findViewById(R.id.available_slots);
        availablesSlots.setText(String.valueOf(upToDateStation.getAvailableSlots()));

        final TextView availablesBikes = (TextView) bikeStationDetails
                .findViewById(R.id.available_bikes);
        availablesBikes.setText(String.valueOf(upToDateStation.getAvailableBikes()));

        final ProgressBar gauge = (ProgressBar) bikeStationDetails
                .findViewById(R.id.bike_station_gauge);
        gauge.setMax(upToDateStation.getAvailableBikes() + upToDateStation.getAvailableSlots());
        gauge.setIndeterminate(false);
        gauge.setProgress(upToDateStation.getAvailableBikes());
        gauge.setSecondaryProgress(upToDateStation.getAvailableBikes()
                + upToDateStation.getAvailableSlots());

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
