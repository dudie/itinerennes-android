package fr.itinerennes.model;

/**
 * Interface implemented by classes describing routes.
 * 
 * @author Olivier Boudet
 */
public interface Route extends Cacheable {

    /**
     * Gets the identifier of the route.
     * 
     * @return the identifier of the route
     */
    @Override
    String getId();

}
