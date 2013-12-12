package fr.itinerennes.ui.activity;

import static com.xtremelabs.robolectric.Robolectric.shadowOf;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.view.View;
import android.widget.ListView;

import com.xtremelabs.robolectric.shadows.ShadowActivity;

import fr.itinerennes.R;
import fr.itinerennes.test.ItineRennesRobolelectricTestRunner;

@RunWith(ItineRennesRobolelectricTestRunner.class)
public class SampleRobolectricTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleRobolectricTest.class);

    private final NetworkAlertsActivity_ act = new NetworkAlertsActivity_();

    @Test
    @Ignore
    public void test() throws InterruptedException {

        act.onCreate(null);

        final ListView list = (ListView) act.findViewById(R.id.alerts_list);

        final View alert = list.getChildAt(2);
        alert.performClick();

        final ShadowActivity sact = shadowOf(act);
        System.out.println(sact.getLastShownDialogId());
    }
}
