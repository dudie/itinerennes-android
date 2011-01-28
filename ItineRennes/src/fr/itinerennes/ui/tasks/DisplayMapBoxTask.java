package fr.itinerennes.ui.tasks;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.view.View;

import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.Station;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.adapter.MapBoxAdapter;
import fr.itinerennes.ui.views.MapBoxView;
import fr.itinerennes.ui.views.overlays.Marker;

/**
 * @param <D>
 *            the type of the bundled data with the item
 * @author Jérémie Huchet
 */
public class DisplayMapBoxTask<D> extends SafeAsyncTask<Void, Void, D> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(DisplayMapBoxTask.class);

    /** The application context. */
    private final ITRContext context;

    /** The view where the additional informations are added. */
    private final MapBoxView boxView;

    /** The {@link MapBoxAdapter} used to display the map box informations. */
    private final MapBoxAdapter<Marker<D>, D> adapter;

    /** The marker item. */
    private final Marker<D> item;

    /** Tells whether or not the element is a bookmark. */
    private boolean isStarred = false;

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
     *            the marker item
     */
    public DisplayMapBoxTask(final ITRContext context, final MapBoxView boxView,
            final MapBoxAdapter<Marker<D>, D> adapter, final Marker<D> item) {

        super(context);
        this.context = context;
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
    protected void onPreExecute() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPreExecute.start - itemId={}", item.getId());
        }
        boxView.setLoading(true);
        final int res = adapter.getBoxIcon(item);
        boxView.setIcon(context.getResources().getDrawable(res));
        boxView.setTitle(adapter.getBoxTitle(item));
        boxView.setLoading(true);
        boxView.setVisibility(View.VISIBLE);
        boxView.setAdditionalInformations(adapter.getBoxDetailsView(context, item));
        boxView.setOnClickIntent(adapter.getOnClickIntent(context, item));

        final Station station = (Station) item.getData();

        boxView.setBookmarkLabel(station.getName());
        boxView.setBookmarkType(station.getClass().getName());
        boxView.setBookmarkId(station.getId());
        boxView.postInvalidate();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPreExecute.end - itemId={}", item.getId());
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
    protected final D doInBackgroundSafely(final Void... params) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.start - itemId={}", item.getId());
        }

        final D data = adapter.backgroundLoad(item);

        final String id = ((Station) item.getData()).getId();
        isStarred = context.getBookmarksService()
                .isStarred(item.getData().getClass().getName(), id);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("doInBackground.end - itemId={}", item.getId());
        }
        return data;
    }

    /**
     * Updates the map box view with the loaded data and deactive the loadinf icon.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected final void onCustomPostExecute(final D data) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPostExecute.start - itemId={}", item.getId());
        }
        final int res = adapter.getBoxIcon(item, data);
        boxView.setIcon(context.getResources().getDrawable(res));
        boxView.setTitle(adapter.getBoxTitle(item, data));
        adapter.updateBoxDetailsView(boxView.getAdditionalInformations(), item, data);
        boxView.setStarred(isStarred);
        boxView.setLoading(false);
        boxView.postInvalidate();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onPostExecute.end - itemId={}", item.getId());
        }
    }
}
