package fr.itinerennes.ui.views;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import fr.itinerennes.R;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.activity.ITRContext;

/**
 * @author Jérémie Huchet
 */
public class PreloadDialog extends AlertDialog {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(PreloadDialog.class);

    /** The bounding box containing the whole world. */
    private static final BoundingBoxE6 BBOX_WORLD = new BoundingBoxE6(180000000, 180000000,
            -180000000, -180000000);

    /** The itinerennes context. */
    private final ITRContext context;

    /** The async task in charge for loading data. */
    private final AsyncTask<BoundingBoxE6, Void, Void> loader;

    /**
     * Creates the preload dialog box.
     * 
     * @param context
     *            the itinerennes context
     */
    public PreloadDialog(final ITRContext context) {

        super(context);
        this.context = context;

        this.loader = new AsyncTask<BoundingBoxE6, Void, Void>() {

            @Override
            protected Void doInBackground(final BoundingBoxE6... params) {

                try {
                    context.getBusService().getStations(BBOX_WORLD);
                } catch (final GenericException e) {
                    context.getExceptionHandler().handleException(e);
                }
                try {
                    context.getBikeService().getStations(BBOX_WORLD);
                } catch (final GenericException e) {
                    context.getExceptionHandler().handleException(e);
                }
                try {
                    context.getSubwayService().getStations(BBOX_WORLD);
                } catch (final GenericException e) {
                    context.getExceptionHandler().handleException(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(final Void result) {

                PreloadDialog.this.dismiss();
            };
        };
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        setTitle(R.string.preload_dialog_title);
        setIcon(android.R.drawable.ic_dialog_info);
        final View v = getLayoutInflater().inflate(R.layout.preload_dialog, null);
        setView(v);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {

        loader.execute(BBOX_WORLD);
    }
}
