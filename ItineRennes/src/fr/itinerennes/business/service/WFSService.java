package fr.itinerennes.business.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.beans.BoundingBox;
import fr.itinerennes.beans.BusStation;
import fr.itinerennes.business.http.wms.WFSJsonService;
import fr.itinerennes.exceptions.GenericException;

/**
 * Manage calls to the Geoserver WFS API.
 * 
 * @author Olivier Boudet
 */
public class WFSService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(WFSService.class);

    /** The WFS json service. */
    private static final WFSJsonService wfsJsonService = new WFSJsonService();

    /** The unique instance of this service. */
    private static WFSService instance = new WFSService();

    /**
     * Private constructor to avoid instanciation.
     */
    private WFSService() {

    }

    /**
     * Gets the unique instance of this service.
     * 
     * @return the unique instance of this service
     */
    public static WFSService getInstance() {

        return instance;
    }

    /**
     * Gets bus stations contained in a bbox.
     * 
     * @param bbox
     *            the bounding box
     * @param max
     *            maximum number of results to fetch
     * @return all bus stations contained in the bbox
     * @throws GenericException
     *             unable to get a result from the server
     */
    public List<BusStation> getBusStationsFromBbox(BoundingBox bbox, int max)
            throws GenericException {

        final List<BusStation> stations = new ArrayList<BusStation>();
        try {
            final JSONArray jsonStations = wfsJsonService.getBusStationsFromBbox(bbox, max);

            for (int i = 0; !jsonStations.isNull(i); i++) {
                stations.add(convertJsonObjectToBusStation(jsonStations.getJSONObject(i)));
            }
        } catch (final JSONException e) {
            throw new GenericException(ErrorCodeConstants.JSON_MALFORMED, String.format(
                    "unable to parse server response : %s", e.getMessage()));
        }

        return stations;
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
    public List<BusStation> getBusStationsFromBbox(BoundingBox bbox) throws GenericException {

        return getBusStationsFromBbox(bbox, 0);
    }

    /**
     * Gets a bus station by its identifier.
     * 
     * @param id
     *            the identifier of the bus station
     * @return the bus station
     * @throws GenericException
     *             unable to get a result from the server
     */
    public BusStation getBusStation(final String id) throws GenericException {

        BusStation station = null;
        try {
            station = convertJsonObjectToBusStation(wfsJsonService.getBusStation(id));
        } catch (final JSONException e) {
            throw new GenericException(ErrorCodeConstants.JSON_MALFORMED, String.format(
                    "unable to parse server response : %s", e.getMessage()), e);
        }
        return station;
    }

    /**
     * Converts a json object to a bean representing a bus station.
     * 
     * @param jsonObject
     *            the json object to convert to a bus station
     * @return the bus station bean
     * @throws JSONException
     *             an error occurred while converting the json object to a {@link BusStation}
     */
    private BusStation convertJsonObjectToBusStation(final JSONObject jsonObject)
            throws JSONException {

        final BusStation station = new BusStation();
        station.setId(jsonObject.getString("id"));

        final JSONObject properties = jsonObject.getJSONObject("properties");

        station.setName(properties.getString("stop_name"));
        station.setLatitude(properties.getDouble("stop_lat"));
        station.setLongitude(properties.getDouble("stop_lon"));

        return station;
    }

}
