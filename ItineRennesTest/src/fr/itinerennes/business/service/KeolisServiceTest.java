package fr.itinerennes.business.service;

import java.util.List;

import android.test.AndroidTestCase;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.beans.BikeStation;
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
            fail(e.getMessage());
        }
        assertNotNull("no bike stations returned by the api", stations);
        assertTrue("at least on station should be returned by the api", stations.size() > 0);
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
            fail(e.getMessage());
        }
        assertNotNull("no bike stations returned by the api", stations);
        assertTrue("3 stations should be returned by the api", stations.size() == 3);
    }

    /**
     * Test method for {@link KeolisService#getBikeStation(int)} .
     */
    public void testGetBikeStation() {

        BikeStation station = null;
        try {
            station = keolisService.getBikeStation(53);
        } catch (final GenericException e) {
            fail(e.getMessage());
        }
        assertNotNull("no bike station returned by the api", station);
        assertEquals(53, station.getId());
        assertEquals(48.12153, station.getLatitude());
        assertEquals(-1.711088, station.getLongitude());
        assertEquals("JF KENNEDY", station.getName());
        assertEquals("DALLE KENNEDY", station.getAddress());
        assertEquals("Villejean-Beauregard", station.getDistrict());
    }
}
