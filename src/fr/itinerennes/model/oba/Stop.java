package fr.itinerennes.model.oba;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

/**
 * Class representing a OneBusAway stop element.
 * 
 * @author Olivier Boudet
 */
public class Stop {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(Stop.class);

    /** Id of the stop. */
    private String id;

    /** Latitude of the stop. */
    private Double lat;

    /** Longitude of the stop. */
    private Double lon;

    /** Direction of the stop. */
    private String direction;

    /** Name of the stop. */
    private String name;

    /** Code of the stop. */
    private int code;

    /** All route serving this stop. */
    private final List<Route> routes;

    public Stop() {

        routes = new ArrayList<Route>();
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {

        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the id to set
     */
    public void setId(final String id) {

        this.id = id;
    }

    /**
     * Gets the latitude of the stop.
     * 
     * @return the latitude
     */
    public Double getLat() {

        return lat;
    }

    /**
     * Sets the latitude of the stop.
     * 
     * @param latitude
     *            the latitude to set
     */
    public void setLat(final Double lat) {

        this.lat = lat;
    }

    /**
     * Gets the longitude of the stop.
     * 
     * @return the longitude
     */
    public Double getLon() {

        return lon;
    }

    /**
     * Sets the longitude of the stop.
     * 
     * @param lon
     *            the longitude to set
     */
    public void setLon(final Double lon) {

        this.lon = lon;
    }

    /**
     * Gets the direction of the stop.
     * 
     * @return the direction
     */
    public String getDirection() {

        return direction;
    }

    /**
     * Sets the direction.
     * 
     * @param direction
     *            the direction to set
     */
    public void setDirection(final String direction) {

        this.direction = direction;
    }

    /**
     * Gets the name of the stop.
     * 
     * @return the name
     */
    public String getName() {

        return name;
    }

    /**
     * Sets the name of the stop.
     * 
     * @param name
     *            the name to set
     */
    public void setName(final String name) {

        this.name = name;
    }

    /**
     * Gets the code of the stop.
     * 
     * @return the code
     */
    public int getCode() {

        return code;
    }

    /**
     * Sets the code of the stop.
     * 
     * @param code
     *            the code to set
     */
    public void setCode(final int code) {

        this.code = code;
    }

    /**
     * Gets the routes of the stop.
     * 
     * @return the routes
     */
    public List<Route> getRoutes() {

        return routes;
    }

}
