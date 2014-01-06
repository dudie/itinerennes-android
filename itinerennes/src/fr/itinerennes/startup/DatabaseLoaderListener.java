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

import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.database.IDataReader;
import fr.itinerennes.startup.LoadingActivity.ProgressObserver;

/**
 * Load data into the database at startup.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */
public final class DatabaseLoaderListener extends AbstractStartupListener implements
        MarkersColumns, AccessibilityColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseLoaderListener.class);

    /** Database instance. */
    private final SQLiteDatabase db;

    /** The source data reader. */
    private final IDataReader reader;

    /**
     * Constructor.
     * 
     * @param context
     *            the application context
     * @param listener
     *            the observer to notify about progression
     * @param reader
     *            the CSV data reader to use to get input data
     */
    public DatabaseLoaderListener(final ItineRennesApplication context,
            final ProgressObserver listener, final IDataReader reader) {

        super(listener);
        this.db = context.getDatabaseHelper().getWritableDatabase();
        this.reader = reader;
    }

    /**
     * Gets the total amount of rows to insert.
     * 
     * @return the total amount of rows to insert
     */
    @Override
    public int progressCount() {

        return reader.getRowCount();
    }

    /**
     * Checks if loading data is required then loads rows into the database if necessary.
     */
    @Override
    public void execute() {

        if (isExecutionNeeded()) {

            LOGGER.debug("Inserting data in table {}", reader.getTable());
            final long start = System.currentTimeMillis();

            insertData();

            final long end = System.currentTimeMillis();
            LOGGER.debug("Data inserted in table {} in {} ms", reader.getTable(), (end - start));

        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Data seems to be already inserted in table {}", reader.getTable());
        }
    }

    /**
     * Returns true if {@link MarkersColumns#MARKERS_TABLE_NAME} is empty.
     * 
     * @return true if {@link MarkersColumns#MARKERS_TABLE_NAME} is empty
     */
    private boolean isExecutionNeeded() {

        final SQLiteStatement statement = db.compileStatement(String.format(
                "SELECT count(%s) FROM %s", BaseColumns._ID, reader.getTable()));
        final long count = statement.simpleQueryForLong();
        statement.close();

        if (count <= 0) {
            return true;
        } else {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Table {} is not empty", reader.getTable());
            }

            return false;
        }
    }

    /**
     * Insert data into the database.
     */
    private void insertData() {

        final String[] columnNames = reader.getColumns();
        final int[] columnIndexes = new int[columnNames.length];

        // setup insert helper for data
        final InsertHelper insertHelper = new InsertHelper(db, reader.getTable());
        for (int i = 0; i < columnNames.length; i++) {
            columnIndexes[i] = insertHelper.getColumnIndex(columnNames[i]);
        }

        try {
            // walking through data to insert
            while (reader.hasNext()) {
                final String[] data = reader.next();

                insertHelper.prepareForInsert();
                for (int i = 0; i < columnIndexes.length; i++) {
                    insertHelper.bind(columnIndexes[i], data[i]);
                }

                if (!db.inTransaction()) {
                    db.beginTransaction();
                }
                insertHelper.execute();
                publishProgress(1);
            }

            if (db.inTransaction()) {
                db.setTransactionSuccessful();
            }
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
        }

    }

}
