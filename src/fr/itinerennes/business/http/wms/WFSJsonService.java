package fr.itinerennes.business.http.wms;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.beans.BoundingBox;
import fr.itinerennes.business.http.GenericHttpService;
import fr.itinerennes.exceptions.GenericException;

/**
 * Manage calls to the Geoserver WMS API.
 * 
 * @author Olivier Boudet
 */

public class WFSJsonService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(WFSJsonService.class);

    /** The HTTP client. */
    private final GenericHttpService httpService = new GenericHttpService();

    /** The HTTP response handler. */
    private final WFSResponseHandler responseHandler = new WFSResponseHandler();

    /**
     * Creates a generic request to the Geoserver WMS API. This method will set the request headers,
     * the output format to json.
     * 
     * @param parameters
     *            the request parameters
     * @return an {@link HttpGet} to send to execute the request
     * @throws GenericException
     *             unable to encode request parameters
     */
    private HttpPost createWFSRequest(final List<NameValuePair> parameters) throws GenericException {

        final HttpPost req = new HttpPost(ItineRennesConstants.GEOSERVER_API_URL);
        req.addHeader("Accept", "text/json");
        req.addHeader("Accept", "applicationtion/json");
        parameters.add(new BasicNameValuePair("service", WFS.VALUE_SERVICE));
        parameters.add(new BasicNameValuePair("version", WFS.VALUE_VERSION));
        parameters.add(new BasicNameValuePair("outputFormat", WFS.VALUE_FORMAT));
        parameters.add(new BasicNameValuePair("srsName", WFS.VALUE_SRS));

        try {
            req.setEntity(new UrlEncodedFormEntity(parameters));
        } catch (final UnsupportedEncodingException e) {
            throw new GenericException(ErrorCodeConstants.API_CALL_FAILED,
                    "unable to encode request parameters", e);
        }

        return req;
    }

    /**
     * Makes a call to the Geoserver WMS API to get bus stations in a bounding box.
     * 
     * @param bbox
     *            the bounding box
     * @param max
     *            optional maximum number of results to fetch
     * @throws JSONException
     *             unable to parse the json response of the server
     */
    public JSONArray getBusStationsFromBbox(BoundingBox bbox, int max) throws GenericException,
            JSONException {

        final List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        params.add(new BasicNameValuePair("bbox", String.format("%s,%s", bbox.toString(),
                WFS.VALUE_SRS)));
        params.add(new BasicNameValuePair("request", WFS.VALUE_FEATURE_REQUEST));
        params.add(new BasicNameValuePair("typeName", WFS.VALUE_STOPS_LAYERS));

        if (max > 0) {
            params.add(new BasicNameValuePair("maxFeatures", String.valueOf(max)));
        }

        final JSONObject data = httpService.execute(createWFSRequest(params), responseHandler);

        return data.getJSONArray("features");
    }

    /**
     * Makes a call to the WFS API to get the bus station related to the given identifier.
     * 
     * @param id
     *            the identifier of the bus station
     * @return a {@link JSONObject} containing the bus station
     * @throws GenericException
     *             unable to get a result from the server
     * @throws JSONException
     *             unable to parse the json response of the server
     */
    public JSONObject getBusStation(final String id) throws GenericException, JSONException {

        final List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        params.add(new BasicNameValuePair("request", WFS.VALUE_FEATURE_REQUEST));
        params.add(new BasicNameValuePair("typeName", WFS.VALUE_STOPS_LAYERS));
        params.add(new BasicNameValuePair("featureId", id));

        final JSONObject data = httpService.execute(createWFSRequest(params), responseHandler);

        if (data.getJSONArray("features").length() > 1)
            throw new GenericException(ErrorCodeConstants.WFS_RESPONSE_ERROR,
                    "the request returns more than one result");
        return data.getJSONArray("features").getJSONObject(0);
    }
}
