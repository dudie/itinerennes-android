package fr.itinerennes.exceptions;

/**
 * Base class for all unchecked exceptions thrown by the ItinéRennes application.
 * 
 * @author Jérémie Huchet
 */
public class ItineRennesRuntimeException extends RuntimeException {

    /** The serial version UID. */
    private static final long serialVersionUID = -5474219145948525318L;

    /**
     * Constructor.
     * 
     * @param message
     *            the error description
     * @param cause
     *            the exception cause
     */
    public ItineRennesRuntimeException(final String message, final Throwable cause) {

        super(message, cause);
    }

    /**
     * Constructor.
     * 
     * @param message
     *            the error description
     */
    public ItineRennesRuntimeException(final String message) {

        super(message, null);
    }
}
