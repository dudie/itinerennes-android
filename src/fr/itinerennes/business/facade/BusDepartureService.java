package fr.itinerennes.business.facade;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.business.http.otp.OTPService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BusDeparture;

/**
 * Service to consult informations about the bus departures service.
 * 
 * @author Olivier Boudet
 */
public class BusDepartureService {

    /** The OTP service. */
    private final OTPService otpService;

    /**
     * Creates an OTP service.
     * 
     * @param database
     *            the database
     */
    public BusDepartureService(final SQLiteDatabase database) {

        otpService = new OTPService();
    }

    public List<BusDeparture> getStationDepartures(final String stationId) throws GenericException {

        return otpService.getStopDepartures(stationId);
    }
}
