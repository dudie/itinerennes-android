package fr.itinerennes.utils.xml;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.itinerennes.model.VersionCheck;

/**
 * Check version XML parser behavior.
 * 
 * @author Jérémie Huchet
 */
public final class XmlVersionParserTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlVersionParserTest.class);

    /** The tested XML parser. */
    private final XmlVersionParser parser = new XmlVersionParser();

    /**
     * Test the XML version parser with an XML file which doesn't contains expected elements.
     */
    @Test
    public void testWrongFileFormat() {

        final InputStream versionFile = getClass().getResourceAsStream("version-format_wrong.xml");
        final VersionCheck check = parser.parse(versionFile);

        // TJHU what do we expect ? Send ACRA report ?
        assertNull(check.getLatest());
        assertNull(check.getMinRequired());
    }

    /**
     * Test the XML version parser with a file which is not an XML formatted file.
     */
    // @Test
    public void testNotXML() {

        final InputStream versionFile = getClass().getResourceAsStream("version-format_no_xml.xml");
        final VersionCheck check = parser.parse(versionFile);

        // TJHU what do we expect ? Send ACRA report ?
    }

    /**
     * Test the XML version parser with the file format for ItinéRennes version <= 0.4.1.
     * <p>
     * The parser shouldn't be able to extract min and latest version because it expects the
     * namespace of elements to be the right one.
     */
    @Test
    public void testOldFileFormat() {

        final InputStream versionFile = getClass().getResourceAsStream("version-format_api_4.xml");
        final VersionCheck check = parser.parse(versionFile);

        assertNull(check.getMinRequired());
        assertNull(check.getLatest());
    }
}
