package fr.itinerennes.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.drawable.Drawable;
import fr.itinerennes.R;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.exceptions.GenericException;

/**
 * @author Jérémie Huchet
 */
@RunWith(RobolectricTestRunner.class)
public class LineIconServiceTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LineIconServiceTest.class);

    private final Map<Integer, String> iconMapping = new HashMap<Integer, String>();

    private LineIconService iconService;

    public LineIconServiceTest() {

        iconMapping.put(R.drawable.z_ic_line_1, "1");
        iconMapping.put(R.drawable.z_ic_line_9, "9");
        iconMapping.put(R.drawable.z_ic_line_40, "40");
        iconMapping.put(R.drawable.z_ic_line_40, "40EX");
    }

    @Before
    public void setup() {

        iconService = new LineIconService(Robolectric.application, null);
    }

    @Test
    public void testResolveIcon() throws GenericException {

        for (final java.util.Map.Entry<Integer, String> mapping : iconMapping.entrySet()) {

            final int resId = mapping.getKey();
            final String name = mapping.getValue();

            final Drawable expectedDrawable = Robolectric.application.getResources().getDrawable(resId);

            final String msg = String.format("check drawable for icon name '%s'", name);
            assertEquals(msg, expectedDrawable, iconService.getIcon(name));
        }
    }

    @Test
    public void testResolveUnknownIcon() throws GenericException {

        assertNotNull(iconService.getIcon(""));
        assertNotNull(iconService.getIcon("unknown"));
    }
}
