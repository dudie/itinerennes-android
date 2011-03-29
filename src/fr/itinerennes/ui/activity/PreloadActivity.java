package fr.itinerennes.ui.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import fr.itinerennes.R;
import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.Columns.MarkersColumns;

/**
 * @author Jérémie Huchet
 */
public class PreloadActivity extends ItinerennesContext implements MarkersColumns,
        AccessibilityColumns {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(PreloadActivity.class);

    /** Identifier for the "preload" dialog. */
    private static final int DIALOG_PRELOAD = 0;

    /** Constant identifying the "failure" dialog. */
    private static final int DIALOG_FAILURE = 1;

    /** Handler message specifying the preload has started. */
    private static final int MSG_DOWNLOAD_START = 0;

    /** Handler message specifying the preload has progressed. */
    private static final int MSG_DOWNLOAD_PROGRESS = 1;

    /** Handler message specifying the preload has finished successfully. */
    private static final int MSG_DOWNLOAD_SUCCESS = 2;

    /** Handler message specifying the preload has failed. */
    private static final int MSG_DOWNLOAD_FAILED = -1;

    /** The bounding box containing the whole world. */
    private static final BoundingBoxE6 BBOX_WORLD = new BoundingBoxE6(180000000, 180000000,
            -180000000, -180000000);

    /** The radio button the user checks to do the preload. */
    private RadioButton radioPreload;

    /** The radio button the user checks to don't preload. */
    private RadioButton radioDontPreload;

    /** The progress bar displaying the prelod progression state. */
    private ProgressBar dialogProgressBar;

    /** The handler to handle progress messages in the UI thread. */
    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(final android.os.Message msg) {

            switch (msg.what) {
            case MSG_DOWNLOAD_START:
                dialogProgressBar.setProgress(0);
                dialogProgressBar.setMax((Integer) msg.obj);
                break;
            case MSG_DOWNLOAD_PROGRESS:
                dialogProgressBar.setProgress(dialogProgressBar.getProgress() + (Integer) msg.obj);
                break;
            case MSG_DOWNLOAD_SUCCESS:
                dismissDialogIfDisplayed(DIALOG_PRELOAD);
                setResult(RESULT_OK);
                finish();
                break;
            case MSG_DOWNLOAD_FAILED:
                dialogProgressBar.setIndeterminate(true);
                dismissDialogIfDisplayed(DIALOG_PRELOAD);
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        radioPreload = (RadioButton) findViewById(R.id.activity_preload_radio_preload);
        radioDontPreload = (RadioButton) findViewById(R.id.activity_preload_radio_dont_preload);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * Triggered when user clicks on the text describing the preload function.
     * 
     * @param target
     *            the clicked view
     */
    public final void onSelectRadioPreload(final View target) {

        radioPreload.setChecked(true);
    }

    /**
     * Triggered when user clicks on the text describing the don't preload function.
     * 
     * @param target
     *            the clicked view
     */
    public final void onSelectRadioNoPreload(final View target) {

        radioDontPreload.setChecked(true);
    }

    /**
     * Triggered when user clicks on the 'continue' button.
     * 
     * @param target
     *            the clicked view
     */
    public final void onDoOrDontPreload(final View target) {

        if (radioDontPreload.isChecked()) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            showDialog(DIALOG_PRELOAD);
            new DataPreloader(handler).start();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected final Dialog onCreateDialog(final int id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreateDialog.start - id={}", id);
        }
        Dialog d = null;
        switch (id) {
        // the preload dialog
        case DIALOG_PRELOAD:
            final AlertDialog.Builder progressBuilder = new AlertDialog.Builder(this);
            progressBuilder.setTitle(R.string.loading);
            final View progressView = getLayoutInflater().inflate(R.layout.progress_dialog, null);
            dialogProgressBar = (ProgressBar) progressView.findViewById(R.id.progress_bar);
            progressBuilder.setView(progressView);
            progressBuilder.setCancelable(false);
            d = progressBuilder.create();
            break;

        // the failure dialog
        case DIALOG_FAILURE:
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_network);
            builder.setCancelable(true);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(final DialogInterface dialog, final int id) {

                    dialog.dismiss();
                }
            });
            d = builder.create();
            break;
        default:
            break;
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreateDialog.end - id={}", id);
        }
        return d;
    }

    /**
     * Thread intended to preload the data.
     * 
     * @author Jérémie Huchet
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

            final SQLiteDatabase db = getDatabaseHelper().getWritableDatabase();

            // insert helper for markers
            final InsertHelper insertMarkerHelper = new InsertHelper(db, MARKERS_TABLE_NAME);
            final int idMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.ID);
            final int typeMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.TYPE);
            final int latMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.LATITUDE);
            final int lonMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.LONGITUDE);
            final int labelMarkerColumn = insertMarkerHelper.getColumnIndex(MarkersColumns.LABEL);

            // insert helper for accessibility informations
            final InsertHelper insertAccessibilityHelper = new InsertHelper(db,
                    ACCESSIBILITY_TABLE_NAME);
            final int idAccessibilityColumn = insertAccessibilityHelper
                    .getColumnIndex(AccessibilityColumns.ID);
            final int typeAccessibilityColumn = insertAccessibilityHelper
                    .getColumnIndex(AccessibilityColumns.TYPE);
            final int wheelchairAccessibilityColumn = insertAccessibilityHelper
                    .getColumnIndex(AccessibilityColumns.WHEELCHAIR);

            // file reader for markers
            final InputStream isMarker = getResources().openRawResource(R.raw.markers);
            final BufferedReader readerMarkers = new BufferedReader(new InputStreamReader(isMarker));

            // file reader for accessibility informations
            final InputStream isAccessibility = getResources().openRawResource(R.raw.accessibility);
            final BufferedReader readerAccessibility = new BufferedReader(new InputStreamReader(
                    isAccessibility));

            LOGGER.debug("Inserting markers in database...");
            final long debut = System.currentTimeMillis();

            db.beginTransaction();
            try {

                String line = readerMarkers.readLine();
                int count = Integer.parseInt(line);

                line = readerAccessibility.readLine();
                count += Integer.parseInt(line);

                handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_START, count));

                // walking through markers file
                while ((line = readerMarkers.readLine()) != null) {
                    final String[] fields = line.split(";");

                    insertMarkerHelper.prepareForInsert();

                    insertMarkerHelper.bind(typeMarkerColumn, fields[0]);
                    insertMarkerHelper.bind(idMarkerColumn, fields[1]);
                    insertMarkerHelper.bind(latMarkerColumn,
                            (int) (Double.parseDouble(fields[2]) * 1E6));
                    insertMarkerHelper.bind(lonMarkerColumn,
                            (int) (Double.parseDouble(fields[3]) * 1E6));
                    insertMarkerHelper.bind(labelMarkerColumn, fields[4]);

                    insertMarkerHelper.execute();
                    handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_PROGRESS, 1));
                }

                // walking through accessibility file
                while ((line = readerAccessibility.readLine()) != null) {
                    final String[] fields = line.split(";");

                    insertAccessibilityHelper.prepareForInsert();

                    insertAccessibilityHelper.bind(idAccessibilityColumn, fields[0]);
                    insertAccessibilityHelper.bind(typeAccessibilityColumn, fields[1]);
                    insertAccessibilityHelper.bind(wheelchairAccessibilityColumn, 1);

                    insertAccessibilityHelper.execute();
                    handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_PROGRESS, 1));
                }

                db.setTransactionSuccessful();
            } catch (final IOException e) {
                try {
                    isMarker.close();
                    isAccessibility.close();
                } catch (final IOException e1) {
                    getExceptionHandler().handleException(e);
                    handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_FAILED));
                }
                getExceptionHandler().handleException(e);
                handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_FAILED));
            } finally {
                try {
                    isMarker.close();
                    isAccessibility.close();
                } catch (final IOException e) {
                    LOGGER.debug("Can't close file input stream !", e);
                }

                db.endTransaction();
            }

            final long fin = System.currentTimeMillis();
            LOGGER.debug(String.format("Markers inserted... Took %s ms", (fin - debut)));

            handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_SUCCESS));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("run.end");
            }
        }
    }
}
