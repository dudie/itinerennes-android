package fr.itinerennes.startup.version.model;

/**
 * Version management model object.
 * 
 * @author Jérémie Huchet
 */
public class PackageVersion {

    /** The optional update informations. */
    private UpdateInfo update;

    /**
     * Gets the update.
     * 
     * @return the update
     */
    public final UpdateInfo getUpdate() {

        return update;
    }

}
