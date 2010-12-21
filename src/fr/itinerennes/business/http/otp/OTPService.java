package fr.itinerennes.business.http.otp;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.http.GenericHttpService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusDeparture;
import fr.itinerennes.model.BusRoute;

/**
 * Manage calls to the OTP API.
 * 
 * @author Olivier Boudet
 */
public class OTPService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(OTPService.class);

    /** The HTTP client. */
    private final GenericHttpService httpService = new GenericHttpService();

    /** The handler to receive stop informations. */
    private final StopRoutesHttpResponseHandler stopHandler = new StopRoutesHttpResponseHandler();

    /** The handler to receive departures stations. */
    private final DeparturesHttpResponseHandler departuresHandler = new DeparturesHttpResponseHandler();

    /**
     * Creates a generic request to the OpenTripPlanner Extended API. This method will set the
     * request headers.
     * 
     * @param parameters
     *            the request parameters
     * @return an {@link HttpPost} to send to execute the request
     * @throws GenericException
     *             unable to encode request parameters
     */
    private HttpGet createOTPRequest(final String path, final List<BasicNameValuePair> parameters)
            throws GenericException {

        final StringBuffer sb = new StringBuffer();
        sb.append(ItineRennesConstants.OTP_API_URL);
        sb.append(path);
        sb.append("?");

        for (final NameValuePair param : parameters) {
            sb.append(param.getName());
            sb.append("=");
            sb.append(param.getValue());
            sb.append("&");
        }

        final HttpGet req = new HttpGet();

        req.addHeader("Accept", "text/json");
        req.addHeader("Accept", "application/json");

        try {
            req.setURI(new URI(sb.toString()));
        } catch (final URISyntaxException e) {
            throw new GenericException(ErrorCodeConstants.API_CALL_FAILED,
                    "unable to encode build request uri", e);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(req.getURI().toString());
        }

        return req;
    }

    /**
     * Makes a call to the OTP Extended API to get stop routes informations.
     * 
     * @param id
     *            id of station
     * @throws GenericException
     *             unable to encode request parameters
     * @return a list of {@link BusRoute} available at this stop.
     */
    public final List<BusRoute> getStopRoutes(final String id) throws GenericException {

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(1);

        params.add(new BasicNameValuePair("id", id));

        final List<BusRoute> data = httpService.execute(
                createOTPRequest(ItineRennesConstants.OTP_API_STOP_PATH, params), stopHandler);

        return data;
    }

    /**
     * Makes a call to the OTP Extended API to get stop next departures informations.
     * 
     * @param id
     *            id of station
     * @throws GenericException
     *             unable to encode request parameters
     * @return a list of {@link BusDeparture}.
     */
    public final List<BusDeparture> getStopDepartures(final String id) throws GenericException {

        final List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(1);

        params.add(new BasicNameValuePair("id", id));

        final List<BusDeparture> data = httpService.execute(
                createOTPRequest(ItineRennesConstants.OTP_API_DEPARTURES_PATH, params),
                departuresHandler);

        return data;
    }

}
