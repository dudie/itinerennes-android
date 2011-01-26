package fr.itinerennes.business.http.wfs;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.http.GenericHttpService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusStation;

/**
 * Manage calls to the Geoserver WFS API.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */

public class WFSService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(WFSService.class);

    /** The HTTP client. */
    private final GenericHttpService httpService = new GenericHttpService();

    /** The HTTP response handler. */
    private final BusStationHttpResponseHandler busHandler = new BusStationHttpResponseHandler();

    /**
     * Creates a generic request to the Geoserver WMS API. This method will set the request headers,
     * the output format to json.
     * 
     * @param parameters
     *            the request parameters
     * @return an {@link HttpPost} to send to execute the request
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

        if (LOGGER.isDebugEnabled()) {
            final StringBuilder msg = new StringBuilder();
            msg.append(req.getURI().toString()).append("?");
            for (final NameValuePair param : parameters) {
                msg.append(param.getName()).append("=").append(param.getValue()).append("&");
            }
            msg.deleteCharAt(msg.length() - 1);
            LOGGER.debug("createWFSRequest - {}", msg.toString());
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
     * @throws GenericException
     *             unable to encode request parameters
     * @return a list of bus stations as {@link JSONObject}s
     */
    public final List<BusStation> getBusStationsFromBbox(final BoundingBoxE6 bbox, final int max)
            throws GenericException {

        final List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        // TOBO peut-on envoyer directement des integer E6 à l'api WFS ?
        params.add(new BasicNameValuePair("bbox", String.format("%s,%s,%s,%s,%s",
                bbox.getLonWestE6() / 1E6, bbox.getLatSouthE6() / 1E6, bbox.getLonEastE6() / 1E6,
                bbox.getLatNorthE6() / 1E6, WFS.VALUE_SRS)));
        params.add(new BasicNameValuePair("request", WFS.VALUE_FEATURE_REQUEST));
        params.add(new BasicNameValuePair("typeName", WFS.VALUE_STOPS_LAYERS));

        if (max > 0) {
            params.add(new BasicNameValuePair("maxFeatures", String.valueOf(max)));
        }

        final List<BusStation> data = httpService.execute(createWFSRequest(params), busHandler);

        return data;
    }

    /**
     * Gets bus stations contained in a bbox.
     * 
     * @param bbox
     *            the bounding box
     * @return all bus stations contained in the bbox
     * @throws GenericException
     *             unable to get a result from the server
     */
    public final List<BusStation> getBusStationsFromBbox(final BoundingBoxE6 bbox)
            throws GenericException {

        return getBusStationsFromBbox(bbox, 0);
    }

    /**
     * Makes a call to the WFS API to get the bus station related to the given identifier.
     * 
     * @param id
     *            the identifier of the bus station
     * @return a bus station
     * @throws GenericException
     *             unable to get a result from the server
     */
    public final BusStation getBusStation(final String id) throws GenericException {

        final List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        params.add(new BasicNameValuePair("request", WFS.VALUE_FEATURE_REQUEST));
        params.add(new BasicNameValuePair("typeName", WFS.VALUE_STOPS_LAYERS));
        params.add(new BasicNameValuePair("featureId", id));

        final List<BusStation> data = httpService.execute(createWFSRequest(params), busHandler);

        if (data != null && data.size() > 1) {
            throw new GenericException(ErrorCodeConstants.WFS_RESPONSE_ERROR,
                    "the request returned more than one result");
        }

        if (data != null && !data.isEmpty()) {
            return data.get(0);
        } else {
            return null;
        }
    }
}
