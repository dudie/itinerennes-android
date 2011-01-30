package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.widget.ToggleButton;

import fr.itinerennes.R;
import fr.itinerennes.model.BikeStation;
import fr.itinerennes.model.BusStation;
import fr.itinerennes.model.SubwayStation;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.adapter.BikeStationBoxAdapter;
import fr.itinerennes.ui.adapter.BusStationBoxAdapter;
import fr.itinerennes.ui.adapter.MarkerItemizedOverlayAdapter;
import fr.itinerennes.ui.adapter.SubwayStationBoxAdapter;
import fr.itinerennes.ui.views.ITRMapView;
import fr.itinerennes.ui.views.MapBoxView;

/**
 * An helper class to get the map overlays.
 * 
 * @author Jérémie Huchet
 */
public class MapOverlayHelper implements OverlayConstants {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(MapOverlayHelper.class);

    /** The itinerennes application context. */
    private final ITRContext context;

    /** The map where overlays must be managed. */
    private final ITRMapView map;

    /** Byte mask describing the current state of overlays. */
    private final int current = 0;

    /** The overlay with items focusable in the map box. */
    private GroupSelectOverlay<SelectableOverlay<?>> groupFocus;

    /** The bus stations focusable overlay. */
    private SelectableItemizedOverlay<SelectableMarker<BusStation>, BusStation> busStationOverlay;

    /** The bike stations focusable overlay. */
    private SelectableItemizedOverlay<SelectableMarker<BikeStation>, BikeStation> bikeStationOverlay;

    /** The subway stations focusable overlay. */
    private SelectableItemizedOverlay<SelectableMarker<SubwayStation>, SubwayStation> subwayStationOverlay;

    /** The my location overlay. */
    private LocationOverlay locationOverlay;

    /**
     * Creates the map overlay helper.
     * 
     * @param map
     *            the map view of which overlays must be managed
     * @param context
     *            the itinerennes application context
     */
    public MapOverlayHelper(final ITRContext context, final ITRMapView map) {

        this.context = context;
        this.map = map;

    }

    /**
     * Gets the target view of overlays displaying details when they are selected.
     * 
     * @return the map box view
     */
    private final MapBoxView getMapBoxView() {

        return (MapBoxView) context.findViewById(R.id.map_box);
    }

    /**
     * Gets the group focus overlay.
     * 
     * @return the group focus overlay
     */
    private GroupSelectOverlay<SelectableOverlay<?>> getGroupSelectOverlay() {

        if (null == groupFocus) {
            groupFocus = new GroupSelectOverlay<SelectableOverlay<?>>(context);
            map.getOverlays().add(groupFocus);
        }
        return groupFocus;
    }

    /**
     * Gets the bus station focusable overlay. Instantiate it if necessary.
     * 
     * @return the bus station focusable overlay
     */
    public final synchronized SelectableItemizedOverlay<SelectableMarker<BusStation>, BusStation> getBusStationOverlay() {

        if (null == busStationOverlay) {
            final MarkerItemizedOverlayAdapter<BusStation> busItemAdapter = new MarkerItemizedOverlayAdapter<BusStation>(
                    context, context.getBusService());
            final BusStationBoxAdapter busDisplayAdaper = new BusStationBoxAdapter(context);

            busStationOverlay = new SelectableItemizedOverlay<SelectableMarker<BusStation>, BusStation>(
                    context, map, busItemAdapter, busDisplayAdaper, getMapBoxView());

            busStationOverlay.setLocalizedName(context.getString(R.string.overlay_bus));
            busStationOverlay.setEnabled(false);
            getGroupSelectOverlay().addOverlay(busStationOverlay);

            final BookmarkItemizedOverlay<BusStation> busBookmarksOverlay = new BookmarkItemizedOverlay<BusStation>(
                    context, map);
            map.getOverlays().add(busBookmarksOverlay);
            busStationOverlay.setOnItemizedOverlayUpdateListener(busBookmarksOverlay);
        }

        return busStationOverlay;
    }

    /**
     * Gets the bike station focusable overlay. Instantiate it if necessary.
     * 
     * @return the bike station focusable overlay
     */
    public final synchronized SelectableItemizedOverlay<SelectableMarker<BikeStation>, BikeStation> getBikeStationOverlay() {

        if (null == bikeStationOverlay) {
            final MarkerItemizedOverlayAdapter<BikeStation> bikeItemAdapter = new MarkerItemizedOverlayAdapter<BikeStation>(
                    context, context.getBikeService());
            final BikeStationBoxAdapter bikeDisplayAdaper = new BikeStationBoxAdapter(context);

            bikeStationOverlay = new SelectableItemizedOverlay<SelectableMarker<BikeStation>, BikeStation>(
                    context, map, bikeItemAdapter, bikeDisplayAdaper, getMapBoxView());

            bikeStationOverlay.setLocalizedName(context.getString(R.string.overlay_bike));
            bikeStationOverlay.setEnabled(false);
            getGroupSelectOverlay().addOverlay(bikeStationOverlay);

            final BookmarkItemizedOverlay<BikeStation> bikeBookmarksOverlay = new BookmarkItemizedOverlay<BikeStation>(
                    context, map);
            map.getOverlays().add(bikeBookmarksOverlay);
            bikeStationOverlay.setOnItemizedOverlayUpdateListener(bikeBookmarksOverlay);
        }

        return bikeStationOverlay;
    }

    /**
     * Gets the subway station focusable overlay. Instantiate it if necessary.
     * 
     * @return the subway station focusable overlay
     */
    public final synchronized SelectableItemizedOverlay<SelectableMarker<SubwayStation>, SubwayStation> getSubwayStationOverlay() {

        if (null == subwayStationOverlay) {
            final MarkerItemizedOverlayAdapter<SubwayStation> subwayItemAdapter = new MarkerItemizedOverlayAdapter<SubwayStation>(
                    context, context.getSubwayService());
            final SubwayStationBoxAdapter subwayDisplayAdaper = new SubwayStationBoxAdapter(context);

            subwayStationOverlay = new SelectableItemizedOverlay<SelectableMarker<SubwayStation>, SubwayStation>(
                    context, map, subwayItemAdapter, subwayDisplayAdaper, getMapBoxView());

            subwayStationOverlay.setLocalizedName(context.getString(R.string.overlay_subway));
            subwayStationOverlay.setEnabled(false);
            getGroupSelectOverlay().addOverlay(subwayStationOverlay);

            final BookmarkItemizedOverlay<SubwayStation> subwayBookmarksOverlay = new BookmarkItemizedOverlay<SubwayStation>(
                    context, map);
            map.getOverlays().add(subwayBookmarksOverlay);
            subwayStationOverlay.setOnItemizedOverlayUpdateListener(subwayBookmarksOverlay);
        }

        return subwayStationOverlay;
    }

    /**
     * Gets the subway station focusable overlay. Instantiate it if necessary.
     * 
     * @return the location overlay
     */
    public final synchronized LocationOverlay getLocationOverlay() {

        if (null == locationOverlay) {
            locationOverlay = new LocationOverlay(context, map,
                    (ToggleButton) context.findViewById(R.id.mylocation_button));

            map.getOverlays().add(locationOverlay);
        }

        return locationOverlay;
    }

    /**
     * Gets the list of all overlays that can be shown or hidden.
     * 
     * @return a list of all overlays that can be shown or hidden
     */
    public final List<SelectableOverlay<?>> getToggleableOVerlays() {

        final List<SelectableOverlay<?>> result = new ArrayList<SelectableOverlay<?>>();
        result.addAll(getGroupSelectOverlay().getOverlays());
        if (!result.contains(getBusStationOverlay())) {
            result.add(getBusStationOverlay());
        }
        if (!result.contains(getBikeStationOverlay())) {
            result.add(getBikeStationOverlay());
        }
        if (!result.contains(getSubwayStationOverlay())) {
            result.add(getSubwayStationOverlay());
        }
        return result;
    }
}
