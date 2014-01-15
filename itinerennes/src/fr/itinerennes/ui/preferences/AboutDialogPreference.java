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
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import fr.itinerennes.R;
import fr.itinerennes.api.client.model.FeedInfo;
import fr.itinerennes.ui.activity.ItineRennesActivity;
import fr.itinerennes.utils.VersionUtils;

/**
 * About dialog preference component.
 * 
 * @author Jérémie Huchet
 */
@EActivity(R.layout.act_about)
class AboutDialogPreference extends ItineRennesActivity {

	/** The event logger. */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(AboutDialogPreference.class);

	@ViewById(R.id.about_version_name)
	protected TextView versionText;

	@ViewById(R.id.about_keolis_version)
	protected TextView keolisVersion;

	@ViewById(R.id.about_keolis_version_pbar)
	protected ProgressBar keolisVersionProgressBar;

	@AfterViews
	protected void setVersionName() {
		versionText.setText(this.getString(R.string.version_dots,
				VersionUtils.getCurrent(this)));
		loadAndUpdateFeedInfo();
	}

	@Background
	protected void loadAndUpdateFeedInfo() {
		try {
			final FeedInfo fi = getApplicationContext()
					.getItineRennesApiClient().getFeedInfo();
			updateKeolisInfos(fi);
		} catch (final IOException e) {
			LOGGER.error("can't retrieve feed info", e);
		}
	}

	@UiThread
	protected void updateKeolisInfos(final FeedInfo fi) {
		keolisVersion.setText(String.format("(%s)", fi.getVersion()));
		keolisVersion.setVisibility(View.VISIBLE);
		keolisVersionProgressBar.setVisibility(View.GONE);
	}
}
