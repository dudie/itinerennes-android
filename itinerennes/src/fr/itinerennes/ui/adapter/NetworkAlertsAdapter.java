package fr.itinerennes.ui.adapter;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import fr.dudie.keolis.model.LineAlert;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.ui.views.LineImageView;

/**
 * Array adapter to display Keolis line alerts in a {@link ListView}.
 * 
 * @author Jérémie Huchet
 */
public final class NetworkAlertsAdapter extends ArrayAdapter<LineAlert> {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAlertsAdapter.class);

    /** The activity context. */
    private final ItineRennesActivity context;

    /** The layout inflater. */
    private final LayoutInflater layoutInfalter;

    /**
     * Creates a network alerts adapter.
     * 
     * @param context
     *            the activity context
     */
    public NetworkAlertsAdapter(final ItineRennesActivity context) {

        super(context, R.layout.li_line_alert);
        this.context = context;
        layoutInfalter = LayoutInflater.from(context);
    }

    /**
     * Sets the alerts.
     * 
     * @param alerts
     *            the list of alerts to set
     */
    public void setAlerts(final Collection<LineAlert> alerts) {

        if (null != alerts) {
            for (final LineAlert alert : alerts) {
                add(alert);
            }
        }
    }

    /**
     * Returns an horizontal view with the icons of the lines related to the alert and the title of
     * the alert on the right.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        ViewGroup row;

        if (null == convertView) {
            row = (ViewGroup) layoutInfalter.inflate(R.layout.li_line_alert, null);
        } else {
            row = (ViewGroup) convertView;
        }

        final LineAlert alert = getItem(position);

        // set line icons
        final ViewGroup linesContainer = (ViewGroup) row
                .findViewById(R.id.li_line_alert_related_lines);
        linesContainer.removeAllViews();
        for (final String line : alert.getLines()) {
            final LineImageView icon = new LineImageView(context);
            icon.setLine(line);
            icon.fitToHeight(24);
            icon.setPadding(2, 0, 2, 0);
            linesContainer.addView(icon);
        }

        // set alert title
        final TextView title = (TextView) row.findViewById(R.id.li_line_alert_title);
        title.setText(alert.getBetterTitle());

        return row;
    }
}
