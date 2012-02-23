package fr.itinerennes.startup.version.model;

/**
 * Contains informations about the available (or not) update.
 * 
 * @author Jérémie Huchet
 */
public class UpdateInfo {

    /** When set to "true", an update is available. */
    private boolean available;

    /**
     * Defines whether or not the available update is mandatory. Do not have any meaning when
     * <code>{@link #available} == false</code>.
     */
    private boolean mandatory;

    /**
     * Gets the available.
     * 
     * @return the available
     */
    public final boolean isAvailable() {

        return available;
    }

    /**
     * Gets the mandatory.
     * 
     * @return the mandatory
     */
    public final boolean isMandatory() {

        return mandatory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("UpdateInfo [available=");
        builder.append(available);
        builder.append(", mandatory=");
        builder.append(mandatory);
        builder.append("]");
        return builder.toString();
    }

}
