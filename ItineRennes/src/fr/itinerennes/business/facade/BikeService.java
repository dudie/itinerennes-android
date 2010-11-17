package fr.itinerennes.business.facade;

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

	/**
	 * Gets a bike station by its identifier.
	 * 
	 * @param id
	 *            the identifier of the station
	 * @return the requested station
	 * @throws GenericException
	 *             unable to retrieve the requested station
	 */
	public static BikeStation getStation(final int id) throws GenericException {

		BikeStation station = RemoteDataCacheProvider.get(BikeStation.class, id);
		if (station == null) {
			station = KeolisService.getBikeStation(id);
			RemoteDataCacheProvider.put(id, station);
		}
		return station;
	}
}
