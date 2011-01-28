package fr.itinerennes.exceptions;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.widget.Toast;

import fr.itinerennes.R;

/**
 * A simple exception handler which logs exceptions and displays a toast message to the user.
 * 
 * @author Jérémie Huchet
 */
public class DefaultExceptionHandler implements ExceptionHandler {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(DefaultExceptionHandler.class);

    /** The duration of the displayed toast message when an error occurs. */
    private static final int ERROR_TOAST_DURATION = 15;

    /** The context. */
    private final Context context;

    /**
     * Creates the defautl exception handler.
     * 
     * @param context
     *            the context
     */
    public DefaultExceptionHandler(final Context context) {

        this.context = context;
    }

    /**
     * Logs the exception.
     * <p>
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.exceptions.ExceptionHandler#handleException(java.lang.Throwable)
     */
    @Override
    public final void handleException(final Throwable ex) {

        final String msg = context.getResources().getString(R.string.error_network_connectivity);
        Toast.makeText(context, msg, ERROR_TOAST_DURATION).show();
        LOGGER.error("an error occurred", ex);
    }
}