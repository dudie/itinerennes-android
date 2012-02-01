package fr.itinerennes.startup;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.AsyncTask;
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

    // /** Constant identifying the "failure" dialog. */
    // private static final int DIALOG_FAILURE = 1;

    /** Handler message to set the progress bar max value. */
    public static final int MSG_PROGRESS_SET_MAX = 0;

    /** Handler message to increase the progress status. */
    public static final int MSG_PROGRESS_INCREASE = 1;

    /** Handler message when progress is done. */
    public static final int MSG_PROGRESS_FINISH = 2;

    // /** Handler message specifying the preload has failed. */
    // public static final int MSG_PROGRESS_FAILED = -1;

    /** The progress bar displaying the preload progression state. */
    private ProgressBar progressBar;

    /** The handler to handle progress messages in the UI thread. */
    private final Handler progressHandler = new Handler() {

        @Override
        public void handleMessage(final android.os.Message msg) {

            switch (msg.what) {
            /* first message: set the progressbar maximum value, this message is received only once */
            case MSG_PROGRESS_SET_MAX:
                progressBar.setIndeterminate(false);
                progressBar.setMax((Integer) msg.obj);
                break;

            /* message sent when the progressbar must be increased */
            case MSG_PROGRESS_INCREASE:
                progressBar.setProgress(progressBar.getProgress() + (Integer) msg.obj);
                break;

            /* message sent when synchronous listeners finished their job, the activity is finished */
            case MSG_PROGRESS_FINISH:
                finish();
                break;

            /* message when a failure occurred during a listener's execution */
            // case MSG_PROGRESS_FAILED:
            // progressBar.setIndeterminate(true);
            // showDialog(DIALOG_FAILURE);
            // break;

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
        progressBar.setIndeterminate(true);

        /* initializes the listeners to trigger in background on application startup */
        final List<AsyncTask<Void, ?, ?>> asyncListeners = new ArrayList<AsyncTask<Void, ?, ?>>();
        asyncListeners.add(new VersionCheckListener(getApplicationContext()));
        // trigger them (execution is forked to background)
        for (final AsyncTask<Void, ?, ?> listener : asyncListeners) {

            listener.execute();
        }

        /* initializes the listeners to trigger in foreground on application startup */
        /* the activity keep running until these listeners finish their work */
        // prepare task runner and start
        final List<AbstractStartupListener> syncListeners = new ArrayList<AbstractStartupListener>();
        final TaskRunner syncListenerRunner = new TaskRunner(syncListeners);

        syncListeners.add(new EmptyDatabaseListener(getApplicationContext(), syncListenerRunner));
        syncListenerRunner.start();

        super.onCreate(savedInstanceState);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * Default <i>back</i> button behavior is overrided. Now <i>back</i> button does the same as
     * <i>home</i> button. See {@link Issue #587 https://bugtracker.dudie.fr/issues/587}.
     * 
     * @see android.app.Activity#onBackPressed()
     */
    @Override
    public final void onBackPressed() {

        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Back button pressed. Displaying home screen.");
        }
    }

    /**
     * Interface to provide abilities to listen on progress events fired by
     * {@link AbstractStartupListener} .
     * 
     * @author Jérémie Huchet
     */
    public interface ProgressObserver {

        /**
         * Invoked by {@link AbstractStartupListener} when a task progresses.
         * 
         * @param progress
         *            the amount of units of work processed
         */
        void publishProgress(Integer progress);
    }

    /**
     * A thread to manage synchronous listeners execution and progress bar update.
     * 
     * @author Jérémie Huchet
     */
    private class TaskRunner extends Thread implements ProgressObserver {

        /** The list of listeners to display and follow execution with the progressbar. */
        private final List<AbstractStartupListener> syncListeners;

        /**
         * Constructor.
         * 
         * @param syncListeners
         *            the listeners
         */
        public TaskRunner(final List<AbstractStartupListener> syncListeners) {

            this.syncListeners = syncListeners;
        }

        /**
         * Executes the listeners and manage progressbar.
         * 
         * @see java.lang.Thread#run()
         */
        @Override
        public void run() {

            int totalProgressCount = 0;
            for (final AbstractStartupListener listener : syncListeners) {
                totalProgressCount += listener.progressCount();
            }
            progressHandler.sendMessage(progressHandler.obtainMessage(MSG_PROGRESS_SET_MAX,
                    totalProgressCount));

            for (final AbstractStartupListener listener : syncListeners) {
                listener.execute();
            }

            progressHandler.sendMessage(progressHandler.obtainMessage(MSG_PROGRESS_FINISH));
        }

        /**
         * {@inheritDoc}
         * 
         * @see fr.itinerennes.startup.LoadingActivity.ProgressObserver#publishProgress(java.lang.Integer)
         */
        @Override
        public void publishProgress(final Integer progress) {

            progressHandler.sendMessage(progressHandler.obtainMessage(MSG_PROGRESS_INCREASE,
                    progress));
        }

    }
}
