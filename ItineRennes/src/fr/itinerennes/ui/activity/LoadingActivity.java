package fr.itinerennes.ui.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.content.Intent;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.model.VersionCheck;
import fr.itinerennes.utils.VersionUtils;
import fr.itinerennes.utils.xml.XmlVersionParser;

/**
 * @author Jérémie Huchet
 */
public class LoadingActivity extends ItineRennesActivity implements MarkersColumns,
        AccessibilityColumns {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(LoadingActivity.class);

    /** Constant identifying the "failure" dialog. */
    private static final int DIALOG_FAILURE = 1;

    /** Handler message specifying the preload has started. */
    private static final int MSG_PRELOAD_START = 0;

    /** Handler message specifying the preload has progressed. */
    private static final int MSG_PRELOAD_PROGRESS = 1;

    /** Handler message specifying the preload has finished successfully. */
    private static final int MSG_PRELOAD_SUCCESS = 2;

    /** Handler message specifying the preload has failed. */
    private static final int MSG_PRELOAD_FAILED = -1;

    /** The progress bar displaying the preload progression state. */
    private ProgressBar progressBar;

    /** The handler to handle progress messages in the UI thread. */
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(final android.os.Message msg) {

            switch (msg.what) {
            case MSG_PRELOAD_START:
                setContentView(R.layout.act_loading);

                progressBar = (ProgressBar) findViewById(R.id.activity_preload_progress_bar);
                progressBar.setProgress(0);
                progressBar.setMax((Integer) msg.obj);
                break;
            case MSG_PRELOAD_PROGRESS:
                progressBar.setProgress(progressBar.getProgress() + (Integer) msg.obj);
                break;
            case MSG_PRELOAD_SUCCESS:
                checkVersion();
                final Intent i = new Intent(getBaseContext(), MapActivity.class);
                startActivity(i);
                finish();
                break;
            case MSG_PRELOAD_FAILED:
                progressBar.setIndeterminate(true);
                showDialog(DIALOG_FAILURE);
                break;

            default:
                break;
            }
        };
    };

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected final void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }

        new DataPreloader(handler).start();

        super.onCreate(savedInstanceState);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * Thread intended to preload the data.
     * 
     * @author Jérémie Huchet
     * @author Olivier Boudet
     */
    private class DataPreloader extends Thread {

        /** The handler to notify progress. */
        private final Handler handler;

        /**
         * Create the data preloader.
         * 
         * @param handler
         *            the handler to notify on progress update
         */
        public DataPreloader(final Handler handler) {

            this.handler = handler;
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

            final SQLiteDatabase db = getApplicationContext().getDatabaseHelper()
                    .getWritableDatabase();

            SQLiteStatement statement = db.compileStatement(String.format(
                    "SELECT count(%s) FROM %s", MarkersColumns.ID, MARKERS_TABLE_NAME));
            final long markersCount = statement.simpleQueryForLong();
            statement.close();

            statement = db.compileStatement(String.format("SELECT count(%s) FROM %s",
                    AccessibilityColumns.ID, ACCESSIBILITY_TABLE_NAME));
            final long accessibilityCount = statement.simpleQueryForLong();
            statement.close();

            // file reader for markers
            final BufferedReader readerMarkers = new BufferedReader(new InputStreamReader(
                    getResources().openRawResource(R.raw.markers)));

            // file reader for accessibility informations
            final BufferedReader readerAccessibility = new BufferedReader(new InputStreamReader(
                    getResources().openRawResource(R.raw.accessibility)));

            LOGGER.debug("Inserting markers in database...");
            final long debut = System.currentTimeMillis();

            try {

                String line = readerMarkers.readLine();
                int count = Integer.parseInt(line);

                line = readerAccessibility.readLine();
                count += Integer.parseInt(line);

                // si l'une ou l'autre des tables de markers ou d'accessibility est vide, on affiche
                // la barre de progression
                if (markersCount <= 0 || accessibilityCount <= 0) {
                    handler.sendMessage(handler.obtainMessage(MSG_PRELOAD_START, count));
                }

                // si la table de markers est vide, on la remplit
                if (markersCount <= 0) {
                    insertMarkers(db, readerMarkers);
                }

                // si la table d'accessibility est vide, on la remplit
                if (accessibilityCount <= 0) {
                    insertAccessibility(db, readerAccessibility);
                }

                if (db.inTransaction()) {
                    db.setTransactionSuccessful();
                }
            } catch (final IOException e) {
                getApplicationContext().getExceptionHandler().handleException(e);
                handler.sendMessage(handler.obtainMessage(MSG_PRELOAD_FAILED));
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

            handler.sendMessage(handler.obtainMessage(MSG_PRELOAD_SUCCESS));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("run.end");
            }
        }

        /**
         * Insert accessibility informations from a buffered reader into a database.
         * 
         * @param db
         *            database in which insert accessibility
         * @param readerAccessibility
         *            buffered reader on accessibility csv file
         * @throws IOException
         *             if the accessibility file can not be read
         */
        private void insertAccessibility(final SQLiteDatabase db,
                final BufferedReader readerAccessibility) throws IOException {

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
                handler.sendMessage(handler.obtainMessage(MSG_PRELOAD_PROGRESS, 1));
            }

        }

        /**
         * Insert markers from a buffered reader into a database.
         * 
         * @param db
         *            database in which insert markers
         * @param readerMarkers
         *            buffered reader on marker csv file
         * @throws IOException
         *             if the marker file can not be read
         */
        private void insertMarkers(final SQLiteDatabase db, final BufferedReader readerMarkers)
                throws IOException {

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
                handler.sendMessage(handler.obtainMessage(MSG_PRELOAD_PROGRESS, 1));
            }

        }
    }

    /**
     * Fetches XML describing Itinerennes versions sends an intent to the current top activity in
     * order to display an error message if necessary.
     */
    protected final void checkVersion() {

        new AsyncTask<Void, Void, Intent>() {

            @Override
            protected Intent doInBackground(final Void... params) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("start checking version");
                }

                Intent i = null;
                final HttpClient httpClient = getApplicationContext().getHttpClient();
                final HttpGet request = new HttpGet();
                try {
                    request.setURI(new URI(ItineRennesConstants.ITINERENNES_VERSION_URL));
                    final HttpResponse response = httpClient.execute(request);
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                        final XmlVersionParser parser = new XmlVersionParser();

                        final VersionCheck versionCheck = parser.parse(response.getEntity()
                                .getContent());

                        final String currentVersion = getString(R.string.version_number);

                        final int comparisonMinRequired = VersionUtils.compare(currentVersion,
                                versionCheck.getMinRequired());
                        final int comparisonLatest = VersionUtils.compare(currentVersion,
                                versionCheck.getLatest());

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug(String
                                    .format("Current version : %s - Latest version : %s - Minimum required version : %s",
                                            currentVersion, versionCheck.getLatest(),
                                            versionCheck.getMinRequired()));
                        }

                        if (comparisonMinRequired < 0 || comparisonLatest < 0) {

                            i = new Intent();
                            i.setAction(NewVersionActivity.INTENT_UPGRADE);

                            if (comparisonMinRequired < 0) {
                                // minimum version required is greater than the current version
                                i.putExtra(NewVersionActivity.INTENT_EXTRA_MANDATORY_UPGRADE, true);

                            } else if (comparisonLatest < 0) {
                                // the latest available version is greater than the current version
                                i.putExtra(NewVersionActivity.INTENT_EXTRA_MANDATORY_UPGRADE, false);
                            }
                        }

                    }
                } catch (final Exception e) {
                    LOGGER.warn("Can not get version informations.");
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("end checking version.");
                }

                return i;
            }

            @Override
            protected void onPostExecute(final Intent intent) {

                if (intent != null) {

                    sendBroadcast(intent);

                }
            }
        }.execute();

    }

}
