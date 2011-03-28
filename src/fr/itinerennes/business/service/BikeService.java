package fr.itinerennes.business.service;

import java.io.IOException;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.keolis.client.JsonKeolisClient;
import fr.itinerennes.keolis.client.KeolisClient;
import fr.itinerennes.keolis.model.BikeStation;
import fr.itinerennes.ui.activity.ItinerennesContext;

/**
 * Service to consult informations about the bike transport service.
 * <p>
 * Every method call is cached using the {@link CacheProvider} and delayed with a
 * {@link AbstractDelayedService}.
 * 
 * @author Jérémie Huchet
 */
public final class BikeService {

    /** The keolis service. */
    private static KeolisClient keolisClient;

    /**
     * Creates a bike service.
     */
    public BikeService(final ItinerennesContext context) {

        keolisClient = new JsonKeolisClient(context.getHttpClient(),
                ItineRennesConstants.KEOLIS_API_URL, ItineRennesConstants.KEOLIS_API_KEY);
    }

    /**
     * Retrieves a bike station from the keolis network service.
     * 
     * @param id
     *            the identifier of the station
     * @return the bike station
     * @throws IOException
     *             an error occurred
     */
    public BikeStation getBikeStation(final String id) throws IOException {

        return keolisClient.getBikeStation(id);
    }

}
