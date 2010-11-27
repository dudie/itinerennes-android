package fr.itinerennes.business.facade;

import java.util.List;

import fr.itinerennes.beans.BikeStation;
import fr.itinerennes.business.RemoteDataCacheProvider;
import fr.itinerennes.business.service.KeolisService;
import fr.itinerennes.exceptions.GenericException;

/**
 * Service to consult informations about the bike transport service.
 * <p>
 * Every method call is cached using the {@link RemoteDataCacheProvider}.
 * 
 * @author Jérémie Huchet
 */
public class BikeService {

    /** The Keolis service. */
    private final static KeolisService keolisService = KeolisService.getInstance();

    /**
     * Gets a bike station by its identifier.
     * 
     * @param id
     *            the identifier of the station
     * @return the requested station
     * @throws GenericException
     *             unable to retrieve the requested station
     */
    public static BikeStation getStation(final String id) throws GenericException {

        BikeStation station = RemoteDataCacheProvider.get(BikeStation.class, id);
        if (station == null) {
            station = keolisService.getBikeStation(id);
            RemoteDataCacheProvider.put(id, station);
        }
        return station;
    }

    /**
     * Gets all bike stations.
     * 
     * @return the requested stations
     * @throws GenericException
     *             unable to retrieve the stations
     */
    public static List<BikeStation> getAllStations() throws GenericException {

        final List<BikeStation> allStations = keolisService.getAllBikeStations();
        // TJHU permettre la récupération des stations d'une bounding box
        for (final BikeStation station : allStations) {
            RemoteDataCacheProvider.put(station.getId(), station);
        }
        return allStations;
    }
}
