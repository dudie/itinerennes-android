package fr.itinerennes.ui.adapter;

import android.view.View;

import fr.itinerennes.ui.tasks.DisplayMapBoxTask;

/**
 * Common base interface of implementation for an adapter that can be used to fill the MapBox.
 * <p>
 * See {@link DisplayMapBoxTask} for other details about the box loading.
 * 
 * @param <T>
 *            the type of data the adapter uses
 * @author Jérémie Huchet
 */
public interface MapBoxAdapter<T> {

    /**
     * This method should initializes the view displaying additional informations for the given
     * item. Execution must be quick as it is executed by the UI thread.
     * 
     * @param item
     *            the item for which the view must be generated
     * @return the view to display in the map box
     */
    View getView(T item);

    /**
     * Called before the {@link #doInBackground(View, Object)} begins.
     * 
     * @param view
     *            the view you returned in {@link #getView(Object)}
     */
    void onStartLoading(View view);

    /**
     * This method allow you to do some tasks in a background thread. If you use some global
     * variables in the adapter, be sure to reinitializes them.
     * 
     * @param view
     *            the view you returned in {@link #getView(Object)}
     * @param item
     *            the item for which this adapter generates a view
     */
    void doInBackground(View view, T item);

    /**
     * This method is called after the method {@link #doInBackground(View, Object)} finished.
     * 
     * @param view
     *            the view you returned in {@link #getView(Object)}
     * @param item
     *            the item for which this adapter generates a view
     */
    void updateView(View view, T item);

    /**
     * Called after the {@link #updateView(View, Object)}.
     * 
     * @param view
     *            the view you returned in {@link #getView(Object)}
     */
    void onStopLoading(View view);

}
