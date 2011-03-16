package fr.itinerennes.ui.activity;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import fr.itinerennes.R;
import fr.itinerennes.exceptions.GenericException;

/**
 * @author Jérémie Huchet
 */
public class PreloadActivity extends ItinerennesContext {

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

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("handleMessage - msg={}", msg.what);
            }

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

            handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_START, 5));
            try {
                getBusService().getStations(BBOX_WORLD);
                handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_PROGRESS, 3));
                getBikeService().getStations(BBOX_WORLD);
                handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_PROGRESS, 1));
                getSubwayService().getStations(BBOX_WORLD);
                handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_PROGRESS, 1));
            } catch (final GenericException e) {
                getExceptionHandler().handleException(e);
                handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_FAILED));
            }
            handler.sendMessage(handler.obtainMessage(MSG_DOWNLOAD_SUCCESS));

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("run.end");
            }
        }
    }
}
