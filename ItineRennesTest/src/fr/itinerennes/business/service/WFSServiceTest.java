package fr.itinerennes.business.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.business.http.wfs.WFSService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusStation;

/**
 * Test class for {@link WFSService}.
 * 
 * @author Olivier Boudet
 */
public class WFSServiceTest extends AndroidTestCase {

    /** The event logger. */
    private static Logger LOGGER = LoggerFactory.getLogger(KeolisServiceTest.class);

    /** The tested WFS service. */
    private final WFSService wfsService = new WFSService();

    @Override
    protected void setUp() throws Exception {

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test method for {@link WFSService#getBusStationsFromBbox(fr.itinerennes.model.BoundingBox)}.
     */
    public final void testGetBusStationsFromBbox() {

        LOGGER.info("testGetBusStations.start");

        /*
         * Test with a bounding box containing any stations
         */
        List<BusStation> stations = null;

        try {
            stations = wfsService.getBusStationsFromBbox(new BoundingBoxE6(49D, 2D, 47D, -2D), 10);
        } catch (final GenericException e) {
            fail(e.getMessage());
        }

        assertNotNull("no bus stations returned by the api", stations);
        assertEquals("10 stations should be returned by the api", 7, stations.size());

        for (final BusStation station : stations) {
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

        /*
         * Test with a bounding box which does not contain station
         */
        try {
            stations = wfsService.getBusStationsFromBbox(new BoundingBoxE6(0, 0, 0, 0));
        } catch (final GenericException e) {
            fail(e.getMessage());
        }
        assertNotNull("stations is null", stations);
        assertTrue("no station should be returned by the api", stations.size() == 0);

        LOGGER.info("testGetBusStations.end");
    }

    /**
     * Test method for {@link WFSService#getBusStation(String)}.
     */
    public final void testGetBusStation() {

        BusStation station = null;
        try {
            station = wfsService.getBusStation("avenir1");
        } catch (final GenericException e) {
            fail(e.getMessage());
        }

        assertNotNull("a bus station should be returned by the api", station);
        assertEquals("avenir1", station.getId());
        assertEquals("Avenir", station.getName());
        assertEquals(48054016, station.getLatitude());
        assertEquals(-1783664, station.getLongitude());
    }
}
