package fr.itinerennes.startup;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.business.service.VersionService;
import fr.itinerennes.startup.version.model.UpdateInfo;
import fr.itinerennes.ui.activity.NewVersionActivity;

/**
 * Listener which checks if a new application version is available.
 * 
 * @author Olivier Boudet
 */
public final class VersionCheckListener extends AsyncTask<Void, Void, Intent> {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionCheckListener.class);

    /** The context. */
    private final Context context;

    /** The version service. */
    private final VersionService versionService;

    /**
     * Constructor.
     * 
     * @param context
     *            the application context
     * @param versionService
     *            the version service
     */
    public VersionCheckListener(final ItineRennesApplication context, final VersionService versionService) {

        this.context = context;
        this.versionService = versionService;
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

        Intent i = null;

        try {
            final UpdateInfo update = versionService.getUpdateInfo();
            if (null != update && Boolean.TRUE.equals(update.isAvailable())) {

                final boolean mandatory = Boolean.valueOf(update.isMandatory());

                LOGGER.info("An update is available (mandatory={})", mandatory);

                i = new Intent();
                i.setAction(NewVersionActivity.INTENT_UPGRADE);
                i.putExtra(NewVersionActivity.INTENT_EXTRA_MANDATORY_UPGRADE, mandatory);

            } else if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("No update available");
            }
        } catch (final Exception e) {
            // TJHU A network failure may happen here is the user isn't connected (see #688)
            // TJHU we must implement a service to handle this case and retry periodically (see
            // #688)
            LOGGER.error("Unable to check if a new version is available");
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
