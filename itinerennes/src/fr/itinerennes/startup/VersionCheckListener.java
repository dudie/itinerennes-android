package fr.itinerennes.startup;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Intent;
import android.os.AsyncTask;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.model.VersionCheck;
import fr.itinerennes.ui.activity.NewVersionActivity;
import fr.itinerennes.utils.VersionUtils;
import fr.itinerennes.utils.xml.XmlVersionParser;

/**
 * Listener which checks if a new application version is available.
 * 
 * @author Olivier Boudet
 */
public final class VersionCheckListener extends AsyncTask<Void, Void, Intent> {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionCheckListener.class);

    /** The context. */
    private final ItineRennesApplication context;

    /**
     * Constructor.
     * 
     * @param context
     *            the application context.
     */
    public VersionCheckListener(final ItineRennesApplication context) {

        this.context = context;
    }

    /**
     * Fetches XML describing Itinerennes versions sends an intent to the current top activity in
     * order to display an error message if necessary.
     * 
     * @param params
     *            no parameter needed
     * @return an {@link Intent} to display an alert dialog about upgrading the app or null if
     *         nothing more has to be done
     */
    @Override
    protected Intent doInBackground(final Void... params) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start checking version");
        }

        Intent i = null;
        final HttpClient httpClient = context.getHttpClient();
        final HttpGet request = new HttpGet();
        try {
            request.setURI(new URI(ItineRennesConstants.ITINERENNES_VERSION_URL));
            final HttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                final XmlVersionParser parser = new XmlVersionParser();

                final VersionCheck versionCheck = parser.parse(response.getEntity().getContent());

                final String currentVersion = context.getResources().getString(
                        R.string.version_number);

                final int comparisonMinRequired = VersionUtils.compare(currentVersion,
                        versionCheck.getMinRequired());
                final int comparisonLatest = VersionUtils.compare(currentVersion,
                        versionCheck.getLatest());

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String
                            .format("Current version : %s - Latest version : %s - Minimum required version : %s",
                                    currentVersion, versionCheck.getLatest(),
                                    versionCheck.getMinRequired()));
                }

                if (comparisonMinRequired < 0 || comparisonLatest < 0) {

                    i = new Intent();
                    i.setAction(NewVersionActivity.INTENT_UPGRADE);

                    if (comparisonMinRequired < 0) {
                        // minimum version required is greater than the current version
                        i.putExtra(NewVersionActivity.INTENT_EXTRA_MANDATORY_UPGRADE, true);

                    } else if (comparisonLatest < 0) {
                        // the latest available version is greater than the current version
                        i.putExtra(NewVersionActivity.INTENT_EXTRA_MANDATORY_UPGRADE, false);
                    }
                }

            }
        } catch (final Exception e) {
            LOGGER.warn("Can not get version informations.");
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("end checking version.");
        }

        return i;
    }

    /**
     * If an intent is given it is launched.
     * 
     * @param intent
     *            the intent to launch, null if no intent has to be launch
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     */
    @Override
    protected void onPostExecute(final Intent intent) {

        if (intent != null) {

            context.sendBroadcast(intent);

        }
    }
}
