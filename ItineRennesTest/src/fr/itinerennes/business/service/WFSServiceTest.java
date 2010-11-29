package fr.itinerennes.business.service;

import java.util.List;

import android.test.AndroidTestCase;
import fr.itinerennes.beans.BoundingBox;
import fr.itinerennes.beans.BusStation;
import fr.itinerennes.business.http.wms.WFSJsonService;
import fr.itinerennes.exceptions.GenericException;

/**
 * Test class for {@link WFSJsonService}.
 * 
 * @author Olivier Boudet
 */
public class WFSServiceTest extends AndroidTestCase {

    /**
     * 
     */
    private WFSService wfsService = WFSService.getInstance();

    @Override
    protected void setUp() throws Exception {

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test method for {@link WFSService#getBusStationsFromBbox(fr.itinerennes.beans.BoundingBox)}.
     */
    public void testGetBusStationsFromBbox() {

        /*
         * Test with a bounding box containing any stations
         */
        List<BusStation> stations = null;

        try {
            stations = wfsService.getBusStationsFromBbox(new BoundingBox(-2, 2, 47, 49), 10);
        } catch (final GenericException e) {
            fail(e.getMessage());
        }

        assertNotNull("no bus stations returned by the api", stations);
        assertTrue("10 stations should be returned by the api", stations.size() == 10);

        /*
         * Test with a bounding box which does not contain station
         */
        try {
            stations = wfsService.getBusStationsFromBbox(new BoundingBox(0, 0, 0, 0));
        } catch (final GenericException e) {
            fail(e.getMessage());
        }
        assertNotNull("stations is null", stations);
        assertTrue("no station should be returned by the api", stations.size() == 0);
    }

    /**
     * Test method for {@link WFSService#getBusStation(String)}.
     */
    public void testGetBusStation() {

        BusStation station = null;
        try {
            station = wfsService.getBusStation("stops.avenir1");
        } catch (GenericException e) {
            fail(e.getMessage());
        }

        assertNotNull("a bus station should be returned by the api", station);
        assertEquals("stops.avenir1", station.getId());
        assertEquals("Avenir", station.getName());
        assertEquals(48.054016051, station.getLatitude());
        assertEquals(-1.783664796, station.getLongitude());
    }
}
