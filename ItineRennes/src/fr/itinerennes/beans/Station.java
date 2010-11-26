package fr.itinerennes.beans;

import org.andnav.osm.util.GeoPoint;

import fr.itinerennes.ItineRennesConstants;

/**
 * Interface implemented by classes describing stations or stops.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public interface Station {

    /**
     * Gets the identifier of the station.
     * 
     * @return the identifier of the station
     */
    String getId();

    /**
     * Gets the name of the station.
     * 
     * @return the name of the station
     */
    String getName();

    /**
     * Gets the type of the station. Can be {@link ItineRennesConstants#STATION_TYPE_BUS},
     * {@link ItineRennesConstants#STATION_TYPE_VELO} or
     * {@link ItineRennesConstants#STATION_TYPE_SUBWAY}
     * 
     * @see ItineRennesConstants
     * @return the type of the station
     */
    int getType();

    /**
     * Gets the geographical localization of the station.
     * 
     * @return the geographical localization of the station
     */
    GeoPoint getGeoPoint();
}
