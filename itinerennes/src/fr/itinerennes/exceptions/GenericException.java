package fr.itinerennes.exceptions;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

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
