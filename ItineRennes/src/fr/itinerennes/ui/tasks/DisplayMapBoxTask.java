package fr.itinerennes.ui.tasks;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.os.AsyncTask;
import android.view.View;

import fr.itinerennes.ui.adapter.MapBoxAdapter;
import fr.itinerennes.ui.views.MapBoxView;
import fr.itinerennes.ui.views.overlays.MarkerOverlayItem;

/**
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class DisplayMapBoxTask<D> extends AsyncTask<Void, Void, D> {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(DisplayMapBoxTask.class);

    /** The view where the additional informations are added. */
    private final MapBoxView boxView;

    /** The {@link MapBoxAdapter} used to display the map box informations. */
    private final MapBoxAdapter<D> adapter;

    /** The marker displayed. */
    private final MarkerOverlayItem item;

    /**
     * Creates the task which will display the map box.
     * 
     * @param context
     *            the itinerennes application context
     * @param boxView
     *            the view where the additional informations are added
     * @param adapter
     *            the map box adapter
     * @param item
     *            the metadata of the marker displayed
     */
    public DisplayMapBoxTask(final MapBoxView boxView, final MapBoxAdapter<D> adapter,
            final MarkerOverlayItem item) {

        super();
        this.boxView = boxView;
        this.adapter = adapter;
        this.item = item;
    }

    /**
     * Active the loading icon.
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected final void onPreExecute() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPreExecute.start - item={}", item);
        }

        boxView.setContentView(adapter.getView(item));
        adapter.onStartLoading(boxView.getContentView());
        boxView.setVisibility(View.VISIBLE);
        boxView.postInvalidate();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPreExecute.end - item={}", item);
        }
    }

    /**
     * Uses the adapter to load some data in background.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#doInBackground(Params[])
     */
    @Override
    protected final D doInBackground(final Void... params) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.start - item={}", item);
        }

        D result = null;
        try {
            result = adapter.doInBackground(boxView.getContentView(), item);
        } catch (final Exception e) {
            // TJHU pas bien mais c'est pour éviter de planter à cause d'une async task perdue
            if (LOGGER.isInfoEnabled()) {
                LOGGER.error("async task failed", e);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.end - item={}", item);
        }

        return result;
    }

    /**
     * Updates the map box view with the loaded data and deactive the loadinf icon.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected final void onPostExecute(final D data) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPostExecute.start - data={}", data);
        }
        adapter.updateView(boxView.getContentView(), data);
        adapter.onStopLoading(boxView.getContentView());
        boxView.postInvalidate();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPostExecute.end - data={}", data);
        }
    }

    @Override
    protected void onCancelled() {

        super.onCancelled();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCancelled.start/stop - isCancelled={}", isCancelled());
        }
    }
}
