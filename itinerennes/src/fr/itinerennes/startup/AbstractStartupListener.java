package fr.itinerennes.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.itinerennes.startup.LoadingActivity.ProgressObserver;

/**
 * Interface definition for a callback to be invoked when the application starts up.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */
public abstract class AbstractStartupListener {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractStartupListener.class);

    /** An (optional) observer listening on progress events. */
    private final ProgressObserver observer;

    /**
     * Default constructor.
     */
    public AbstractStartupListener() {

        this(null);
    }

    /**
     * Constructor.
     * 
     * @param observer
     *            an (optional) observer of progress events
     */
    public AbstractStartupListener(final ProgressObserver observer) {

        this.observer = observer;
        if (null == observer) {
            LOGGER.warn("No observer attached to {}", this.getClass().getSimpleName());
        }
    }

    /**
     * This will be run in background before starting the {@link #execute()} job. This is intended
     * to compute/estimate the total length of the work which will be done by {@link #execute()}.
     * <p>
     * Even if this is run in background, execution should be as quick as possible.
     * 
     * @return the total amount of progress units needed to complete the task
     */
    public abstract int progressCount();

    /**
     * This is run in background.
     */
    public abstract void execute();

    /**
     * Notifies the observer that the background process has progressed.
     * 
     * @param progress
     *            the amount of progress units which have been processed
     */
    public final void publishProgress(final int progress) {

        if (null != observer) {
            observer.publishProgress(progress);
        }
    }
}
