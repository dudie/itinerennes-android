package fr.itinerennes.model;

/**
 * Simple object used to store informations concerning versions available and required.
 * 
 * @author Olivier Boudet
 */
public class VersionCheck {

    /** Latest version available. */
    private String latest;

    /** Minimum version required. */
    private String minRequired;

    /**
     * Gets the latest version available.
     * 
     * @return the latest
     */
    public String getLatest() {

        return latest;
    }

    /**
     * Sets the latest version available.
     * 
     * @param latest
     *            the latest to set
     */
    public void setLatest(final String latest) {

        this.latest = latest;
    }

    /**
     * Gets the minimum version required.
     * 
     * @return the minRequired
     */
    public String getMinRequired() {

        return minRequired;
    }

    /**
     * Sets the minimum version required.
     * 
     * @param minRequired
     *            the minRequired to set
     */
    public void setMinRequired(final String minRequired) {

        this.minRequired = minRequired;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("VersionCheck [latest=");
        builder.append(latest);
        builder.append(", minRequired=");
        builder.append(minRequired);
        builder.append("]");
        return builder.toString();
    }
}
