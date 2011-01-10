package fr.itinerennes.business.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.SubwayStationCacheEntryHandler;
import fr.itinerennes.business.http.keolis.KeolisService;
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
public class SubwayService extends AbstractKeolisStationProvider<SubwayStation> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(SubwayService.class);

    /** The Keolis service. */
    private static final KeolisService keolisService = new KeolisService();;

    /**
     * Creates a bike service.
     * 
     * @param database
     *            the database
     */
    public SubwayService(final SQLiteDatabase database) {

        super(new AbstractDelayedService<SubwayStation>(new CacheProvider<SubwayStation>(database,
                new SubwayStationCacheEntryHandler(database),
                ItineRennesConstants.TTL_SUBWAY_STATIONS),
                ItineRennesConstants.MIN_TIME_BETWEEN_KEOLIS_GET_ALL_CALLS) {

            @Override
            protected List<SubwayStation> getAll() throws GenericException {

                return keolisService.getAllSubwayStations();
            }
        });
    }

    /**
     * Retrieves a subway station from the keolis network service.
     * 
     * @return the subway station
     * @throws GenericException
     *             an error occurred
     * @see fr.itinerennes.business.facade.AbstractKeolisStationProvider#retrieveFreshStation(java.lang.String)
     */
    @Override
    protected final SubwayStation retrieveFreshStation(final String id) throws GenericException {

        return keolisService.getSubwayStation(id);
    }

}
