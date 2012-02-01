package fr.itinerennes.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.acra.ErrorReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.R;
import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.startup.LoadingActivity.ProgressObserver;

/**
 * Listener which checks if markers table is empty, and fill it if necessary.
 * 
 * @author Olivier Boudet
 */
public final class EmptyDatabaseListener extends AbstractStartupListener implements MarkersColumns,
        AccessibilityColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyDatabaseListener.class);

    /** The context. */
    private final ItineRennesApplication context;

    /** Database instance. */
    private final SQLiteDatabase db;

    /**
     * Constructor.
     * 
     * @param context
     *            the application context
     * @param listener
     *            the observer to notify about progression
     */
    public EmptyDatabaseListener(final ItineRennesApplication context,
            final ProgressObserver listener) {

        super(listener);
        this.context = context;
        this.db = context.getDatabaseHelper().getWritableDatabase();
    }

    /**
     * Reads the first line of the markers.csv file to determine the total number of markers to
     * insert.
     * 
     * @return the total amount of markers to insert
     */
    @Override
    public int progressCount() {

        final BufferedReader readerMarkers = new BufferedReader(new InputStreamReader(context
                .getResources().openRawResource(R.raw.markers)));
        int count = 0;
        try {
            count = Integer.parseInt(readerMarkers.readLine());
        } catch (final NumberFormatException e) {
            LOGGER.error("unable to parse the markers's file first line", e);
        } catch (final IOException e) {
            LOGGER.error("unable to read markers's file first line", e);
        } finally {
            try {
                readerMarkers.close();
            } catch (final IOException e) {
                LOGGER.error("unable to close stream propertly", e);
            }
        }
        return count;
    }

    /**
     * Checks if marker/stations is required then loads them into the database if necessary.
     */
    @Override
    public void execute() {

        if (isExecutionNeeded()) {
            LOGGER.info("Inserting stations...");
            doInsertMarkers();
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Markers seems to be already inserted");
        }
    }

    /**
     * Returns true if {@link MarkersColumns#MARKERS_TABLE_NAME} is empty.
     * 
     * @return true if {@link MarkersColumns#MARKERS_TABLE_NAME} is empty
     */
    private boolean isExecutionNeeded() {

        final SQLiteStatement statement = db.compileStatement(String.format(
                "SELECT count(%s) FROM %s", MarkersColumns.ID, MARKERS_TABLE_NAME));
        final long markersCount = statement.simpleQueryForLong();
        statement.close();

        if (markersCount <= 0) {
            return true;
        } else {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Database is not empty.");
            }

            return false;
        }
    }

    /**
     * Loads markers/stations into the database.
     */
    public void doInsertMarkers() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInsertMarkers.start");
        }

        // file reader for markers
        final BufferedReader readerMarkers = new BufferedReader(new InputStreamReader(context
                .getResources().openRawResource(R.raw.markers)));

        // file reader for accessibility informations
        final BufferedReader readerAccessibility = new BufferedReader(new InputStreamReader(context
                .getResources().openRawResource(R.raw.accessibility)));

        LOGGER.debug("Inserting markers in database...");
        final long debut = System.currentTimeMillis();

        try {

            // skip header line with total line number
            readerMarkers.readLine();
            readerAccessibility.readLine();

            insertMarkers(readerMarkers);
            insertAccessibility(readerAccessibility);

            if (db.inTransaction()) {
                db.setTransactionSuccessful();
            }
        } catch (final IOException e) {
            ErrorReporter.getInstance().handleSilentException(e);
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

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInsertMarkers.end");
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
    private void insertAccessibility(final BufferedReader readerAccessibility) throws IOException {

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
            publishProgress(1);
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
            insertMarkerHelper.bind(latMarkerColumn, (int) (Double.parseDouble(fields[2]) * 1E6));
            insertMarkerHelper.bind(lonMarkerColumn, (int) (Double.parseDouble(fields[3]) * 1E6));
            insertMarkerHelper.bind(labelMarkerColumn, fields[4]);
            insertMarkerHelper.bind(searchLabelMarkerColumn, fields[5]);

            insertMarkerHelper.execute();
            publishProgress(1);
        }

    }

}
