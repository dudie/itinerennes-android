package fr.itinerennes.business.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import fr.itinerennes.business.cache.BikeStationCacheEntryHandler;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.database.DatabaseHelper;
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
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(BikeService.class);

    /** The keolis service. */
    private static final KeolisService keolisService = new KeolisService();

    /**
     * Creates a bike service.
     * 
     * @param dbHelper
     *            the database helper
     */
    public BikeService(final DatabaseHelper dbHelper) {

        super(dbHelper,
                new CacheProvider<BikeStation>(dbHelper, new BikeStationCacheEntryHandler()));
    }

    /**
     * Retrieves a bike station from the keolis network service.
     * 
     * @param id
     *            the identifier of the station
     * @return the bike station
     * @throws GenericException
     *             an error occurred
     * @see fr.itinerennes.business.service.AbstractKeolisStationProvider#retrieveFreshStation(java.lang.String)
     */
    @Override
    protected BikeStation retrieveFreshStation(final String id) throws GenericException {

        return keolisService.getBikeStation(id);
    }

    /**
     * Retrieves all bike stations from the keolis network service.
     * 
     * @return all the bike stations
     * @throws GenericException
     *             an error occurred
     * @see fr.itinerennes.business.service.AbstractKeolisStationProvider#retrieveAllStations()
     */
    @Override
    protected List<BikeStation> retrieveAllStations() throws GenericException {

        return keolisService.getAllBikeStations();
    }
}
