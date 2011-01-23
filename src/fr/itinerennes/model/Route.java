package fr.itinerennes.model;

/**
 * Interface implemented by classes describing routes.
 * 
 * @author Olivier Boudet
 */
public interface Route extends Cacheable {

    /** Life time for cached {@link Route}s : {@value #TTL} seconds. */
    int TTL = 7776000;

    /**
     * Gets the identifier of the route.
     * 
     * @return the identifier of the route
     */
    @Override
    String getId();

}
