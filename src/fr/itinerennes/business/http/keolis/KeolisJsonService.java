package fr.itinerennes.business.http.keolis;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeContants;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.http.GenericHttpService;
import fr.itinerennes.exceptions.GenericException;

/**
 * Manage calls to the Keolis API.
 * 
 * @author Jérémie Huchet
 */
public class KeolisJsonService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(KeolisJsonService.class);

    /** The HTTP client. */
    private final GenericHttpService httpService = new GenericHttpService();

    /** The HTTP response handler. */
    private final KeolisResponseHandler responseHandler = new KeolisResponseHandler();

    /**
     * Creates a generic request to the Keolis API. This method will set the request headers, the
     * version and the API key needed to send a request to Keolis.
     * 
     * @param parameters
     *            the request parameters
     * @return an {@link HttpPost} to send to execute the request
     * @throws GenericException
     *             unable to encode request parameters
     */
    private HttpPost createKeolisRequest(final List<NameValuePair> parameters)
            throws GenericException {

        final HttpPost req = new HttpPost(ItineRennesConstants.KEOLIS_API_URL);
        req.addHeader("Accept", "text/json");
        req.addHeader("Accept", "application/json");
        parameters.add(new BasicNameValuePair(Keo.API_VERSION,
                ItineRennesConstants.KEOLIS_API_VERSION));
        parameters.add(new BasicNameValuePair(Keo.API_KEY, ItineRennesConstants.KEOLIS_API_KEY));

        try {
            req.setEntity(new UrlEncodedFormEntity(parameters));
        } catch (final UnsupportedEncodingException e) {
            throw new GenericException(ErrorCodeContants.API_CALL_FAILED,
                    "unable to encode request parameters", e);
        }

        if (LOGGER.isDebugEnabled()) {
            final StringBuilder msg = new StringBuilder();
            msg.append(req.getURI().toString()).append("?");
            for (final NameValuePair param : parameters) {
                msg.append(param.getName()).append("=").append(param.getValue()).append("&");
            }
            msg.deleteCharAt(msg.length() - 1);
            LOGGER.debug(msg.toString());
        }

        return req;
    }

    /**
     * Makes a call to the Keolis API to get all bike stations.
     * 
     * @throws GenericException
     *             unable to get a result from the server
     * @throws JSONException
     *             unable to parse the json response of the server
     */
    public JSONArray getAllBikeStations() throws GenericException, JSONException {

        final List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        params.add(new BasicNameValuePair(Keo.Network.PARAM_NAME, Keo.Network.VALUE_LE_VELO_STAR));
        params.add(new BasicNameValuePair(Keo.Command.PARAM_NAME, Keo.Command.GET_BIKE_STATIONS));
        params.add(new BasicNameValuePair(Keo.GetBikeStations.PARAM_STATION,
                Keo.GetBikeStations.VALUE_STATION_ALL));

        JSONObject data = null;
        data = httpService.execute(createKeolisRequest(params), responseHandler);

        return data.getJSONArray("station");
    }

    /**
     * Makes a call to the Keolis API to get the 3 first nearest stations.
     * 
     * @throws GenericException
     *             unable to get a result from the server
     * @throws JSONException
     *             unable to parse the json response of the server
     */
    public JSONArray getBikeStationsNearFrom(final double latitude, final double longitude)
            throws GenericException, JSONException {

        final List<NameValuePair> params = new ArrayList<NameValuePair>(8);
        params.add(new BasicNameValuePair(Keo.Network.PARAM_NAME, Keo.Network.VALUE_LE_VELO_STAR));
        params.add(new BasicNameValuePair(Keo.Command.PARAM_NAME, Keo.Command.GET_BIKE_STATIONS));
        params.add(new BasicNameValuePair(Keo.GetBikeStations.PARAM_STATION,
                Keo.GetBikeStations.VALUE_STATION_PROXIMITY));
        params.add(new BasicNameValuePair(Keo.GetBikeStations.PARAM_MODE,
                Keo.GetBikeStations.VALUE_MODE_COORDINATES));
        params.add(new BasicNameValuePair(Keo.GetBikeStations.PARAM_LATITUDE, String
                .valueOf(latitude)));
        params.add(new BasicNameValuePair(Keo.GetBikeStations.PARAM_LONGITUDE, String
                .valueOf(longitude)));

        JSONObject data = null;
        synchronized (httpService) {
            data = httpService.execute(createKeolisRequest(params), responseHandler);
        }

        return data.getJSONArray("station");
    }

    /**
     * Makes a call to the Keolis API to get the bike station related to the given identifier.
     * 
     * @param id
     *            the identifier of the bike station
     * @throws GenericException
     *             unable to get a result from the server
     * @throws JSONException
     *             unable to parse the json response of the server
     */
    public JSONObject getBikeStation(final String id) throws GenericException, JSONException {

        final List<NameValuePair> params = new ArrayList<NameValuePair>(6);
        params.add(new BasicNameValuePair(Keo.Network.PARAM_NAME, Keo.Network.VALUE_LE_VELO_STAR));
        params.add(new BasicNameValuePair(Keo.Command.PARAM_NAME, Keo.Command.GET_BIKE_STATIONS));
        params.add(new BasicNameValuePair(Keo.GetBikeStations.PARAM_STATION,
                Keo.GetBikeStations.VALUE_STATION_IDENTIFIER));
        params.add(new BasicNameValuePair(Keo.GetBikeStations.PARAM_VALUE, id));

        JSONObject data = null;
        synchronized (httpService) {
            data = httpService.execute(createKeolisRequest(params), responseHandler);
        }

        return data.getJSONObject("station");
    }

    /**
     * Makes a call to the Keolis API to get the subway station related to the given identifier.
     * 
     * @param id
     *            the identifier of the subway station
     * @throws GenericException
     *             unable to get a result from the server
     * @throws JSONException
     *             unable to parse the json response of the server
     */
    public JSONObject getSubwayStation(final int id) throws GenericException, JSONException {

        final List<NameValuePair> params = new ArrayList<NameValuePair>(6);
        params.add(new BasicNameValuePair(Keo.Network.PARAM_NAME, Keo.Network.VALUE_STAR));
        params.add(new BasicNameValuePair(Keo.Command.PARAM_NAME, Keo.Command.GET_METRO_STATIONS));
        // params.add(new BasicNameValuePair(Keo.Mode.ATT_NAME, Keo.Mode.IDENTIFIER));
        // params.add(new BasicNameValuePair(Keo.VALUE, String.valueOf(id)));

        JSONObject data = null;
        synchronized (httpService) {
            data = httpService.execute(createKeolisRequest(params), responseHandler);
        }

        return data.getJSONObject("station");
    }
}
