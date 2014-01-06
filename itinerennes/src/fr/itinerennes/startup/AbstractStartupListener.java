package fr.itinerennes.startup;

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

import fr.itinerennes.startup.LoadingActivity.ProgressObserver;

/**
 * Interface definition for a callback to be invoked when the application starts up.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */
public abstract class AbstractStartupListener {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStartupListener.class);

    /** An (optional) observer listening on progress events. */
    private final ProgressObserver observer;

    /**
     * Default constructor.
     */
    public AbstractStartupListener() {

        this(null);
    }

    /**
     * Constructor.
     * 
     * @param observer
     *            an (optional) observer of progress events
     */
    public AbstractStartupListener(final ProgressObserver observer) {

        this.observer = observer;
        if (null == observer) {
            LOGGER.warn("No observer attached to {}", this.getClass().getSimpleName());
        }
    }

    /**
     * This will be run in background before starting the {@link #execute()} job. This is intended
     * to compute/estimate the total length of the work which will be done by {@link #execute()}.
     * <p>
     * Even if this is run in background, execution should be as quick as possible.
     * 
     * @return the total amount of progress units needed to complete the task
     */
    public abstract int progressCount();

    /**
     * This is run in background.
     */
    public abstract void execute();

    /**
     * Notifies the observer that the background process has progressed.
     * 
     * @param progress
     *            the amount of progress units which have been processed
     */
    public final void publishProgress(final int progress) {

        if (null != observer) {
            observer.publishProgress(progress);
        }
    }
}
