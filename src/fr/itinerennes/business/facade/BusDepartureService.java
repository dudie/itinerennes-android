package fr.itinerennes.business.facade;

import java.util.Date;
import java.util.List;

import fr.itinerennes.business.http.otp.OTPService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusDeparture;

/**
 * Service to consult informations about the bus departures service.
 * 
 * @author Olivier Boudet
 */
public class BusDepartureService extends AbstractService {

    /** The OTP service. */
    private final OTPService otpService;

    /**
     * Creates an OTP service.
     * 
     * @param dbHelper
     *            the database helper
     */
    public BusDepartureService(final DatabaseHelper dbHelper) {

        super(dbHelper);
        otpService = new OTPService();
    }

    /**
     * Gets first departures for the given station.
     * 
     * @param stationId
     *            id of the station
     * @return the list of departures
     * @throws GenericException
     */
    public final List<BusDeparture> getStationDepartures(final String stationId)
            throws GenericException {

        return getStationDepartures(stationId, new Date());
    }

    /**
     * Gets first departures after the given date for the given station.
     * 
     * @param stationId
     *            id of the station
     * @param departureDate
     *            minimum date for departures to retrieve
     * @return the list of departures
     * @throws GenericException
     */
    public final List<BusDeparture> getStationDepartures(final String stationId,
            final Date departureDate) throws GenericException {

        return otpService.getStopDepartures(stationId, departureDate);
    }
}
