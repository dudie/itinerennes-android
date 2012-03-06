package fr.itinerennes.ui.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import fr.itinerennes.Conf;
import fr.itinerennes.R;

/**
 * Transparent activity showing a dialog in case of new version available.
 * 
 * @author Olivier Boudet
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

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onResume()
     */
    @Override
    protected void onResume() {

        super.onResume();

        // disable animation
        getWindow().setWindowAnimations(0);

        final Intent intent = getIntent();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int positiveButtonStringId;

        if (intent.getBooleanExtra(INTENT_EXTRA_MANDATORY_UPGRADE, false)) {
            builder.setMessage(R.string.error_mandatory_upgrade);
            builder.setCancelable(false);
            positiveButtonStringId = android.R.string.ok;
        } else {
            builder.setMessage(R.string.error_recommended_upgrade);
            positiveButtonStringId = android.R.string.yes;
            builder.setNegativeButton(getString(android.R.string.no),
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {

                            dialog.cancel();
                            finish();

                        }
                    });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(final DialogInterface dialog) {

                    finish();

                }
            });
        }
        builder.setPositiveButton(getString(positiveButtonStringId),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {

                        try {
                            startActivity(buildMarketIntent());
                        } catch (final ActivityNotFoundException e) {
                            // in case the device we are running on doesn't handle market intents,
                            // then we open an URL
                            startActivity(buildManualDownloadIntent());
                        }
                    }
                });
        builder.create().show();
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

    /**
     * Creates an intent in order to open the Itinerennes website download page.
     * 
     * @return intent to open Itinerennes website download page
     */
    private Intent buildManualDownloadIntent() {

        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Conf.ITINERENNES_DOWNLOAD_PAGE_URL));

        return intent;
    }
}
