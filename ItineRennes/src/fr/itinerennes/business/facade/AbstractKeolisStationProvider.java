package fr.itinerennes.business.facade;

import java.util.List;

import org.andnav.osm.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Station;

/**
 * Manages commons functionalities offered by {@link StationProvider}s using {@link KeolisService}.
 * 
 * @param <T>
 *            the type of stations returned by the service
 * @author Jérémie Huchet
 */
public abstract class AbstractKeolisStationProvider<T extends Station> implements StationProvider {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(AbstractKeolisStationProvider.class);

    /** The service delayer used to retrieve stations. */
    private final AbstractDelayedService<T> delayedService;

    /**
     * The cache of stations (this reference points to the same instance the
     * <code>delayedService</code> uses).
     */
    private final CacheProvider<T> cache;

    /**
     * Creates the service.
     * 
     * @param delayedService
     *            a delayed service
     */
    public AbstractKeolisStationProvider(final AbstractDelayedService<T> delayedService) {

        this.delayedService = delayedService;
        this.cache = delayedService.getCache();
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#getStation(java.lang.String)
     */
    @Override
    public T getStation(final String id) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStation.start - id={}", id);
        }

        // TJHU consulter les métadonnées du cache pour vérifier que la station retournée depuis le
        // cache n'est pas périmiée
        final T station = delayedService.getResource(id);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStation.end - stationNotNull=%s", station != null);
        }
        return station;
    }

    /**
     * Gets a station by its identifier. Unlike {@link #getStation(String)}, this method ensures the
     * result is up to date.
     * 
     * @param id
     *            the station identifier
     * @return the up to date station
     * @throws GenericException
     *             an error occurred
     */
    public T getFreshStation(final String id) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getFreshStation.start - id={}", id);
        }
        // TJHU contrôler la date de dernière mise à jour de l'information dans le cache. Si date <
        // 60 secondes, mise à jour inutile et donc pas d'appel à retrieveFreshStation() mais appel
        // au cache directement
        final T station = retrieveFreshStation(id);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getFreshStation.end - stationNotNull=%s", station != null);
        }
        return station;
    }

    /**
     * Gets an up to date station from the keolis service.
     * 
     * @param id
     *            the identifier of the station
     * @return the up to date station
     * @throws GenericException
     *             an error occurred
     */
    protected abstract T retrieveFreshStation(final String id) throws GenericException;

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#getStations(org.andnav.osm.util.BoundingBoxE6)
     */
    @Override
    public List<T> getStations(final BoundingBoxE6 bbox) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStations.start - bbos={}", bbox);
        }

        final List<T> stations = cache.load(bbox);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getStations.end - %s stations", stations == null ? 0 : stations.size());
        }

        return stations;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.facade.StationProvider#release()
     */
    @Override
    public void release() {

        // TODO Auto-generated method stub

    }
}
