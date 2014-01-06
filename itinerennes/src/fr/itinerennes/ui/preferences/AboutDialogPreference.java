package fr.itinerennes.ui.preferences;

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
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.utils.VersionUtils;

/**
 * About dialog preference component.
 * 
 * @author Jérémie Huchet
 */
public final class AboutDialogPreference extends DialogPreference {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AboutDialogPreference.class);

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     * @param attrs
     *            the xml attributes
     */
    public AboutDialogPreference(final Context context, final AttributeSet attrs) {

        super(context, attrs);
    }

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     * @param attrs
     *            the xml attributes
     * @param defStyle
     *            the default style
     */
    public AboutDialogPreference(final Context context, final AttributeSet attrs, final int defStyle) {

        super(context, attrs, defStyle);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.preference.DialogPreference#onCreateDialogView()
     */
    @Override
    protected View onCreateDialogView() {

        final View aboutView = super.onCreateDialogView();
        final TextView versionText = (TextView) aboutView.findViewById(R.id.about_version_name);
        versionText.setText(getContext().getString(R.string.version_dots,
                VersionUtils.getCurrent(this.getContext())));

        return aboutView;
    }
}
