package fr.itinerennes.business.facade;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.database.DatabaseHelper;

/**
 * Common operations for all services.
 * 
 * @author Jérémie Huchet
 */
public abstract class AbstractService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(AbstractService.class);

    /** The database helper to use to open the database. */
    protected final DatabaseHelper dbHelper;

    /**
     * Initializes the handler.
     * 
     * @param dbHelper
     *            the database helper
     */
    public AbstractService(final DatabaseHelper dbHelper) {

        this.dbHelper = dbHelper;
    }
}
