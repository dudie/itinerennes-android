package fr.itinerennes.ui.activity;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

import fr.dudie.keolis.model.LineAlert;
import fr.itinerennes.R;
import fr.itinerennes.business.service.KeolisApi;
import fr.itinerennes.ui.adapter.NetworkAlertsAdapter;
import fr.itinerennes.ui.views.LineImageView;

/**
 * Displays a list with all transport alerts provided by Keolis.
 * 
 * @author Jérémie Huchet
 */
@EActivity(R.layout.act_network_alerts)
public class NetworkAlertsActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAlertsActivity.class);

    /** Identifier of the dialog displaying details about an alert. */
    private static final int DIALOG_ALERT_DETAILS = 0;

    /** The adapter used to display line alerts. */
    private NetworkAlertsAdapter alertsAdapter;

    @Bean
    KeolisApi keolis;

    @ViewById(R.id.misc_view_is_loading)
    View loader;

    @ViewById(R.id.alerts_list)
    ListView listAlerts;

    @AfterViews
    void initializeAlertsAdapter() {
        alertsAdapter = new NetworkAlertsAdapter(this);
        listAlerts.setAdapter(alertsAdapter);
    }

    @ItemClick(R.id.alerts_list)
    void onAlertIemClick(final int itemId) {
        showDialog(itemId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAlerts();
    }

    @Background
    void loadAlerts() {
        List<LineAlert> alerts = null;
        try {
            alerts = keolis.getAllLinesAlerts();
        } catch (final IOException e) {
            // TJHU Gérer l'excepton proprement
            getApplicationContext().getExceptionHandler().handleException(e);
        }

        if (null != alerts) {
            LOGGER.debug("received {} alerts", alerts != null ? alerts.size()
                    : 0);
            displayAlerts(alerts);
        } else {
            LOGGER.warn("can't retrieve line alerts");
        }
    }

    @UiThread
    void displayAlerts(final List<LineAlert> alerts) {
        // TJHU gérer le cas où il n'y a pas d'alerte pour le moment
        alertsAdapter.setAlerts(alerts);
        alertsAdapter.notifyDataSetChanged();

        listAlerts.setVisibility(View.VISIBLE);
        loader.setVisibility(View.GONE);
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
        final ViewGroup lines = (ViewGroup) alertView
                .findViewById(R.id.dial_network_alert_list_icons);
        for (final String lineId : alertToDisplay.getLines()) {
            final LineImageView lineIcon = new LineImageView(this);
            lineIcon.setLine(lineId);
            lineIcon.fitToHeight(28);
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
