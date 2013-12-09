package fr.itinerennes.ui.preferences;

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
