package fr.itinerennes.database;

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

import java.util.Iterator;

/**
 * An interface providing a way to iterate over data to import into a DB table.
 * 
 * @author Jérémie Huchet
 */
public interface IDataReader extends Iterator<String[]> {

    /**
     * Gets the total amount of rows.
     * 
     * @return the row count
     */
    int getRowCount();

    /**
     * Gets the name of the table to which the read data should be imported.
     * 
     * @return the table name
     */
    String getTable();

    /**
     * Gets the name of the columns of the table.
     * 
     * @return the columns names
     */
    String[] getColumns();
}
