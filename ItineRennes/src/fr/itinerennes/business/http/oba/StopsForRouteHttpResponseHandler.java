package fr.itinerennes.business.http.oba;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusStation;

/**
 * Handles responses for a call to the "stops-for-routes" method of the OneBusAway API.
 * 
 * @author Jérémie Huchet
 */
public class StopsForRouteHttpResponseHandler extends HttpResponseHandler {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(StopsForRouteHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected Object handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final List<BusStation> stations = new ArrayList<BusStation>();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }
        return stations;
    }
}
