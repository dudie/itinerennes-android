package fr.itinerennes.utils.xml;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.model.VersionCheck;

/**
 * Check version XML parser behavior.
 * 
 * @author Jérémie Huchet
 */
public final class XmlVersionParserTest extends AndroidTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlVersionParserTest.class);

    /** The tested XML parser. */
    private final XmlVersionParser parser = new XmlVersionParser();

    /**
     * Test the XML version parser with an XML file which doesn't contains expected elements.
     */
    public void testWrongFileFormat() {

        final InputStream versionFile = getClass().getResourceAsStream("version-format_wrong.xml");
        final VersionCheck check = parser.parse(versionFile);

        // TJHU what do we expect ? Send ACRA report ?
    }

    /**
     * Test the XML version parser with a file which is not an XML formatted file.
     */
    public void testNotXML() {

        final InputStream versionFile = getClass().getResourceAsStream("version-format_no_xml.xml");
        final VersionCheck check = parser.parse(versionFile);

        // TJHU what do we expect ? Send ACRA report ?
    }

    /**
     * Test the XML version parser with the file format for ItinéRennes version <= 0.4.1.
     */
    public void testOldFileFormat() {

        final InputStream versionFile = getClass().getResourceAsStream("version-format_api_4.xml");
        final VersionCheck check = parser.parse(versionFile);

        assertEquals("0.3", check.getMinRequired());
        assertEquals("0.4.1", check.getLatest());
    }

    /**
     * Test the XML version parser with the file format for ItinéRennes version > 0.5.x.
     */
    public void testNewFileFormat() {

        final InputStream versionFile = getClass().getResourceAsStream("version-format_api_7.xml");
        final VersionCheck check = parser.parse(versionFile);

        assertEquals("0.6", check.getMinRequired());
        assertEquals("0.8.2", check.getLatest());
    }
}
