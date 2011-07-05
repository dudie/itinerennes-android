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

    /** StringBuilder used for each xml element. */
    private StringBuilder builder;

    /** Result object containing version informations. */
    private VersionCheck versionCheck;

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
        if (localName.equalsIgnoreCase(LATEST)) {
            versionCheck.setLatest(builder.toString());
        } else if (localName.equalsIgnoreCase(MIN_REQUIRED)) {
            versionCheck.setMinRequired(builder.toString());
        }

    }

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
