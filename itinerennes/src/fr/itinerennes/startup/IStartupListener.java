package fr.itinerennes.startup;

/**
 * Interface definition for a callback to be invoked when the application starts up.
 * 
 * @author Olivier Boudet
 */
public interface IStartupListener {

    /**
     * Checks if execution of the listener is needed.
     * 
     * @return true if execution is needed.
     */
    boolean isExecutionNeeded();

    /**
     * Checks if execution of the listener is done in background. If true, the LoadingActivity will
     * not wait the execution end to finish.
     * 
     * @return true if execution is done in background.
     */
    boolean isInBackground();

    /**
     * Will be executed if execution is needed.
     */
    void execute();

}
