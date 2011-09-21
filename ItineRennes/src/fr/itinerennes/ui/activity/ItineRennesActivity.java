package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import fr.itinerennes.ItineRennesApplication;

/**
 * An abstract activity providing common functionalities such as automatic.
 * 
 * @author Jérémie Huchet
 */
public abstract class ItineRennesActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(ItineRennesActivity.class);

    /** Receiver for new version notifications. */
    private final NewVersionReceiver receiver = new NewVersionReceiver();

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {

        super.onResume();

        registerReceiver(receiver, new IntentFilter(NewVersionActivity.INTENT_UPGRADE));
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onPause()
     */
    @Override
    protected void onPause() {

        super.onPause();

        unregisterReceiver(receiver);
    }

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
    public final ItineRennesApplication getApplicationContext() {

        return (ItineRennesApplication) super.getApplicationContext();
    }

    /**
     * Broadcast Receiver for new application version intents.
     * 
     * @author Olivier Boudet
     */
    private class NewVersionReceiver extends BroadcastReceiver {

        /**
         * {@inheritDoc}
         * 
         * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
         *      android.content.Intent)
         */
        @Override
        public void onReceive(final Context context, final Intent intent) {

            LOGGER.debug("onReceive.start");

            intent.setClass(context, NewVersionActivity.class);
            startActivity(intent);

        }
    }
}
