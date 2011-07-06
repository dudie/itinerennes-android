package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import fr.itinerennes.R;

/**
 * @author orgoz
 */
public final class NewVersionActivity extends Activity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NewVersionActivity.class);

    /** Intent name to use to open a dialog notifyng a new application version. */
    public static final String INTENT_UPGRADE = String.format("%s.UPGRADE",
            ItineRennesActivity.class.getName());

    /** Intent parameter name to indication if the upgrade is mandatory or not. */
    public static final String INTENT_EXTRA_MANDATORY_UPGRADE = "MANDATORY_UPGRADE";

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // disable animation
        getWindow().setWindowAnimations(0);

        final Intent intent = getIntent();

        if (intent.getBooleanExtra(INTENT_EXTRA_MANDATORY_UPGRADE, false)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_mandatory_upgrade);
            builder.setPositiveButton(getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {

                            startActivity(buildMarketIntent());

                        }
                    }).create().show();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.error_recommended_upgrade);
            builder.setPositiveButton(getString(android.R.string.yes),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {

                            startActivity(buildMarketIntent());
                        }
                    });
            builder.setNegativeButton(getString(android.R.string.no),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {

                            dialog.cancel();

                        }
                    });
            builder.setCancelable(false);
            builder.create().show();
        }

    }

    /**
     * Creates an intent in order to open the Itinerennes market page.
     * 
     * @return intent to open Itinerennes market page
     */
    private Intent buildMarketIntent() {

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + getPackageName()));

        return intent;
    }
}
