package fr.itinerennes.business.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.business.http.oba.OneBusAwayService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusStation;

/**
 * Test class for {@link OneBusAwayService}.
 * 
 * @author Jérémie Huchet
 */
public class OneBusAwayServiceTest extends AndroidTestCase {

    /** The event logger. */
    private static Logger LOGGER = LoggerFactory.getLogger(OneBusAwayServiceTest.class);

    /** The one bus away service. */
    private final OneBusAwayService obaService = new OneBusAwayService();

    /**
     * Test method for {@link OneBusAwayService#getStopsForRoute(String)}.
     */
    public void testGetBikeStations() {

        LOGGER.info("testGetBikeStations.start");

        List<BusStation> stations = null;
        try {
            stations = obaService.getStopsForRoute("1_1");
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }

        LOGGER.info("testGetBikeStations.end");
    }
}
