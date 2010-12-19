package fr.itinerennes.model;

import java.util.Arrays;

/**
 * Contains informations to retrieve the icon of a transport line.
 * 
 * @author Jérémie Huchet
 */
public class LineIcon implements Cacheable {

    /** The line identifier. */
    private String line;

    /** The URL to fetch the icon from. */
    private String iconUrl;

    /** The the icon bytes value. */
    private byte[] iconBytes;

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.model.Cacheable#getId()
     */
    @Override
    public final String getId() {

        return getLine();
    }

    /**
     * Gets the line identifier.
     * 
     * @return the line identifier
     */
    public final String getLine() {

        return line;
    }

    /**
     * Sets the line identifier.
     * 
     * @param line
     *            the line identifier to set
     */
    public final void setLine(final String line) {

        this.line = line;
    }

    /**
     * Gets the URL to fetch the icon from.
     * 
     * @return the URL to fetch the icon from
     */
    public final String getIconUrl() {

        return iconUrl;
    }

    /**
     * Sets the URL to fetch the icon from.
     * 
     * @param iconUrl
     *            the URL to fetch the icon to set
     */
    public final void setIconUrl(final String iconUrl) {

        this.iconUrl = iconUrl;
    }

    /**
     * Gets the icon bytes.
     * 
     * @return the icon bytes
     */
    public final byte[] getIconBytes() {

        return iconBytes;
    }

    /**
     * Sets the icon bytes.
     * 
     * @param iconBytes
     *            the icon bytes to set
     */
    public final void setIconBytes(final byte[] iconBytes) {

        this.iconBytes = iconBytes;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("LineIcon [line=");
        builder.append(line);
        builder.append(", iconUrl=");
        builder.append(iconUrl);
        builder.append(", iconBytes=");
        builder.append(Arrays.toString(iconBytes));
        builder.append("]");
        return builder.toString();
    }
}
