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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import fr.itinerennes.R;

/**
 * A simple exception handler which logs exceptions and displays a toast message to the user.
 * 
 * @author Jérémie Huchet
 */
public class DefaultExceptionHandler extends Handler implements ExceptionHandler {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("handleException from Thread {} : {}", Thread.currentThread().getName(),
                    ex.getMessage());
        }
        post(new Runnable() {

            @Override
            public void run() {

                final String msg = context.getResources().getString(
                        R.string.error_network_connectivity);
                Toast.makeText(context, msg, ERROR_TOAST_DURATION).show();
                LOGGER.error("an error occurred", ex);
            }
        });
    }
}
