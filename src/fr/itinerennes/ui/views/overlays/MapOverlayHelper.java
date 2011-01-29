package fr.itinerennes.ui.views.overlays;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.graphics.Color;
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

    public void show(final int mask) {

        if (this.groupFocus == null) {
            this.groupFocus = new GroupSelectOverlay<SelectableOverlay<?>>(context);
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
        if (match(PATH, mask) && !match(PATH, current)) {
            final PolylineOverlay path = new PolylineOverlay(Color.RED, context);
            path.setPolyline("sjtdHzkfIzJzOfT~M");
            this.map.getOverlays().add(path);
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
     * Gets the target view of overlays displaying details when they are selected.
     * 
     * @return the map box view
     */
    private MapBoxView getMapBoxView() {

        return (MapBoxView) context.findViewById(R.id.map_box);
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
                    context, busItemAdapter, busDisplayAdaper, getMapBoxView());
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
                    context, bikeItemAdapter, bikeDisplayAdaper, getMapBoxView());
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
                    context, subwayItemAdapter, subwayDisplayAdaper, getMapBoxView());
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
