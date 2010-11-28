package fr.itinerennes.beans;

/**
 * Contains informations to retrieve the icon of a transport line.
 * 
 * @author Jérémie Huchet
 */
public class LineTransportIcon {

    /** The line identifier. */
    private String line;

    /** The URL to fetch the icon from. */
    private String iconUrl;

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
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("LineTransportIcon [line=");
        builder.append(line);
        builder.append(", iconUrl=");
        builder.append(iconUrl);
        builder.append("]");
        return builder.toString();
    }
}
