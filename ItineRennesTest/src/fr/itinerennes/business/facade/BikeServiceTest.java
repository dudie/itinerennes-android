package fr.itinerennes.business.facade;

import android.test.AndroidTestCase;

import fr.itinerennes.beans.BikeStation;
import fr.itinerennes.business.http.keolis.KeolisService;

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

        final BikeStation station = null;
        final long startUncached, endUncached, totalUncached;
        final long startCached, endCached, totalCached;

        startUncached = System.currentTimeMillis();
        // try {
        // station = BikeStationProvider.getStation(String.valueOf(53));
        // } catch (final GenericException e) {
        // e.printStackTrace();
        // fail(e.getMessage());
        // }
        endUncached = System.currentTimeMillis();
        totalUncached = endUncached - startUncached;

        assertNotNull("no bike station returned by the api", station);
        assertEquals(String.valueOf(53), station.getId());
        assertEquals(48.12153, station.getLatitude());
        assertEquals(-1.711088, station.getLongitude());
        assertEquals("JF KENNEDY", station.getName());
        assertEquals("DALLE KENNEDY", station.getAddress());
        assertEquals("Villejean-Beauregard", station.getDistrict());

        // check cache contains the station
        // assertTrue("the cache doesn't contains the retrieved station",
        // CacheProvider.contains(station.getClass(), station.getId()));

        // make a new call and assert it makes less time to retrieve data
        startCached = System.currentTimeMillis();
        // try {
        // station = BikeStationProvider.getStation(String.valueOf(53));
        // } catch (final GenericException e) {
        // e.printStackTrace();
        // fail(e.getMessage());
        // }
        endCached = System.currentTimeMillis();
        totalCached = endCached - startCached;
        assertTrue("cached call is faster than remote call", totalCached < totalUncached);
    }
}
