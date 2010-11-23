package fr.itinerennes.business.facade;

import android.test.AndroidTestCase;

import fr.itinerennes.beans.BikeStation;
import fr.itinerennes.business.RemoteDataCacheProvider;
import fr.itinerennes.business.facade.BikeService;
import fr.itinerennes.business.service.KeolisService;
import fr.itinerennes.exceptions.GenericException;

/**
 * Test class for {@link KeolisService}.
 * 
 * @author Jérémie Huchet
 */
public class BikeServiceTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test method for {@link BikeService#getStation(int)}.
     */
    public void testGetStation() {

        BikeStation station = null;
        try {
            station = BikeService.getStation(53);
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

        // check cache contains the station
        assertTrue("the cache doesn't contains the retrieved station",
                RemoteDataCacheProvider.contains(station.getClass(), station.getId()));
    }
}
