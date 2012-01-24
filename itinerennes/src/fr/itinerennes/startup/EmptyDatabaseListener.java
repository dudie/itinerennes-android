package fr.itinerennes.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Handler;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.R;
import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.Columns.MarkersColumns;

/**
 * Listener which checks if markers table is empty, and fill it if necessary.
 * 
 * @author Olivier Boudet
 */
public class EmptyDatabaseListener implements IStartupListener, MarkersColumns,
        AccessibilityColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyDatabaseListener.class);

    /** The context. */
    private final ItineRennesApplication context;

    /** Handler to notify progress. */
    private final Handler handler;

    /** Database instance. */
    private final SQLiteDatabase db;

    /**
     * Constructor.
     * 
     * @param context
     *            the application context.
     * @param progressHandler
     *            handler to use for progress messages
     */
    public EmptyDatabaseListener(final ItineRennesApplication context, final Handler progressHandler) {

        this.handler = progressHandler;
        this.context = context;
        this.db = context.getDatabaseHelper().getWritableDatabase();
    }

    /**
     * Returns true if {@link MarkersColumns#MARKERS_TABLE_NAME} is empty.
     * 
     * @return true if {@link MarkersColumns#MARKERS_TABLE_NAME} is empty
     * @see fr.itinerennes.startup.IStartupListener#isExecutionNeeded()
     */
    @Override
    public final boolean isExecutionNeeded() {

        final SQLiteStatement statement = db.compileStatement(String.format(
                "SELECT count(%s) FROM %s", MarkersColumns.ID, MARKERS_TABLE_NAME));
        final long markersCount = statement.simpleQueryForLong();
        statement.close();

        if (markersCount <= 0) {
            return true;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Database is not empty.");
        }

        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.startup.IStartupListener#execute()
     */
    @Override
    public final void execute() {

        new DataPreloader().start();

    }

    /**
     * Thread intended to preload the data.
     * 
     * @author Jérémie Huchet
     * @author Olivier Boudet
     */
    private class DataPreloader extends Thread {

        /**
         * Create the data preloader.
         */
        public DataPreloader() {

        }

        /**
         * Preloads the data and notifies the handler of the progression.
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("run.start");
            }

            // file reader for markers
            final BufferedReader readerMarkers = new BufferedReader(new InputStreamReader(context
                    .getResources().openRawResource(R.raw.markers)));

            // file reader for accessibility informations
            final BufferedReader readerAccessibility = new BufferedReader(new InputStreamReader(
                    context.getResources().openRawResource(R.raw.accessibility)));

            LOGGER.debug("Inserting markers in database...");
            final long debut = System.currentTimeMillis();

            try {

                String line = readerMarkers.readLine();
                int count = Integer.parseInt(line);

                line = readerAccessibility.readLine();
                count += Integer.parseInt(line);

                handler.sendMessage(handler.obtainMessage(
                        LoadingActivity.MSG_PROGRESS_INCREASE_MAX, count));

                insertMarkers(readerMarkers);
                insertAccessibility(readerAccessibility);

                if (db.inTransaction()) {
                    db.setTransactionSuccessful();
                }
            } catch (final IOException e) {
                context.getExceptionHandler().handleException(e);
                handler.sendMessage(handler.obtainMessage(LoadingActivity.MSG_PROGRESS_FAILED,
                        EmptyDatabaseListener.this));
            } finally {
                try {
                    readerAccessibility.close();
                    readerMarkers.close();
                } catch (final IOException e) {
                    LOGGER.debug("Can't close buffered readers !", e);
                }

                if (db.inTransaction()) {
                    db.endTransaction();
                }
            }

            final long fin = System.currentTimeMillis();
            LOGGER.debug(String.format("Markers inserted... Took %s ms", (fin - debut)));

            handler.sendMessage(handler.obtainMessage(LoadingActivity.MSG_PROGRESS_FINISH,
                    EmptyDatabaseListener.this));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("run.end");
            }
        }

        /**
         * Insert accessibility informations from a buffered reader into a database.
         * 
         * @param readerAccessibility
         *            buffered reader on accessibility csv file
         * @throws IOException
         *             if the accessibility file can not be read
         */
        private void insertAccessibility(final BufferedReader readerAccessibility)
                throws IOException {

            // insert helper for accessibility informations
            final InsertHelper insertAccessibilityHelper = new InsertHelper(db,
                    ACCESSIBILITY_TABLE_NAME);
            final int idAccessibilityColumn = insertAccessibilityHelper
                    .getColumnIndex(AccessibilityColumns.ID);
            final int typeAccessibilityColumn = insertAccessibilityHelper
                    .getColumnIndex(AccessibilityColumns.TYPE);
            final int wheelchairAccessibilityColumn = insertAccessibilityHelper
                    .getColumnIndex(AccessibilityColumns.WHEELCHAIR);

            // walking through accessibility file
            String line = null;
            while ((line = readerAccessibility.readLine()) != null) {
                final String[] fields = line.split(";");

                insertAccessibilityHelper.prepareForInsert();

                insertAccessibilityHelper.bind(idAccessibilityColumn, fields[0]);
                insertAccessibilityHelper.bind(typeAccessibilityColumn, fields[1]);
                insertAccessibilityHelper.bind(wheelchairAccessibilityColumn, 1);

                insertAccessibilityHelper.execute();
                handler.sendMessage(handler.obtainMessage(LoadingActivity.MSG_PROGRESS_INCREASE, 1));
            }

        }

        /**
         * Insert markers from a buffered reader into a database.
         * 
         * @param readerMarkers
         *            buffered reader on marker csv file
         * @throws IOException
         *             if the marker file can not be read
         */
        private void insertMarkers(final BufferedReader readerMarkers) throws IOException {

            if (!db.inTransaction()) {
                db.beginTransaction();
            }

            // insert helper for markers
            final InsertHelper insertMarkerHelper = new InsertHelper(db, MARKERS_TABLE_NAME);
            final int idMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.ID);
            final int typeMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.TYPE);
            final int latMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.LATITUDE);
            final int lonMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.LONGITUDE);
            final int labelMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.LABEL);
            final int searchLabelMarkerColumn = insertMarkerHelper
                    .getColumnIndex(MarkersColumns.SEARCH_LABEL);

            // walking through markers file
            String line = null;
            while ((line = readerMarkers.readLine()) != null) {
                if (!db.inTransaction()) {
                    db.beginTransaction();
                }
                final String[] fields = line.split(";");

                insertMarkerHelper.prepareForInsert();

                insertMarkerHelper.bind(typeMarkerColumn, fields[0]);
                insertMarkerHelper.bind(idMarkerColumn, fields[1]);
                insertMarkerHelper.bind(latMarkerColumn,
                        (int) (Double.parseDouble(fields[2]) * 1E6));
                insertMarkerHelper.bind(lonMarkerColumn,
                        (int) (Double.parseDouble(fields[3]) * 1E6));
                insertMarkerHelper.bind(labelMarkerColumn, fields[4]);
                insertMarkerHelper.bind(searchLabelMarkerColumn, fields[5]);

                insertMarkerHelper.execute();
                handler.sendMessage(handler.obtainMessage(LoadingActivity.MSG_PROGRESS_INCREASE, 1));
            }

        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.startup.IStartupListener#isInBackground()
     */
    @Override
    public final boolean isInBackground() {

        return false;
    }

}
