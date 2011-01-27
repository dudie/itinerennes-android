package fr.itinerennes.business.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.business.http.oba.OneBusAwayService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.oba.ArrivalAndDeparture;
import fr.itinerennes.model.oba.Schedule;

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
     * Test method for {@link OneBusAwayService#getTripDetails(String)}.
     */
    public void testGetTripDetails() {

        LOGGER.info("testGetTripDetails.start");

        Schedule schedule = null;
        try {
            schedule = obaService.getTripDetails("1_10000");
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }

        assertNotNull("no schedule returned by the api", schedule);
        assertEquals("33 stop times should be returned by the api", schedule.getStopTimes().size(),
                33);
        assertEquals("the first stop of this trip should be Grand Quartier", schedule
                .getStopTimes().get(0).getStop().getName(), "Grand Quartier");

        LOGGER.info("testGetTripDetails.end");
    }
    
    /**
     * Test method for {@link OneBusAwayService#getArrivalsAndDeparturesForStop(String)}.
     */
    public void testGetArrivalsAndDeparturesForStop() {

        LOGGER.info("testGetArrivalsAndDeparturesForStop.start");

        List<ArrivalAndDeparture> arrivalsAndDepartures = null;
        try {
            arrivalsAndDepartures = obaService.getArrivalsAndDeparturesForStop("1_gdquart");
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }

        assertNotNull("no arrivals and departures returned by the api", arrivalsAndDepartures);
        

        LOGGER.info("testGetArrivalsAndDeparturesForStop.end");
    }
}
