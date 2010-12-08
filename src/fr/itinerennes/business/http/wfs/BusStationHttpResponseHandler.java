package fr.itinerennes.business.http.wfs;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeConstants;
import fr.itinerennes.beans.BusStation;
import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;

/**
 * Handles http responses containing bus stations in json format.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */
public class BusStationHttpResponseHandler extends HttpResponseHandler<List<BusStation>> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BusStationHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected final List<BusStation> handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject jsonResult = WFSUtils.getServiceResponse(content);

        final List<BusStation> stations = new ArrayList<BusStation>();

        try {
            final JSONArray jsonStations = jsonResult.getJSONArray("features");
            for (int i = 0; !jsonStations.isNull(i); i++) {
                // TOBO delete this condition when gtfs data will be fixed
                final JSONObject properties = jsonStations.getJSONObject(i).getJSONObject(
                        "properties");
                if (!properties.getString("stop_lat").equalsIgnoreCase("48.109946150056601")) {
                    stations.add(convertJsonObjectToBusStation(jsonStations.getJSONObject(i)));
                }
            }
        } catch (final JSONException e) {
            final String message = "a station can't be converted";
            LOGGER.error(message, e);
            throw new GenericException(ErrorCodeConstants.JSON_MALFORMED, message, e);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }
        return stations;
    }

    /**
     * Converts a json object to a bean representing a bus station.
     * 
     * @param jsonObject
     *            the json object to convert to a bus station
     * @return the bus station bean
     */
    private BusStation convertJsonObjectToBusStation(final JSONObject jsonObject) {

        final BusStation station = new BusStation();
        station.setId(jsonObject.optString("id"));

        final JSONObject properties = jsonObject.optJSONObject("properties");

        station.setName(properties.optString("stop_name"));
        station.setLatitude((int) (properties.optDouble("stop_lat") * 1E6));
        station.setLongitude((int) (properties.optDouble("stop_lon") * 1E6));

        return station;
    }
}
