package fr.itinerennes.business.service;

import fr.itinerennes.database.DatabaseHelper;

/**
 * Common operations for all services.
 * 
 * @author Jérémie Huchet
 */
public abstract class AbstractService {

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
