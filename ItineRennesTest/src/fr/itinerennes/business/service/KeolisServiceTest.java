package fr.itinerennes.business.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.beans.BikeDistrict;
import fr.itinerennes.beans.BikeStation;
import fr.itinerennes.beans.Station;
import fr.itinerennes.beans.SubwayStation;
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
    private final KeolisService keolisService = KeolisService.getInstance();

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

    /**
     * Test method for {@link KeolisService#getAllBikeDistricts()}.
     */
    public void testGetAllBikeDistricts() {

        LOGGER.info("testGetAllBikeDistricts.start");

        List<BikeDistrict> districts = null;
        try {
            districts = keolisService.getAllBikeDistricts();
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }
        assertNotNull("no district returned", districts);
        assertEquals("on November, 27th 2010, the keolis API returns 12 bike districts", 12,
                districts.size());

        for (final BikeDistrict district : districts) {
            assertFalse(String.format("district [%s] has no id", district),
                    StringUtils.isEmpty(district.getId()));
            assertFalse(String.format("district [%s] has no name", district),
                    StringUtils.isEmpty(district.getName()));
        }

        LOGGER.info("testGetAllBikeDistricts.end");
    }
}
