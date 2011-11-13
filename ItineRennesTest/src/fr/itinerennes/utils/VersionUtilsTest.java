package fr.itinerennes.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.utils.VersionUtils;

/**
 * @author orgoz
 */
public class VersionUtilsTest extends AndroidTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionUtilsTest.class);

    /**
     * Test case for {@link VersionUtils#compare(String, String)}
     */
    public final void testCompare() {

        int result;

        // compare equal versions
        result = VersionUtils.compare("1.0.0", "1.0.0");
        assertEquals(0, result);
        result = VersionUtils.compare("1.1.0", "1.1.0");
        assertEquals(0, result);
        result = VersionUtils.compare("1.0.2", "1.0.2");
        assertEquals(0, result);
        result = VersionUtils.compare("2.5.2", "2.5.2");
        assertEquals(0, result);
        result = VersionUtils.compare("2.0", "2.0");
        assertEquals(0, result);

        // first version lesser than the second
        result = VersionUtils.compare("1.0.0", "1.0.1");
        assertTrue(result < 0);
        result = VersionUtils.compare("1.1.0", "1.1.1");
        assertTrue(result < 0);
        result = VersionUtils.compare("2.0.2", "2.1.0");
        assertTrue(result < 0);
        result = VersionUtils.compare("2.5.2", "3.0.1");
        assertTrue(result < 0);
        result = VersionUtils.compare("3.0", "3.1");
        assertTrue(result < 0);

        // first version greater than the second
        result = VersionUtils.compare("1.0.1", "1.0.0");
        assertTrue(result > 0);
        result = VersionUtils.compare("2.1.0", "1.1.1");
        assertTrue(result > 0);
        result = VersionUtils.compare("2.1.0", "1.1.0");
        assertTrue(result > 0);
        result = VersionUtils.compare("3.5.7", "2.0.1");
        assertTrue(result > 0);
        result = VersionUtils.compare("0.2", "0.1");
        assertTrue(result > 0);
    }
}
