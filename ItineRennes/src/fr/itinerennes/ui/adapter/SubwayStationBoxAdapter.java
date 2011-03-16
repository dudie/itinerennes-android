package fr.itinerennes.ui.adapter;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.model.SubwayStation;
import fr.itinerennes.ui.activity.ItinerennesContext;
import fr.itinerennes.ui.views.event.ToggleStarListener;
import fr.itinerennes.ui.views.overlays.old.SelectableMarker;

/**
 * @author Jérémie Huchet
 */
public class SubwayStationBoxAdapter implements MapBoxAdapter<SelectableMarker<SubwayStation>> {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(SubwayStationBoxAdapter.class);

    /** The itinerennes context. */
    private final ItinerennesContext context;

    /** The layout inflater. */
    private final LayoutInflater inflater;

    /**
     * Creates the subway station adapter for map box.
     * 
     * @param context
     *            the context
     */
    public SubwayStationBoxAdapter(final ItinerennesContext context) {

        this.context = context;
        inflater = context.getLayoutInflater();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#getView(java.lang.Object)
     */
    @Override
    public final View getView(final SelectableMarker<SubwayStation> item) {

        final View subwayView = inflater.inflate(R.layout.map_box_subway, null);
        ((TextView) subwayView.findViewById(R.id.map_box_title)).setText(item.getData().getName());

        final ToggleButton star = (ToggleButton) subwayView
                .findViewById(R.id.map_box_toggle_bookmark);
        star.setChecked(context.getBookmarksService().isStarred(SubwayStation.class.getName(),
                item.getData().getId()));
        star.setOnCheckedChangeListener(new ToggleStarListener(context, SubwayStation.class
                .getName(), item.getData().getId(), item.getData().getName()));

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
    public final void doInBackground(final View view, final SelectableMarker<SubwayStation> item) {

        // nothing to load
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.adapter.MapBoxAdapter#updateView(android.view.View, java.lang.Object)
     */
    @Override
    public final void updateView(final View view, final SelectableMarker<SubwayStation> item) {

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
