package fr.itinerennes.ui.activity;

import static com.xtremelabs.robolectric.Robolectric.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.view.View;
import android.widget.ListView;

import com.xtremelabs.robolectric.shadows.ShadowActivity;

import fr.itinerennes.R;

//@RunWith(RobolectricTestRunner.class)
public class SampleRobolectricTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleRobolectricTest.class);

    private final NetworkAlertsActivity act = new NetworkAlertsActivity();

    // @org.junit.Test
    public void test() {

        act.onCreate(null);

        final ListView list = (ListView) act.findViewById(R.id.alerts_list);

        final View alert = list.getChildAt(2);
        alert.performClick();

        final ShadowActivity sact = shadowOf(act);
        System.out.println(sact.getLastShownDialogId());
    }
}
