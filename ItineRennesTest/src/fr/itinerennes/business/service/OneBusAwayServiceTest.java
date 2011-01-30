package fr.itinerennes.business.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.business.http.oba.OneBusAwayService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.oba.ArrivalAndDeparture;
import fr.itinerennes.model.oba.ScheduleStopTime;
import fr.itinerennes.model.oba.StopSchedule;
import fr.itinerennes.model.oba.TripSchedule;

/**
 * Test class for {@link OneBusAwayService}.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class OneBusAwayServiceTest extends AndroidTestCase {

    /** The event logger. */
    private static Logger LOGGER = LoggerFactory.getLogger(OneBusAwayServiceTest.class);

    /** The one bus away service. */
    private final OneBusAwayService obaService = new OneBusAwayService();

    /**
     * Test method for {@link OneBusAwayService#getTripDetails(String)}.
     */
    public final void testGetTripDetails() {

        LOGGER.info("testGetTripDetails.start");

        TripSchedule schedule = null;
        try {
            schedule = obaService.getTripDetails("10000");
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        }

        assertNotNull("no schedule returned by the api", schedule);
        assertEquals("33 stop times should be returned by the api", 33, schedule.getStopTimes()
                .size());
        assertEquals("the first stop of this trip should be Grand Quartier", "Grand Quartier",
                schedule.getStopTimes().get(0).getStop().getName());

        LOGGER.info("testGetTripDetails.end");
    }

    /**
     * Test method for {@link OneBusAwayService#getArrivalsAndDeparturesForStop(String)}.
     */
    public final void testGetArrivalsAndDeparturesForStop() {

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

    /**
     * Test method for {@link OneBusAwayService#getScheduleForStop(String)}.
     */
    public final void testGetScheduleForStop() {

        LOGGER.info("testGetScheduleForStop.start");

        StopSchedule schedule = null;
        try {
            final Calendar calendar = Calendar.getInstance();
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setTime(sdf.parse("2011-01-27"));
            schedule = obaService.getScheduleForStop("1_repbottb", calendar.getTime());
        } catch (final GenericException e) {
            LOGGER.error("GenericException", e);
            fail(e.getMessage());
        } catch (final ParseException e) {
            LOGGER.error("The date is not in a valid format.", e);
            fail(e.getMessage());
        }

        assertNotNull("no schedule returned by the api", schedule);

        assertEquals("143 stop times should be returned by the api", 143, schedule.getStopTimes()
                .size());
        assertEquals("the stop id should be 1_repbottb", "1_repbottb", schedule.getStop().getId());
        assertEquals("the stop name should be République Pré Botté", "République Pré Botté",
                schedule.getStop().getName());
        assertEquals("5 lines should be return for this stop", 5, schedule.getStop().getRoutes()
                .size());

        int cpt = 0;
        for (final ScheduleStopTime stopTime : schedule.getStopTimes()) {
            if (stopTime.getRoute().getShortName().equals("50")) {
                cpt++;
            }
        }
        assertEquals("65 stop times should be returned by the api for line 50", 65, cpt);

        LOGGER.info("testGetScheduleForStop.end");
    }
}
