package fr.itinerennes.exceptions;

/**
 * Implementing classes defines the application behavior when business errors occurs.
 * 
 * @author Jérémie Huchet
 */
public interface ExceptionHandler {

    /**
     * Called by a component when an exception occurred.
     * 
     * @param thread
     *            the thread that has an uncaught exception
     * @param ex
     *            the exception that was thrown
     */
    void handleException(Thread thread, Throwable ex);
}
