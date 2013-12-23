package fr.itinerennes.ui.activity;

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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import fr.itinerennes.ItineRennesApplication;

/**
 * An abstract activity providing common functionalities such as automatic.
 * 
 * @author Jérémie Huchet
 */
public abstract class ItineRennesActivity extends SherlockFragmentActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ItineRennesActivity.class);

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
