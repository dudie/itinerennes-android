package fr.itinerennes.utils.xml;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import fr.itinerennes.model.VersionCheck;

/**
 * XML Parser for version check.
 * 
 * @author Olivier Boudet
 */
public class XmlVersionParser {

    /**
     * Parses the specified input stream using {@link XmlVersionHandler}.
     * 
     * @param input
     *            input stream to parse
     * @return {@link VersionCheck} object containing version informations.
     */
    public final VersionCheck parse(final InputStream input) {

        final SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            final SAXParser parser = factory.newSAXParser();
            final XmlVersionHandler handler = new XmlVersionHandler();
            parser.parse(input, handler);
            return handler.getVersionCheck();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
