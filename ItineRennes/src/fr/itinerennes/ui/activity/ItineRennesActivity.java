package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.R;

/**
 * An abstract activity providing common functionalities such as automatic.
 * 
 * @author Jérémie Huchet
 */
public abstract class ItineRennesActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(ItineRennesActivity.class);

    public static final String INTENT_MANDATORY_UPGRADE = String.format("%s.MANDATORY_UPGRADE",
            ItineRennesActivity.class.getName());

    public static final String INTENT_RECOMMENDED_UPGRADE = String.format("%s.RECOMMENDED_UPGRADE",
            ItineRennesActivity.class.getName());

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

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onNewIntent(android.content.Intent)
     */
    @Override
    protected final void onNewIntent(final Intent intent) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.start");
        }

        if (INTENT_MANDATORY_UPGRADE.equals(intent.getAction())) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_mandatory_upgrade);
            builder.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {

                            startActivity(buildMarkerIntent());
                        }
                    }).create().show();

        } else if (INTENT_RECOMMENDED_UPGRADE.equals(intent.getAction())) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_recommended_upgrade);
            builder.setPositiveButton(getString(android.R.string.yes),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {

                            startActivity(buildMarkerIntent());
                        }
                    });
            builder.setNegativeButton(getString(android.R.string.no),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {

                            dialog.cancel();
                        }
                    });
            builder.create().show();
        } else {
            onCustomNewIntent(intent);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onNewIntent.end");
        }
    }

    /**
     * Creates an intent in order to open the Itinerennes market page.
     * 
     * @return intent to open Itinerennes market page
     */
    protected final Intent buildMarkerIntent() {

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));

        return intent;
    }

    /**
     * Called when built-in onNewIntent method has not handle the new intent.
     * 
     * @param intent
     *            The new intent that was started for the activity.
     */
    protected void onCustomNewIntent(final Intent intent) {

    }
}
