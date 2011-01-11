package fr.itinerennes.business.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.SubwayStationCacheEntryHandler;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.SubwayStation;

/**
 * Service to consult informations about the subway transport service.
 * <p>
 * Every method call is cached using the {@link CacheProvider} and delayed with a
 * {@link AbstractDelayedService}.
 * 
 * @author Jérémie Huchet
 */
public final class SubwayService extends AbstractKeolisStationProvider<SubwayStation> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(SubwayService.class);

    /** The Keolis service. */
    private static final KeolisService keolisService = new KeolisService();

    /**
     * Creates a bike service.
     * 
     * @param dbHelper
     *            the database helper
     */
    public SubwayService(final DatabaseHelper dbHelper) {

        super(dbHelper, new CacheProvider<SubwayStation>(dbHelper,
                new SubwayStationCacheEntryHandler(dbHelper)));
    }

    /**
     * Retrieves a subway station from the keolis network service.
     * 
     * @param id
     *            the station identifier
     * @return the subway station
     * @throws GenericException
     *             an error occurred
     * @see fr.itinerennes.business.facade.AbstractKeolisStationProvider#retrieveFreshStation(java.lang.String)
     */
    @Override
    protected SubwayStation retrieveFreshStation(final String id) throws GenericException {

        return keolisService.getSubwayStation(id);
    }

    /**
     * Retrieves all subway stations from the keolis network service.
     * 
     * @return all the subway stations
     * @throws GenericException
     *             an error occurred
     * @see fr.itinerennes.business.facade.AbstractKeolisStationProvider#retrieveAllStations()
     */
    @Override
    protected List<SubwayStation> retrieveAllStations() throws GenericException {

        return keolisService.getAllSubwayStations();
    }

}
