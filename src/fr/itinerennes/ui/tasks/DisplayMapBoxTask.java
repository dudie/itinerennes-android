package fr.itinerennes.ui.tasks;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.view.View;

import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.adapter.MapBoxAdapter;
import fr.itinerennes.ui.views.MapBoxView;

/**
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class DisplayMapBoxTask<D> extends SafeAsyncTask<Void, Void, Void> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(DisplayMapBoxTask.class);

    /** The application context. */
    private final ITRContext context;

    /** The view where the additional informations are added. */
    private final MapBoxView boxView;

    /** The {@link MapBoxAdapter} used to display the map box informations. */
    private final MapBoxAdapter<D> adapter;

    /** The metadata of the marker displayed. */
    private final D data;

    /**
     * Creates the task which will display the map box.
     * 
     * @param context
     *            the itinerennes application context
     * @param boxView
     *            the view where the additional informations are added
     * @param adapter
     *            the map box adapter
     * @param data
     *            the metadata of the marker displayed
     */
    public DisplayMapBoxTask(final ITRContext context, final MapBoxView boxView,
            final MapBoxAdapter<D> adapter, final D data) {

        super(context);
        this.context = context;
        this.boxView = boxView;
        this.adapter = adapter;
        this.data = data;
    }

    /**
     * Active the loading icon.
     * 
     * @see android.os.AsyncTask#onPreExecute()
     */
    @Override
    protected void onPreExecute() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPreExecute.start - data={}", data);
        }

        boxView.setContentView(adapter.getView(data));
        adapter.onStartLoading(boxView.getContentView());
        boxView.setVisibility(View.VISIBLE);
        boxView.postInvalidate();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPreExecute.end - data={}", data);
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
    protected final Void doInBackgroundSafely(final Void... params) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.start - data={}", data);
        }

        adapter.doInBackground(boxView.getContentView(), data);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.end - data={}", data);
        }
        return null;
    }

    /**
     * Updates the map box view with the loaded data and deactive the loadinf icon.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected final void onCustomPostExecute(final Void nothing) {

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
}
