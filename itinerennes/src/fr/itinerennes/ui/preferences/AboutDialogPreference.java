package fr.itinerennes.ui.preferences;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

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
public class AboutDialogPreference extends ItineRennesActivity {

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
