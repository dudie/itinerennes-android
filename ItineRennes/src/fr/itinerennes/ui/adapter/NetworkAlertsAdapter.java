package fr.itinerennes.ui.adapter;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import fr.dudie.keolis.model.LineAlert;

import fr.itinerennes.R;

/**
 * Array adapter to display Keolis line alerts in a {@link ListView}.
 * 
 * @author Jérémie Huchet
 */
public final class NetworkAlertsAdapter extends ArrayAdapter<LineAlert> {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAlertsAdapter.class);

    private final LayoutInflater layoutInfalter;

    public NetworkAlertsAdapter(final Context context) {

        super(context, R.layout.li_line_alert);
        layoutInfalter = LayoutInflater.from(context);
    }

    public void setAlerts(final Collection<LineAlert> alerts) {

        if (null != alerts) {
            for (final LineAlert alert : alerts) {
                add(alert);
            }
        }
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LinearLayout row;

        if (null == convertView) {
            row = (LinearLayout) layoutInfalter.inflate(R.layout.li_line_alert, null);
        } else {
            row = (LinearLayout) convertView;
        }

        final TextView title = (TextView) row.findViewById(R.id.li_line_alert_title);
        title.setText(getItem(position).getBetterTitle());

        return row;
    }
}
