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

    /** Station type bike. */
    public static final int TYPE_VELO = 1;

    /** Station type bus. */
    public static final int TYPE_BUS = 2;

    /** Station type subway. */
    public static final int TYPE_SUBWAY = 3;

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
     * Gets the type of the station. Can be {@link ItineRennesConstants#TYPE_BUS},
     * {@link ItineRennesConstants#TYPE_VELO} or
     * {@link ItineRennesConstants#TYPE_SUBWAY}
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
