package fr.itinerennes.commons.listeners;

/**
 * An interface to listen on miscellaneous task progression.
 * 
 * @author Jérémie Huchet
 */
public interface ProgressListener {

    /**
     * Gets the the amount of units of work the task will need.
     * 
     * @return the targeted progress count / the amount of units of work
     */
    int getTotal();

    /**
     * Triggered when the task starts.
     */
    void onStart();

    /**
     * Triggered when the task progresses.
     * 
     * @param progress
     *            the progress count
     */
    void onProgress(int progress);

    /**
     * Triggered when the task will begin a work but doesn't know how long it will run.
     * 
     * @param indeterminateFor
     *            the amount of next indeterminate units of work
     */
    void onProgressIndeterminate(int indeterminateFor);

    /**
     * Triggered when the task ends.
     * <p>
     * At this moment, the initial 'total' count sent in {@link #onStart(int)} call should be equals
     * to the addition of each 'progress' value sent during {@link #onProgress(int)} calls.
     */
    void onFinish();
}
