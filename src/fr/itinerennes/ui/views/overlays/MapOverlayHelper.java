package fr.itinerennes.ui.views.overlays;

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
import fr.itinerennes.ui.adapter.StationItemizedOverlayAdapter;
import fr.itinerennes.ui.adapter.SubwayStationBoxAdapter;
import fr.itinerennes.ui.views.MapBoxView;
import fr.itinerennes.ui.views.MapView;

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
    private final MapView map;

    /** Byte mask describing the current state of overlays. */
    private final int current = 0;

    /** The overlay with items focusable in the map box. */
    private GroupFocusOverlay<FocusableOverlay<?>> groupFocus;

    /** The bus stations focusable overlay. */
    private FocusableItemizedOverlay<FocusableOverlayItem<BusStation>, BusStation> busStationOverlay;

    /** The bike stations focusable overlay. */
    private FocusableItemizedOverlay<FocusableOverlayItem<BikeStation>, BikeStation> bikeStationOverlay;

    /** The subway stations focusable overlay. */
    private FocusableItemizedOverlay<FocusableOverlayItem<SubwayStation>, SubwayStation> subwayStationOverlay;

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
    public MapOverlayHelper(final ITRContext context, final MapView map) {

        this.context = context;
        this.map = map;

    }

    public void show(final int mask) {

        if (this.groupFocus == null) {
            this.groupFocus = new GroupFocusOverlay<FocusableOverlay<?>>(context,
                    (MapBoxView) context.findViewById(R.id.map_box));
            this.map.getOverlays().add(groupFocus);
        }

        if (match(BUS_STATIONS, mask) && !match(BUS_STATIONS, current)) {
            groupFocus.addOverlay(getBusStationOverlay());
            map.getListeners().add(getBusStationOverlay());
        }
        if (match(BIKE_STATIONS, mask) && !match(BIKE_STATIONS, current)) {
            groupFocus.addOverlay(getBikeStationOverlay());
            map.getListeners().add(getBikeStationOverlay());
        }
        if (match(SUBWAY_STATIONS, mask) && !match(SUBWAY_STATIONS, current)) {
            groupFocus.addOverlay(getSubwayStationOverlay());
            map.getListeners().add(getSubwayStationOverlay());
        }
        if (match(LOCATION, mask) && !match(LOCATION, current)) {
            this.map.getOverlays().add(getLocationOverlay());
        }
    }

    public void hide(final int mask) {

        if (match(BUS_STATIONS, mask) && !match(BUS_STATIONS, current)) {
            groupFocus.removeOverlay(getBusStationOverlay());
            map.getListeners().remove(getBusStationOverlay());
        }
        if (match(BIKE_STATIONS, mask) && match(BIKE_STATIONS, current)) {
            groupFocus.removeOverlay(getBikeStationOverlay());
            map.getListeners().remove(getBikeStationOverlay());
        }
        if (match(SUBWAY_STATIONS, mask) && !match(SUBWAY_STATIONS, current)) {
            groupFocus.removeOverlay(getSubwayStationOverlay());
            map.getListeners().remove(getSubwayStationOverlay());
        }
    }

    private boolean match(final int value, final int mask) {

        return (value & mask) == value;
    }

    /**
     * Gets the bus station focusable overlay. Instantiate it if necessary.
     * 
     * @return the bus station focusable overlay
     */
    public final synchronized FocusableItemizedOverlay<FocusableOverlayItem<BusStation>, BusStation> getBusStationOverlay() {

        if (null == busStationOverlay) {
            final StationItemizedOverlayAdapter<BusStation> busItemAdapter = new StationItemizedOverlayAdapter<BusStation>(
                    context, context.getBusService());
            final BusStationBoxAdapter busDisplayAdaper = new BusStationBoxAdapter(
                    context.getBusService(), context.getBusRouteService(),
                    context.getLineIconService());

            busStationOverlay = new FocusableItemizedOverlay<FocusableOverlayItem<BusStation>, BusStation>(
                    context, busItemAdapter, busDisplayAdaper);
        }

        return busStationOverlay;
    }

    /**
     * Gets the bike station focusable overlay. Instantiate it if necessary.
     * 
     * @return the bike station focusable overlay
     */
    public final synchronized FocusableItemizedOverlay<FocusableOverlayItem<BikeStation>, BikeStation> getBikeStationOverlay() {

        if (null == bikeStationOverlay) {
            final StationItemizedOverlayAdapter<BikeStation> bikeItemAdapter = new StationItemizedOverlayAdapter<BikeStation>(
                    context, context.getBikeService());
            final BikeStationBoxAdapter bikeDisplayAdaper = new BikeStationBoxAdapter(
                    context.getBikeService());

            bikeStationOverlay = new FocusableItemizedOverlay<FocusableOverlayItem<BikeStation>, BikeStation>(
                    context, bikeItemAdapter, bikeDisplayAdaper);
        }

        return bikeStationOverlay;
    }

    /**
     * Gets the subway station focusable overlay. Instantiate it if necessary.
     * 
     * @return the subway station focusable overlay
     */
    public final synchronized FocusableItemizedOverlay<FocusableOverlayItem<SubwayStation>, SubwayStation> getSubwayStationOverlay() {

        if (null == subwayStationOverlay) {
            final StationItemizedOverlayAdapter<SubwayStation> subwayItemAdapter = new StationItemizedOverlayAdapter<SubwayStation>(
                    context, context.getSubwayService());
            final SubwayStationBoxAdapter subwayDisplayAdaper = new SubwayStationBoxAdapter();

            subwayStationOverlay = new FocusableItemizedOverlay<FocusableOverlayItem<SubwayStation>, SubwayStation>(
                    context, subwayItemAdapter, subwayDisplayAdaper);
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
        }

        return locationOverlay;
    }
}
