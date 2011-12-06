package fr.itinerennes.exceptions;

/**
 * Implementing classes defines the application behavior when business errors occurs.
 * 
 * @author Jérémie Huchet
 */
public interface ExceptionHandler {

    /**
     * Called by a component when an exception occurred.
     * @param ex
     *            the exception that was thrown
     */
    void handleException(Throwable ex);
}
