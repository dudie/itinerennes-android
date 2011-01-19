package fr.itinerennes.exceptions;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

/**
 * A simple exception handler which logs exceptions.
 * 
 * @author Jérémie Huchet
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(DefaultExceptionHandler.class);

    /**
     * Logs the exception.
     * <p>
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.exceptions.ExceptionHandler#handleException(java.lang.Thread,
     *      java.lang.Throwable)
     */
    @Override
    public final void handleException(final Thread thread, final Throwable ex) {

        Thread.currentThread().getUncaughtExceptionHandler();
        LOGGER.error("an error occurred in thread " + thread.getName(), ex);
    }
}
