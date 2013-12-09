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

import android.app.Activity;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import fr.itinerennes.R;
import fr.itinerennes.utils.VersionUtils;

/**
 * About dialog preference component.
 * 
 * @author Jérémie Huchet
 */
@EActivity(R.layout.act_about)
public class AboutDialogPreference extends Activity {

	/** The event logger. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AboutDialogPreference.class);

	@ViewById(R.id.about_version_name)
	protected TextView versionText;

	@AfterViews
	protected void setVersionName() {
		versionText.setText(this.getString(R.string.version_dots,
				VersionUtils.getCurrent(this)));
	}
}
