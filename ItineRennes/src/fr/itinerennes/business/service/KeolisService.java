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

    /**
     * Gets all bike stations.
     * 
     * @return all bike stations
     * @throws GenericException
     *             unable to get a result from the server
     */
    public static List<BikeStation> getAllBikeStations() throws GenericException {

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
    public static List<BikeStation> getBikeStationsNearFrom(final double latitude,
            final double longitude) throws GenericException {

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
    public static BikeStation getBikeStation(final int id) throws GenericException {

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
     * @param jsonObject
     *            the json object to convert to a bike station
     * @return the bike station bean
     * @throws JSONException
     *             an error occurred while converting the json object to a {@link BikeStation}
     */
    private static BikeStation convertJsonObjectToBikeStation(final JSONObject jsonObject)
            throws JSONException {

        final BikeStation station = new BikeStation();
        station.setActive(convertJsonStringToBoolean(jsonObject.getInt("state")));
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
    private static Date convertJsonStringToDate(final String stringDate) throws JSONException {

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
    private static boolean convertJsonStringToBoolean(final int status) {

        return 0 != status;
    }

}
