package fr.itinerennes.utils.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.itinerennes.model.VersionCheck;

/**
 * XML handler for version check.
 * 
 * @author Olivier Boudet
 */
public class XmlVersionHandler extends DefaultHandler {

    /** Name of the latest tag. */
    private static final String LATEST = "latest";

    /** Name of the min_required tag. */
    private static final String MIN_REQUIRED = "min_required";

    /** Name of document root element <code>itinerennes</code>. */
    private static final Object ITINERENNES = "itinerennes";

    /** Name of element version. */
    private static final Object VERSION = "version";

    /** StringBuilder used for each xml element. */
    private StringBuilder builder;

    /** Result object containing version informations. */
    private VersionCheck versionCheck;

    /**
     * Defines whether or not the parser is currently under the <code>itinerennes</code> document
     * root element.
     */
    private boolean isItinerennesElementActive;

    /**
     * Defines whether or not the parser is currently under the <code>itinerennes/version</code>
     * element.
     */
    private boolean isVersionElementActive;

    /**
     * {@inheritDoc}
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startDocument()
     */
    @Override
    public final void startDocument() throws SAXException {

        super.startDocument();
        versionCheck = new VersionCheck();
        builder = new StringBuilder();
        isItinerennesElementActive = false;
        isVersionElementActive = false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public final void startElement(final String uri, final String localName, final String name,
            final Attributes attributes) throws SAXException {

        super.startElement(uri, localName, name, attributes);

        builder.setLength(0);

        if (ITINERENNES.equals(localName)) {
            isItinerennesElementActive = true;
        } else if (VERSION.equals(localName) && isItinerennesElementActive) {
            isVersionElementActive = true;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
     */
    @Override
    public final void characters(final char[] ch, final int start, final int length)
            throws SAXException {

        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public final void endElement(final String uri, final String localName, final String name)
            throws SAXException {

        super.endElement(uri, localName, name);

        if (ITINERENNES.equals(localName)) {
            isItinerennesElementActive = false;
        } else if (isItinerennesElementActive) {
            if (VERSION.equals(localName)) {
                isVersionElementActive = false;
            } else if (LATEST.equalsIgnoreCase(localName) && isVersionElementActive) {
                versionCheck.setLatest(builder.toString());
            } else if (MIN_REQUIRED.equalsIgnoreCase(localName) && isVersionElementActive) {
                versionCheck.setMinRequired(builder.toString());
            }
        }

    }

    /**
     * Gets the versionCheck.
     * 
     * @return the versionCheck
     */
    public final VersionCheck getVersionCheck() {

        return versionCheck;
    }
}
