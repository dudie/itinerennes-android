package fr.itinerennes.business.service;

import java.util.List;

import android.test.AndroidTestCase;

import fr.itinerennes.ItineRennesConstants;
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

        List<BikeStation> stations = null;
        try {
            stations = keolisService.getAllBikeStations();
        } catch (final GenericException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull("no bike stations returned by the api", stations);
        assertTrue("at least one bike station should be returned by the api", stations.size() > 0);
        assertEquals("on November, 27th 2010, the keolis API returns 83 bike stations", 83,
                stations.size());
    }

    /**
     * Test method for {@link KeolisService#getBikeStationsNearFrom(double, double)} .
     */
    public void testGetBikeStationsNearFrom() {

        List<BikeStation> stations = null;
        try {
            stations = keolisService.getBikeStationsNearFrom(
                    ItineRennesConstants.CONFIG_RENNES_LAT, ItineRennesConstants.CONFIG_RENNES_LON);
        } catch (final GenericException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull("no bike stations returned by the api", stations);
        assertEquals("3 bike stations should be returned by the api", stations.size(), 3);
    }

    /**
     * Test method for {@link KeolisService#getBikeStation(String)} .
     */
    public void testGetBikeStation() {

        BikeStation station = null;
        try {
            station = keolisService.getBikeStation(String.valueOf(53));
        } catch (final GenericException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull("no bike station returned by the api", station);
        assertEquals(String.valueOf(53), station.getId());
        assertEquals(48.12153, station.getLatitude());
        assertEquals(-1.711088, station.getLongitude());
        assertEquals("JF KENNEDY", station.getName());
        assertEquals("DALLE KENNEDY", station.getAddress());
        assertEquals("Villejean-Beauregard", station.getDistrict());
        assertEquals(Station.TYPE_VELO, station.getType());
    }

    /**
     * Test method for {@link KeolisService#getSubwayStationsNearFrom(double, double)} .
     */
    public void testGetSubwayStationsNearFrom() {

        List<SubwayStation> stations = null;
        try {
            stations = keolisService.getSubwayStationsNearFrom(
                    ItineRennesConstants.CONFIG_RENNES_LAT, ItineRennesConstants.CONFIG_RENNES_LON);
        } catch (final GenericException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull("no subway stations returned by the api", stations);
        assertEquals("3 subway stations should be returned by the api", stations.size(), 3);
    }

    /**
     * Test method for {@link KeolisService#getAllSubwayStations()}.
     */
    public void testGetSubwayStations() {

        List<SubwayStation> stations = null;
        try {
            stations = keolisService.getAllSubwayStations();
        } catch (final GenericException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull("no subway stations returned by the api", stations);
        assertTrue("at least one subway station should be returned by the api", stations.size() > 0);
        assertEquals("on November, 27th 2010, the keolis API returns 15 subway stations", 15,
                stations.size());
    }

    /**
     * Test method for {@link KeolisService#getSubwayStation(String)} .
     */
    public void testGetSubwayStation() {

        SubwayStation station = null;
        try {
            station = keolisService.getSubwayStation("CDG");
        } catch (final GenericException e) {
            e.printStackTrace();
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
    }
}
