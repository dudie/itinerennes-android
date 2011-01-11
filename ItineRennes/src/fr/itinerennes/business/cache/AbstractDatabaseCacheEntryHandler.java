package fr.itinerennes.business.cache;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import fr.itinerennes.database.DatabaseHelper;

/**
 * Common operations for all handlers using database.
 * 
 * @author Jérémie Huchet
 */
public abstract class AbstractDatabaseCacheEntryHandler {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(AbstractDatabaseCacheEntryHandler.class);

    /** The database helper to use to open the database. */
    protected final DatabaseHelper dbHelper;

    /**
     * Initializes the handler.
     * 
     * @param dbHelper
     *            the database helper
     */
    public AbstractDatabaseCacheEntryHandler(final DatabaseHelper dbHelper) {

        this.dbHelper = dbHelper;
    }
}
