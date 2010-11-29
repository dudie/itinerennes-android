package fr.itinerennes.exceptions;

import fr.itinerennes.ErrorCodeConstants;

/**
 * An abstract exception defining the error code describing the problem.
 * 
 * @author Jérémie Huchet
 */
public class GenericException extends Exception {

    /** The error code. */
    private final short code;

    /**
     * Creates a new generic exception.
     * 
     * @param code
     *            the error code (see {@link ErrorCodeConstants}
     * @param message
     *            an optional description message of the error
     * @param cause
     *            an optional cause
     */
    public GenericException(final short code, final String message, final Throwable cause) {

        super(message, cause);
        this.code = code;
    }

    /**
     * Creates a new generic exception.
     * 
     * @param code
     *            the error code (see {@link ErrorCodeConstants}
     * @param message
     *            an optional description message of the error
     */
    public GenericException(final short code, final String message) {

        this(code, message, null);
    }

    /**
     * Creates a new generic exception.
     * 
     * @param code
     *            the error code (see {@link ErrorCodeConstants}
     */
    public GenericException(final short code) {

        this(code, null, null);
    }

    /**
     * Gets the code.
     * 
     * @return the code
     */
    public short getCode() {

        return code;
    }
}
