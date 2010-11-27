package fr.itinerennes.business.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.ErrorCodeContants;
import fr.itinerennes.beans.BikeStation;
import fr.itinerennes.beans.SubwayStation;
import fr.itinerennes.business.http.keolis.KeolisJsonService;
import fr.itinerennes.exceptions.GenericException;

/**
 * Manage calls to the Keolis API.
 * 
 * @author Jérémie Huchet
 */
public class KeolisService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(KeolisService.class);

    /** The date format the Keolis service use to send. */
    private static final SimpleDateFormat KEOLIS_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ssZ");

    /** The Keolis json service. */
    private static final KeolisJsonService keolisJsonService = new KeolisJsonService();

    /** The unique instance of this service. */
    private static KeolisService instance = new KeolisService();

    /**
     * Private constructor to avoid instanciation.
     */
    private KeolisService() {

    }

    /**
     * Gets the unique instance of this service.
     * 
     * @return the unique instance of this service
     */
    public static KeolisService getInstance() {

        return instance;
    }

    /**
     * Gets all bike stations.
     * 
     * @return all bike stations
     * @throws GenericException
     *             unable to get a result from the server
     */
    public List<BikeStation> getAllBikeStations() throws GenericException {

        final List<BikeStation> stations = new ArrayList<BikeStation>();
        try {
            final JSONArray jsonStations = keolisJsonService.getAllBikeStations();

            for (int i = 0; !jsonStations.isNull(i); i++) {
                stations.add(convertJsonObjectToBikeStation(jsonStations.getJSONObject(i)));
            }
        } catch (final JSONException e) {
            throw new GenericException(ErrorCodeContants.JSON_MALFORMED, String.format(
                    "unable to parse server response : %s", e.getMessage()));
        }

        return stations;
    }

    /**
     * Gets the first 3 nearest stations ordered by the nearest to the farest.
     * 
     * @param longitude
     *            the latitude
     * @param latitude
     *            the longitude
     * @return the first 3 nearest stations
     * @throws GenericException
     *             unable to get a result from the server
     */
    public List<BikeStation> getBikeStationsNearFrom(final double latitude, final double longitude)
            throws GenericException {

        final List<BikeStation> stations = new ArrayList<BikeStation>();
        try {
            final JSONArray jsonStations = keolisJsonService.getBikeStationsNearFrom(latitude,
                    longitude);

            for (int i = 0; !jsonStations.isNull(i); i++) {
                stations.add(convertJsonObjectToBikeStation(jsonStations.getJSONObject(i)));
            }
        } catch (final JSONException e) {
            throw new GenericException(ErrorCodeContants.JSON_MALFORMED, String.format(
                    "unable to parse server response : %s", e.getMessage()));
        }
        return stations;
    }

    /**
     * Gets a bike station by its identifier.
     * 
     * @param id
     *            the identifier of the bike station
     * @return the bike station
     * @throws GenericException
     *             unable to get a result from the server
     */
    public BikeStation getBikeStation(final String id) throws GenericException {

        BikeStation station = null;
        try {
            station = convertJsonObjectToBikeStation(keolisJsonService.getBikeStation(id));
        } catch (final JSONException e) {
            throw new GenericException(ErrorCodeContants.JSON_MALFORMED, String.format(
                    "unable to parse server response : %s", e.getMessage()));
        }
        return station;
    }

    /**
     * Converts a json object to a bean representing a bike station.
     * 
     * <pre>
     * "station":[
     *    {
     *       "number":"75",
     *       "name":"ZAC SAINT SULPICE",
     *       "address":"RUE DE FOUG\u00c8RES",
     *       "state":"1",
     *       "latitude":"48.1321",
     *       "longitude":"-1.63528",
     *       "slotsavailable":"29",
     *       "bikesavailable":"1",
     *       "pos":"0",
     *       "district":"Maurepas - Patton",
     *       "lastupdate":"2010-11-24T00:03:05+01:00"
     *    }
     * ]
     * </pre>
     * 
     * @param jsonObject
     *            the json object to convert to a bike station
     * @return the bike station bean
     * @throws JSONException
     *             an error occurred while converting the json object to a {@link BikeStation}
     */
    private BikeStation convertJsonObjectToBikeStation(final JSONObject jsonObject)
            throws JSONException {

        final BikeStation station = new BikeStation();
        station.setActive(convertJsonIntToBoolean(jsonObject.getInt("state")));
        station.setAddress(jsonObject.getString("address"));
        station.setAvailableBikes(jsonObject.getInt("bikesavailable"));
        station.setAvailableSlots(jsonObject.getInt("slotsavailable"));
        station.setDistrict(jsonObject.getString("district"));
        station.setId(jsonObject.getString("number"));
        station.setLastUpdate(convertJsonStringToDate(jsonObject.getString("lastupdate")));
        station.setLatitude(jsonObject.getDouble("latitude"));
        station.setLongitude(jsonObject.getDouble("longitude"));
        station.setName(jsonObject.getString("name"));
        station.setPos(jsonObject.getInt("pos"));

        return station;
    }

    /**
     * Converts Keolis date string to a {@link Date}.
     * 
     * @param stringDate
     *            the date as a string ("2010-11-11T20:47:06+01:00")
     * @return the date
     * @throws JSONException
     *             the date is malformed
     */
    private Date convertJsonStringToDate(final String stringDate) throws JSONException {

        try {
            return KEOLIS_DATE_FORMAT.parse(stringDate);
        } catch (final ParseException e) {
            throw new JSONException(e.getMessage());
        }
    }

    /**
     * Converts Keolis int status to a {@link Boolean}.
     * 
     * @param status
     * @return false if the given integer is equals to 0, else true
     */
    private boolean convertJsonIntToBoolean(final int status) {

        return 0 != status;
    }

    /**
     * Gets a subway station by its identifier.
     * 
     * @param id
     *            the identifier of the subway station
     * @return the subway station
     * @throws GenericException
     *             unable to get a result from the server
     */
    public SubwayStation getSubwayStation(final int id) throws GenericException {

        SubwayStation station = null;
        try {
            station = convertJsonObjectToSubwayStation(keolisJsonService.getSubwayStation(id));
        } catch (final JSONException e) {
            throw new GenericException(ErrorCodeContants.JSON_MALFORMED, String.format(
                    "unable to parse server response : %s", e.getMessage()));
        }
        return station;
    }

    /**
     * Converts a json object to a bean representing a subway station.
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
     * @param jsonObject
     *            the json object to convert to a subway station
     * @return the subway station bean
     * @throws JSONException
     *             an error occurred while converting the json object to a {@link SubwayStation}
     */
    private SubwayStation convertJsonObjectToSubwayStation(final JSONObject jsonObject)
            throws JSONException {

        final SubwayStation station = new SubwayStation();
        station.setId(jsonObject.getString("id"));
        station.setName(jsonObject.getString("name"));
        station.setLongitude(jsonObject.getDouble("latitude"));
        station.setLatitude(jsonObject.getDouble("latitude"));
        station.setHasPlatformDirection1(convertJsonIntToBoolean(jsonObject
                .getInt("rankingPlatformDirection1")));
        station.setHasPlatformDirection2(convertJsonIntToBoolean(jsonObject
                .getInt("rankingPlatformDirection2")));
        station.setRankingPlatformDirection1(jsonObject.getInt("rankingPlatformDirection1"));
        station.setRankingPlatformDirection2(jsonObject.getInt("rankingPlatformDirection2"));
        station.setFloors(jsonObject.getInt("floors"));
        station.setLastUpdate(convertJsonStringToDate(jsonObject.getString("lastupdate")));

        return station;
    }
}
