package fr.itinerennes.business.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.beans.BikeStation;
import fr.itinerennes.beans.LineTransportIcon;
import fr.itinerennes.beans.Station;
import fr.itinerennes.beans.SubwayStation;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.exceptions.GenericException;

/**
 * Test class for {@link KeolisService}.
 * 
 * @author Jérémie Huchet
 */
public class KeolisServiceTest extends AndroidTestCase {

    /** The event logger. */
    private static Logger LOGGER = LoggerFactory.getLogger(KeolisServiceTest.class);

    /** The keolis service. */
    private final KeolisService keolisService = new KeolisService();

    @Override
    protected void setUp() throws Exception {

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test method for {@link KeolisService#getAllBikeStations()}.
     */
    public void testGetBikeStations() {

        LOGGER.info("testGetBikeStations.start");

        List<BikeStation> stations = null;
        try {
            stations = keolisService.getAllBikeStations();
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }

        assertNotNull("no bike stations returned by the api", stations);
        assertTrue("at least one bike station should be returned by the api", stations.size() > 0);
        assertEquals("on November, 27th 2010, the keolis API returns 83 bike stations", 83,
                stations.size());

        for (final BikeStation station : stations) {
            LOGGER.debug("checking {}", station);
            assertFalse(String.format("station [%s] has no id", station),
                    StringUtils.isEmpty(station.getId()));
            assertFalse(String.format("station [%s] has no name", station),
                    StringUtils.isEmpty(station.getName()));
            assertNotNull(String.format("station [%s] has no geoposition", station),
                    station.getGeoPoint());
            assertNotNull(String.format("station [%s] has latitude", station.getLatitude()),
                    station.getGeoPoint());
            assertNotNull(String.format("station [%s] has longitude", station.getLongitude()),
                    station.getGeoPoint());
        }

        LOGGER.info("testGetBikeStations.end");
    }

    /**
     * Test method for {@link KeolisService#getBikeStationsNearFrom(double, double)} .
     */
    public void testGetBikeStationsNearFrom() {

        LOGGER.info("testGetBikeStationsNearFrom.start");

        List<BikeStation> stations = null;
        try {
            stations = keolisService.getBikeStationsNearFrom(
                    ItineRennesConstants.CONFIG_RENNES_LAT, ItineRennesConstants.CONFIG_RENNES_LON);
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }
        assertNotNull("no bike stations returned by the api", stations);
        assertEquals("3 bike stations should be returned by the api", stations.size(), 3);

        LOGGER.info("testGetBikeStationsNearFrom.end");
    }

    /**
     * Test method for {@link KeolisService#getBikeStation(String)} .
     */
    public void testGetBikeStation() {

        LOGGER.info("testGetBikeStation.start");

        BikeStation station = null;
        try {
            station = keolisService.getBikeStation(String.valueOf(53));
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }
        assertNotNull("no bike station returned by the api", station);
        assertEquals(String.valueOf(53), station.getId());
        assertEquals(48.12153, station.getLatitude());
        assertEquals(-1.711088, station.getLongitude());
        assertEquals("JF KENNEDY", station.getName());
        assertEquals("DALLE KENNEDY", station.getAddress());
        assertEquals("Villejean-Beauregard", station.getDistrict());
        assertEquals(Station.TYPE_BIKE, station.getType());

        LOGGER.info("testGetBikeStation.end");
    }

    /**
     * Test method for {@link KeolisService#getAllLineIcons()}.
     */
    public void testGetAllLineIcons() {

        LOGGER.info("testGetAllLineIcons.start");

        List<LineTransportIcon> icons = null;
        try {
            icons = keolisService.getAllLineIcons();
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }
        assertNotNull("no icon returned", icons);
        assertEquals("on November, 29th 2010, the keolis API returns 61 bike districts", 61,
                icons.size());

        for (final LineTransportIcon icon : icons) {
            LOGGER.debug("checking {}", icon);
            assertFalse(String.format("icon [%s] is not associated to a line", icon),
                    StringUtils.isEmpty(icon.getLine()));
            assertFalse(String.format("icon [%s] has no url", icon),
                    StringUtils.isEmpty(icon.getIconUrl()));
            Object iconImage = null;
            try {
                final URL iconUrl = new URL(icon.getIconUrl());
                iconImage = iconUrl.getContent();
            } catch (final MalformedURLException e) {
                LOGGER.error("MalformedURLException", e);
                fail(String.format("url of icon [%s] is invalid", icon));
            } catch (final IOException e) {
                LOGGER.error("IOException", e);
                fail(String.format("unable to retrieve image for icon [%s]", icon));
            }
            assertNotNull("image retrieved can't be null", iconImage);
        }

        LOGGER.info("testGetAllLineIcons.end");
    }

    /**
     * Test method for {@link KeolisService#getSubwayStationsNearFrom(double, double)} .
     */
    public void testGetSubwayStationsNearFrom() {

        LOGGER.info("testGetSubwayStationsNearFrom.start");

        List<SubwayStation> stations = null;
        try {
            stations = keolisService.getSubwayStationsNearFrom(
                    ItineRennesConstants.CONFIG_RENNES_LAT, ItineRennesConstants.CONFIG_RENNES_LON);
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }
        assertNotNull("no subway stations returned by the api", stations);
        assertEquals("3 subway stations should be returned by the api", stations.size(), 3);

        LOGGER.info("testGetSubwayStationsNearFrom.end");
    }

    /**
     * Test method for {@link KeolisService#getAllSubwayStations()}.
     */
    public void testAllGetSubwayStations() {

        LOGGER.info("testAllGetSubwayStations.start");

        List<SubwayStation> stations = null;
        try {
            stations = keolisService.getAllSubwayStations();
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }
        assertNotNull("no subway stations returned by the api", stations);
        assertTrue("at least one subway station should be returned by the api", stations.size() > 0);
        assertEquals("on November, 27th 2010, the keolis API returns 15 subway stations", 15,
                stations.size());

        for (final SubwayStation station : stations) {
            LOGGER.debug("checking {}", station);
            assertFalse(String.format("station [%s] has no id", station),
                    StringUtils.isEmpty(station.getId()));
            assertFalse(String.format("station [%s] has no name", station),
                    StringUtils.isEmpty(station.getName()));
            assertNotNull(String.format("station [%s] has no geoposition", station),
                    station.getGeoPoint());
            assertNotNull(String.format("station [%s] has latitude", station.getLatitude()),
                    station.getGeoPoint());
            assertNotNull(String.format("station [%s] has longitude", station.getLongitude()),
                    station.getGeoPoint());
        }

        LOGGER.info("testAllGetSubwayStations.end");
    }

    /**
     * Test method for {@link KeolisService#getSubwayStation(String)} .
     */
    public void testGetSubwayStation() {

        LOGGER.info("testGetSubwayStation.start");

        SubwayStation station = null;
        try {
            station = keolisService.getSubwayStation("CDG");
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }
        assertNotNull("no subway station returned by the api", station);
        assertEquals("CDG", station.getId());
        assertEquals(48.10556000, station.getLatitude());
        assertEquals(-1.676990000, station.getLongitude());
        assertEquals("Charles de Gaulle", station.getName());
        assertEquals(-3, station.getFloors());
        assertEquals(9, station.getRankingPlatformDirection1());
        assertEquals(21, station.getRankingPlatformDirection2());
        assertEquals(true, station.isHasPlatformDirection1());
        assertEquals(true, station.isHasPlatformDirection2());
        assertEquals(Station.TYPE_SUBWAY, station.getType());

        LOGGER.info("testGetSubwayStation.end");
    }
}
