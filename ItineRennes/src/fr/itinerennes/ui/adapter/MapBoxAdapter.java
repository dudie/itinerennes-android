package fr.itinerennes.ui.adapter;

import org.slf4j.Marker;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.ui.tasks.DisplayMapBoxTask;
import fr.itinerennes.ui.views.overlays.ITROverlayItem;

/**
 * Common base interface of implementation for an adapter that can be used to fill the MapBox when a
 * marker is focused.
 * <p>
 * See {@link DisplayMapBoxTask} for other details about the box loading.
 * 
 * @param <T>
 *            the type of data the adapter uses
 * @author Jérémie Huchet
 */
public interface MapBoxAdapter<T extends ITROverlayItem<?>, D> {

    /**
     * This method should return the title to display in the information box instantaneously.
     * 
     * @param item
     *            the focused item
     * @return the title to display in the information box
     */
    String getBoxTitle(T item);

    /**
     * This method should return the drawable to display in the information instantaneously.
     * 
     * @param item
     *            the focused item
     * @return the resource id of the drawable to display in the information box
     */
    int getBoxIcon(T item);

    /**
     * This method should return the view to display in the information box.
     * 
     * @param context
     *            the application context
     * @param item
     *            the focused item
     * @return the view to display in the information box
     */
    View getBoxDetailsView(Context context, T item);

    /**
     * This method is run in the background part of an {@link AsyncTask}, so it can be used to load
     * useful data to populate the information box view whithout blocking the ui thread.
     * 
     * @param item
     *            the focused item
     * @return some useful data to fill the information box view
     * @throws GenericException
     *             if the background load cannot be completed
     */
    D backgroundLoad(T item) throws GenericException;

    /**
     * This method should return the title to display in the information box after
     * {@link #backgroundLoad(Marker)} was executed.
     * 
     * @param item
     *            the focused item
     * @param data
     *            the preloaded data with {@link #backgroundLoad(Marker)}
     * @return the title to display in the information box
     */
    String getBoxTitle(T item, D data);

    /**
     * This method should return the icon to display in the information box after
     * {@link #backgroundLoad(Marker)} was executed.
     * 
     * @param item
     *            the focused item
     * @param data
     *            the preloaded data with {@link #backgroundLoad(Marker)}
     * @return the resource id of the drawable to display in the information box
     */
    int getBoxIcon(T item, D data);

    /**
     * This method is triggered after {@link #backgroundLoad(Marker)} was executed. So you are able
     * to update its content with the data loaded in background.
     * 
     * @param item
     *            the focused item
     * @param data
     *            the preloaded data with {@link #backgroundLoad(Marker)}
     * @param boxDetailsView
     */
    void updateBoxDetailsView(View boxDetailsView, T item, final D data);

    /**
     * When the user clicks on the map box, this method is called before a new activity is started
     * with the itent provided by {@link #getOnClickIntent(Object)}.
     * 
     * @param item
     *            the focused item
     */
    void beforeStartActivity(T item);

    /**
     * When the user clicks on the map box, this method is used to retrieve an intent to start a new
     * activity if {@link #beforeStartActivity(Object)} returned true.
     * 
     * @param packageContext
     *            a Context of the application package
     * @param item
     *            the focused item
     * @return the intent to use to start a new activity when the box is clicked
     */
    Intent getOnClickIntent(Context packageContext, T item);
}
