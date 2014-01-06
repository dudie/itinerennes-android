package fr.itinerennes.business.service;

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
