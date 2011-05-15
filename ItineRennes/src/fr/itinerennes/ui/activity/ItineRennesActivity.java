package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.Activity;

import fr.itinerennes.ItineRennesApplication;

/**
 * An abstract activity providing common functionalities such as automatic.
 * 
 * @author Jérémie Huchet
 */
public abstract class ItineRennesActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(ItineRennesActivity.class);

    /**
     * Tries to dismiss a displayed dialog but catch the exception throws by the original
     * implementation if the dialog was not displayed.
     * 
     * @param id
     *            the identifier of the dialog to dismiss
     * @return true if a dialog has been dismissed
     * @see #dismissDialog(int)
     */
    public final boolean dismissDialogIfDisplayed(final int id) {

        boolean dismissed = false;
        try {
            super.dismissDialog(id);
            dismissed = true;
        } catch (final IllegalArgumentException e) {
            dismissed = false;
        }
        return dismissed;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.content.ContextWrapper#getApplicationContext()
     */
    @Override
    public ItineRennesApplication getApplicationContext() {

        return (ItineRennesApplication) super.getApplicationContext();
    }
}
