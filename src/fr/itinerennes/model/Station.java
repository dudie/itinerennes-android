package fr.itinerennes.model;

import org.andnav.osm.util.GeoPoint;

import fr.itinerennes.ItineRennesConstants;

/**
 * Interface implemented by classes describing stations or stops.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public interface Station extends Cacheable {

    /** Life time for cached {@link Station}s : {@value #TTL} seconds. */
    int TTL = 3600;

    /** Station type bike. */
    int TYPE_BIKE = 0;

    /** Station type bus. */
    int TYPE_BUS = 1;

    /** Station type subway. */
    int TYPE_SUBWAY = 2;

    /**
     * Gets the identifier of the station.
     * 
     * @return the identifier of the station
     */
    @Override
    String getId();

    /**
     * Gets the name of the station.
     * 
     * @return the name of the station
     */
    String getName();

    /**
     * Gets the type of the station. Can be {@link #TYPE_BUS}, {@link #TYPE_BIKE} or
     * {@link #TYPE_SUBWAY}
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

    /**
     * Gets the icon id of the station.
     * 
     * @return the id of the stations's icon
     */
    int getIconDrawableId();
}
