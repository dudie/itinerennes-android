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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;

import fr.itinerennes.R;
import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.database.Columns.RoutesStopsColumns;

/**
 * Base class to implement a {@link IDataReader} based on a CSV file with the total line count on
 * the first line.
 * <p>
 * Sample:
 * 
 * <pre>
 * 3
 * one;first value
 * two;second value
 * three;third and last value;
 * </pre>
 * 
 * @author Jérémie Huchet
 */
public final class CSVDataReader implements IDataReader {

    /** The table name. */
    private final String tableName;

    /** The column names. */
    private final String[] columnNames;

    /** A reader to access the file. */
    private final BufferedReader reader;

    /**
     * The total line count found on the file's first line (the total count excludes the first
     * line).
     */
    private final int lineCount;

    /** The next line value or null if the end of the file has been reached. */
    private String nextLine;

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     * @param rawResId
     *            the identifier of the raw resource to read
     * @param tableName
     *            the table name.
     * @param columnNames
     *            the columns names
     */
    public CSVDataReader(final Context context, final int rawResId, final String tableName,
            final String[] columnNames) {

        this.tableName = tableName;
        this.columnNames = columnNames;
        reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(
                rawResId)));
        try {
            lineCount = Integer.parseInt(reader.readLine());
            nextLine = reader.readLine();
        } catch (final IOException e) {
            throw new RuntimeException("Unable to initialize the CSV reader", e);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.database.IDataReader#getRowCount()
     */
    @Override
    public int getRowCount() {

        return lineCount;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.database.IDataReader#getTable()
     */
    @Override
    public String getTable() {

        return tableName;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.database.IDataReader#getColumns()
     */
    @Override
    public String[] getColumns() {

        return columnNames;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {

        return nextLine != null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Iterator#next()
     */
    @Override
    public String[] next() {

        final String[] next = nextLine.split(";", -1);
        try {
            nextLine = reader.readLine();
        } catch (final IOException e) {
            throw new RuntimeException("Unable to read CSV data", e);
        }
        return next;
    }

    /**
     * Throws an UnsupportedOperationException everytime.
     * 
     * @see java.util.Iterator#remove()
     * @throws UnsupportedOperationException
     */
    @Override
    public void remove() {

        throw new UnsupportedOperationException("Can't remove data from initial dataset");
    }

    /**
     * Get a reader for markers initial dataset.
     * 
     * @param context
     *            the context
     * @return a reader for markers
     */
    public static CSVDataReader markers(final Context context) {

        // CSV file format:
        // BUS;2_1001;48.12903716;-1.632507902;Longs Champs;longschamps
        // type;id;lat;lon;label;search_label;city
        final String[] columns = new String[] { MarkersColumns.TYPE, MarkersColumns.ID,
                MarkersColumns.LATITUDE, MarkersColumns.LONGITUDE, MarkersColumns.LABEL,
                MarkersColumns.SEARCH_LABEL, MarkersColumns.CITY };

        return new CSVDataReader(context, R.raw.markers, MarkersColumns.MARKERS_TABLE_NAME, columns);
    }

    /**
     * Get a reader for accessibility initial dataset.
     * 
     * @param context
     *            the context
     * @return a reader for accessibility
     */
    public static CSVDataReader accessibility(final Context context) {

        // CSV file format:
        // id;type;wheelchair
        final String[] columns = new String[] { AccessibilityColumns.ID, AccessibilityColumns.TYPE,
                AccessibilityColumns.WHEELCHAIR };

        return new CSVDataReader(context, R.raw.accessibility,
                AccessibilityColumns.ACCESSIBILITY_TABLE_NAME, columns);
    }

    /**
     * Get a reader for routes and stops relationships initial dataset.
     * 
     * @param context
     *            the context
     * @return a reader for routes and stops relationships
     */
    public static CSVDataReader routesStops(final Context context) {

        // CSV file format:
        // route_id;stop_id
        final String[] columns = new String[] { RoutesStopsColumns.ROUTE_ID,
                RoutesStopsColumns.STOP_ID };

        return new CSVDataReader(context, R.raw.routes_stops,
                RoutesStopsColumns.ROUTES_STOPS_TABLE_NAME, columns);
    }
}
