package fr.itinerennes.business.service;

import java.io.IOException;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.keolis.client.JsonKeolisClient;
import fr.itinerennes.keolis.client.KeolisClient;
import fr.itinerennes.keolis.model.SubwayStation;
import fr.itinerennes.ui.activity.ItinerennesContext;

/**
 * Service to consult informations about the subway transport service.
 * <p>
 * Every method call is cached using the {@link CacheProvider} and delayed with a
 * {@link AbstractDelayedService}.
 * 
 * @author Jérémie Huchet
 */
public final class SubwayService {

    /** The keolis service. */
    private static KeolisClient keolisClient;

    /**
     * Creates a subway service.
     * 
     * @param dbHelper
     *            the database helper
     */
    public SubwayService(final ItinerennesContext context) {

        keolisClient = new JsonKeolisClient(context.getHttpClient(),
                ItineRennesConstants.KEOLIS_API_URL, ItineRennesConstants.KEOLIS_API_KEY);
    }

    /**
     * Retrieves a subway station from the keolis network service.
     * 
     * @param id
     *            the station identifier
     * @return the subway station
     * @throws IOException
     *             an error occurred
     */
    public SubwayStation getSubwayStation(final String id) throws IOException {

        return keolisClient.getSubwayStation(id);
    }

}
