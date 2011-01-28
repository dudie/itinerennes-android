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
public interface CopyOfMapBoxAdapter<T> {

    /**
     * @param item
     *            the item for which the view must be generated
     * @return the view to display in the map box
     */
    View getView(T item);

    void doInBackground(T item);

    boolean updateView(View v, T item);

}
