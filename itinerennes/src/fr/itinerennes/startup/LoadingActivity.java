package fr.itinerennes.startup;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import fr.itinerennes.R;
import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.Columns.MarkersColumns;
import fr.itinerennes.ui.activity.ItineRennesActivity;

/**
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class LoadingActivity extends ItineRennesActivity implements MarkersColumns,
        AccessibilityColumns {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoadingActivity.class);

    /** Constant identifying the "failure" dialog. */
    private static final int DIALOG_FAILURE = 1;

    /** Handler message to increase the progress bar max. */
    public static final int MSG_PROGRESS_INCREASE_MAX = 0;

    /** Handler message to increase the progress status. */
    public static final int MSG_PROGRESS_INCREASE = 1;

    /** Handler message when progress is done. */
    public static final int MSG_PROGRESS_FINISH = 2;

    /** Handler message specifying the preload has failed. */
    public static final int MSG_PROGRESS_FAILED = -1;

    /** The progress bar displaying the preload progression state. */
    private ProgressBar progressBar;

    /** List of listeners to invoke when application is starting up. */
    private final List<IStartupListener> startupListeners = new ArrayList<IStartupListener>();

    /** List of running listeners. */
    private final List<IStartupListener> runningListeners = new ArrayList<IStartupListener>();

    /** The handler to handle progress messages in the UI thread. */
    private final Handler progressHandler = new Handler() {

        @Override
        public void handleMessage(final android.os.Message msg) {

            switch (msg.what) {
            case MSG_PROGRESS_INCREASE_MAX:
                progressBar.setMax(progressBar.getMax() + (Integer) msg.obj);
                break;
            case MSG_PROGRESS_INCREASE:
                progressBar.setProgress(progressBar.getProgress() + (Integer) msg.obj);
                break;
            case MSG_PROGRESS_FINISH:
                runningListeners.remove(msg.obj);
                if (runningListeners.size() == 0) {
                    finish();
                }
                break;
            case MSG_PROGRESS_FAILED:
                progressBar.setIndeterminate(true);
                runningListeners.remove(msg.obj);
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

        setContentView(R.layout.act_loading);
        progressBar = (ProgressBar) findViewById(R.id.activity_preload_progress_bar);
        progressBar.setProgress(0);

        startupListeners.add(new EmptyDatabaseListener(getApplicationContext(), progressHandler));
        startupListeners.add(new VersionCheckListener(getApplicationContext()));

        for (final IStartupListener listener : startupListeners) {
            if (listener.isExecutionNeeded()) {
                if (!listener.isInBackground()) {
                    runningListeners.add(listener);
                }
                listener.execute();
            }
        }

        super.onCreate(savedInstanceState);

        if (runningListeners.size() == 0) {
            finish();
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

}
