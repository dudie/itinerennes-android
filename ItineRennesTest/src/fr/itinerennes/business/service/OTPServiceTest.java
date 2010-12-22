package fr.itinerennes.business.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.business.http.otp.OTPService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusRoute;

/**
 * Test class for {@link OTPService}.
 * 
 * @author Olivier Boudet
 */
public class OTPServiceTest extends AndroidTestCase {

    /** The event logger. */
    private static Logger LOGGER = LoggerFactory.getLogger(OTPServiceTest.class);

    /** The tested OTP service. */
    private final OTPService otpService = new OTPService();

    @Override
    protected void setUp() throws Exception {

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {

        super.tearDown();
    }

    /**
     * Test method for {@link OTPService#getStopRoutes(String)}.
     */
    public final void testGetStopRoutes() {

        LOGGER.info("testGetStopRoutes.start");

        /*
         * Test with a correct station with many routes.
         */
        List<BusRoute> routes = null;

        try {
            routes = otpService.getStopRoutes("repbotta");
        } catch (final GenericException e) {
            fail(e.getMessage());
        }

        assertNotNull("no bus routes returned by the api", routes);
        assertEquals("4 routes should be returned by the api", 4, routes.size());

        for (final BusRoute route : routes) {
            LOGGER.debug("checking {}", route);
            assertFalse(String.format("station [%s] has no id", route),
                    StringUtils.isEmpty(route.getId()));
            assertFalse(String.format("station [%s] has no short name", route),
                    StringUtils.isEmpty(route.getShortName()));
            assertNotNull(String.format("station [%s] has no long name", route),
                    route.getLongName());
        }

        /*
         * Test with a correct station with only one routes.
         */
        routes = null;

        try {
            routes = otpService.getStopRoutes("mairieb");
        } catch (final GenericException e) {
            fail(e.getMessage());
        }

        assertNotNull("no bus routes returned by the api", routes);
        assertEquals("1 route should be returned by the api", 1, routes.size());

        for (final BusRoute route : routes) {
            LOGGER.debug("checking {}", route);
            assertFalse(String.format("station [%s] has no id", route),
                    StringUtils.isEmpty(route.getId()));
            assertFalse(String.format("station [%s] has no short name", route),
                    StringUtils.isEmpty(route.getShortName()));
            assertNotNull(String.format("station [%s] has no long name", route),
                    route.getLongName());
        }

        /*
         * Test with an unknown station.
         */
        routes = null;

        try {
            routes = otpService.getStopRoutes("republique");
        } catch (final GenericException e) {
            fail(e.getMessage());
        }

        assertNull("at least one bus route returned by the api. 0 expected.", routes);

        LOGGER.info("testGetStopRoutes.end");
    }

}
