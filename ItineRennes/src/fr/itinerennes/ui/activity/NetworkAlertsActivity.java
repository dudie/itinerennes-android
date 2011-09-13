package fr.itinerennes.ui.activity;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import fr.dudie.keolis.model.LineAlert;

import fr.itinerennes.R;
import fr.itinerennes.ui.adapter.NetworkAlertsAdapter;
import fr.itinerennes.ui.views.LineImageView;

/**
 * Displays a list with all transport alerts provided by Keolis.
 * 
 * @author Jérémie Huchet
 */
public final class NetworkAlertsActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAlertsActivity.class);

    /** Identifier of the dialog displaying details about an alert. */
    private static final int DIALOG_ALERT_DETAILS = 0;

    /** The adapter used to display line alerts. */
    private NetworkAlertsAdapter alertsAdapter;

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.start");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_network_alerts);

        alertsAdapter = new NetworkAlertsAdapter(this);

        final ListView listAlerts = (ListView) findViewById(R.id.alerts_list);
        listAlerts.setAdapter(alertsAdapter);
        listAlerts.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view,
                    final int position, final long id) {

                showDialog(position);
            }
        });

        final AsyncTask<Void, Void, List<LineAlert>> loadingTask = new AsyncTask<Void, Void, List<LineAlert>>() {

            /**
             * {@inheritDoc}
             * 
             * @see android.os.AsyncTask#doInBackground(Params[])
             */
            @Override
            protected List<LineAlert> doInBackground(final Void... params) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("retrieving alerts");
                }

                List<LineAlert> alerts = null;
                try {
                    alerts = getApplicationContext().getKeolisClient().getAllLinesAlerts();
                } catch (final IOException e) {
                    // TJHU Gérer l'excepton proprement
                    getApplicationContext().getExceptionHandler().handleException(e);
                }

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("received {} alerts", alerts != null ? alerts.size() : 0);
                }

                return alerts;
            }

            /**
             * {@inheritDoc}
             * 
             * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
             */
            @Override
            protected void onPostExecute(final List<LineAlert> alerts) {

                // TJHU gérer le cas où il n'y a pas d'alerte pour le moment
                alertsAdapter.setAlerts(alerts);
                alertsAdapter.notifyDataSetChanged();
                listAlerts.setVisibility(View.VISIBLE);
                findViewById(R.id.misc_view_is_loading).setVisibility(View.GONE);

            }

        };
        loadingTask.execute();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("onCreate.end");
        }
    }

    /**
     * Creates the dialog for the given identifier.
     * 
     * @param id
     *            the id reflects the position of the line alert to show in the
     *            {@link #alertsAdapter}
     * @see android.app.Activity#onCreateDialog(int)
     */
    @Override
    protected Dialog onCreateDialog(final int id) {

        final LineAlert alertToDisplay = alertsAdapter.getItem(id);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        final View alertView = getLayoutInflater().inflate(R.layout.dial_network_alert_details,
                null);

        // set lines related to the alert
        final TextView title = (TextView) alertView.findViewById(R.id.dial_network_alert_title);
        title.setText(alertToDisplay.getBetterTitle());
        final LinearLayout lines = (LinearLayout) alertView
                .findViewById(R.id.dial_network_alert_list_icons);
        for (final String lineId : alertToDisplay.getLines()) {
            final LineImageView lineIcon = new LineImageView(this);
            lineIcon.setLine(lineId);
            lineIcon.setBounds(28, 28);
            lineIcon.setPadding(2, 0, 2, 0);
            lines.addView(lineIcon);
        }

        // set dates
        final TextView startTime = (TextView) alertView
                .findViewById(R.id.dial_network_alert_start_time);
        startTime.setText(this.getString(R.string.date_from,
                toFullDateString(alertToDisplay.getStartTime())));
        final TextView endTime = (TextView) alertView
                .findViewById(R.id.dial_network_alert_end_time);
        endTime.setText(this.getString(R.string.date_to,
                toFullDateString(alertToDisplay.getEndTime())));
        final TextView details = (TextView) alertView.findViewById(R.id.dial_network_alert_details);

        // set alert details
        if (null != alertToDisplay.getDetail()) {
            details.setText(alertToDisplay.getDetail().replaceAll("\\.", ".\n").trim());
        }

        builder.setView(alertView);
        return builder.create();
    }

    /**
     * Gets a full date string representation such as December 12, 2011 12:56.
     * 
     * @param date
     *            the date
     * @return the string representation
     */
    private CharSequence toFullDateString(final Date date) {

        return String.format("%s %s", DateFormat.getLongDateFormat(this).format(date), DateFormat
                .getTimeFormat(this).format(date));
    }
}
