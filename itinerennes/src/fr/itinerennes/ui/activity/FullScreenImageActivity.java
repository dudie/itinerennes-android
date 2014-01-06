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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import fr.itinerennes.R;

/**
 * Displays an image in fullscreen mode.
 * 
 * @author Jérémie Huchet
 */
public final class FullScreenImageActivity extends ItineRennesActivity {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FullScreenImageActivity.class);

    /** Intent parameter to set the identifier of the drawable to display. */
    private static final String INTENT_PARAM_DRAWABLE_ID = String.format("%s.drawableId",
            FullScreenImageActivity.class.getName());

    /**
     * Generates an intent to open the {@link FullScreenImageActivity}.
     * 
     * @param packageContext
     *            the context
     * @param drawableId
     *            the identifier of the drawable to display
     * @return an intent to open the {@link FullScreenImageActivity}
     */
    public static Intent createIntent(final Context packageContext, final int drawableId) {

        final Intent i = new Intent(packageContext, FullScreenImageActivity.class);
        i.putExtra(INTENT_PARAM_DRAWABLE_ID, drawableId);
        return i;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final int drawableId = getIntent().getIntExtra(INTENT_PARAM_DRAWABLE_ID, -1);

        LOGGER.debug("onCreate.start - drawableId={}", drawableId);

        setContentView(R.layout.act_fullscreen_image);
        final ImageView image = (ImageView) findViewById(R.act_fullscreen_image.image);
        image.setImageResource(drawableId);

        LOGGER.debug("onCreate.end");
    }
}
