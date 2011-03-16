package fr.itinerennes.business.http.keolis;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import fr.itinerennes.business.http.HttpResponseHandler;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.SubwayStation;
import fr.itinerennes.utils.StringUtils;

/**
 * Handles http responses containing subway stations in json format.
 * 
 * <pre>
 * "station":[
 *    {
 *       "id":"ANF",
 *       "name":"Anatole France",
 *       "latitude":"48.11810000",
 *       "longitude":"-1.687460000",
 *       "hasPlatformDirection1":"1",
 *       "hasPlatformDirection2":"1",
 *       "rankingPlatformDirection1":"12",
 *       "rankingPlatformDirection2":"18",
 *       "floors":"-1",
 *       "lastupdate":"2010-11-23T23:15:58+01:00"
 *    }
 * ]
 * </pre>
 * 
 * @author Jérémie Huchet
 */
public class SubwayStationHttpResponseHandler extends HttpResponseHandler<List<SubwayStation>> {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(SubwayStationHttpResponseHandler.class);

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.http.HttpResponseHandler#handleContent(java.lang.String)
     */
    @Override
    protected final List<SubwayStation> handleContent(final String content) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.start");
        }

        final JSONObject data = KeoUtils.getServiceResponse(content);

        List<SubwayStation> listStations = null;

        if (null != data) {
            // try to handle the response as if there is one station
            final JSONObject jsonStation = data.optJSONObject("station");
            if (null != jsonStation) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("response contains 1 station");
                }
                listStations = new ArrayList<SubwayStation>(1);
                listStations.add(convertJsonObjectToSubwayStation(jsonStation));
            } else {
                // else handle multiple stations
                final JSONArray jsonStations = data.optJSONArray("station");
                if (null != jsonStations) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("response contains multiple stations");
                    }
                    listStations = new ArrayList<SubwayStation>();
                    for (int i = 0; !jsonStations.isNull(i); i++) {
                        listStations.add(convertJsonObjectToSubwayStation(jsonStations
                                .optJSONObject(i)));
                    }
                }
            }
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleContent.end");
        }
        return listStations;
    }

    /**
     * Converts a json object to a bean representing a subway station.
     * 
     * @param jsonObject
     *            the json object to convert to a subway station
     * @return the subway station bean
     */
    private SubwayStation convertJsonObjectToSubwayStation(final JSONObject jsonObject) {

        final SubwayStation station = new SubwayStation();
        station.setId(jsonObject.optString("id"));
        station.setName(StringUtils.toStartCase(jsonObject.optString("name")));
        station.setLongitude((int) (jsonObject.optDouble("longitude") * 1E6));
        station.setLatitude((int) (jsonObject.optDouble("latitude") * 1E6));
        station.setHasPlatformDirection1(KeoUtils.convertJsonIntToBoolean(jsonObject
                .optInt("hasPlatformDirection1")));
        station.setHasPlatformDirection2(KeoUtils.convertJsonIntToBoolean(jsonObject
                .optInt("hasPlatformDirection2")));
        String rankPlatformDir = jsonObject.optString("rankingPlatformDirection1");
        if (null != rankPlatformDir && !"".equals(rankPlatformDir)) {
            station.setRankingPlatformDirection1(Integer.valueOf(rankPlatformDir));
        } else {
            station.setRankingPlatformDirection1(0);
        }
        rankPlatformDir = jsonObject.optString("rankingPlatformDirection2");
        if (null != rankPlatformDir && !"".equals(rankPlatformDir)) {
            station.setRankingPlatformDirection2(Integer.valueOf(rankPlatformDir));
        } else {
            station.setRankingPlatformDirection2(0);
        }
        station.setFloors(jsonObject.optInt("floors"));
        station.setLastUpdate(KeoUtils.convertJsonStringToDate(jsonObject.optString("lastupdate")));

        return station;
    }
}
