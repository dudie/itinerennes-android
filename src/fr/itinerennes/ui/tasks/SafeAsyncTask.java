package fr.itinerennes.ui.tasks;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.os.AsyncTask;

import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.activity.ITRContext;

/**
 * @author Jérémie Huchet
 */
public abstract class SafeAsyncTask<Params, Progress, Result> extends
        AsyncTask<Params, Progress, Result> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(SafeAsyncTask.class);

    /** The itinerennes application context. */
    private final ITRContext context;

    /** The error occurred, if so. */
    private GenericException error = null;

    /**
     * Creates an async task which {@link #doInBackground(Object...)} work can throw a
     * {@link GenericException}.
     * 
     * @param context
     *            the itinerennes application context
     */
    public SafeAsyncTask(final ITRContext context) {

        this.context = context;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected final Result doInBackground(final Params... params) {

        Result result = null;
        try {
            result = doInBackgroundSafely(params);
        } catch (final GenericException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("an exception occurred during #doInBackgroundSafely()");
            }
            this.error = e;
        }
        return result;
    }

    /**
     * Called by {@link #doInBackground(Object...)}.
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     * @throws GenericException
     *             a business exception thrown
     */
    protected abstract Result doInBackgroundSafely(Params... params) throws GenericException;

    /**
     * Checks if an exception has been thrown by {@link #doInBackgroundSafely(Object[])}. If so,
     * invokes the exception handler of the application.
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected final void onPostExecute(final Result result) {

        if (error != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("sending an exception to the error handler");
            }
            context.getExceptionHandler().handleException(error);
        }
        onCustomPostExecute(result);
    }

    /**
     * Called by {@link #onPostExecute(Object)} after the error handling has been done.
     * 
     * @param result
     *            the result of {@link #doInBackgroundSafely(Object[])}, be careful it may be null
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    protected abstract void onCustomPostExecute(Result result);
}
