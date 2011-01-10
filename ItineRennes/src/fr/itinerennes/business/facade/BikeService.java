package fr.itinerennes.business.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.BikeStationCacheEntryHandler;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.BikeStation;

/**
 * Service to consult informations about the bike transport service.
 * <p>
 * Every method call is cached using the {@link CacheProvider} and delayed with a
 * {@link AbstractDelayedService}.
 * 
 * @author Jérémie Huchet
 */
public final class BikeService extends AbstractKeolisStationProvider<BikeStation> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BikeService.class);

    /** The keolis service. */
    private static final KeolisService keolisService = new KeolisService();

    /**
     * Creates a bike service.
     * 
     * @param database
     *            the database
     */
    public BikeService(final SQLiteDatabase database) {

        super(new AbstractDelayedService<BikeStation>(
                new CacheProvider<BikeStation>(database,
                        new BikeStationCacheEntryHandler(database),
                        ItineRennesConstants.TTL_BIKE_STATIONS),
                ItineRennesConstants.MIN_TIME_BETWEEN_KEOLIS_GET_ALL_CALLS) {

            @Override
            protected List<BikeStation> getAll() throws GenericException {

                return keolisService.getAllBikeStations();
            }
        });
    }

    /**
     * Retrieves a bike station from the keolis network service.
     * 
     * @return the bike station
     * @throws GenericException
     *             an error occurred
     * @see fr.itinerennes.business.facade.AbstractKeolisStationProvider#retrieveFreshStation(java.lang.String)
     */
    @Override
    protected final BikeStation retrieveFreshStation(final String id) throws GenericException {

        return keolisService.getBikeStation(id);
    }
}
