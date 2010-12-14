package fr.itinerennes.model;

import java.util.Date;

/**
 * Contains informations about a station equipment.
 * 
 * @author Jérémie Huchet
 */
public class Equipment {

    /** The elevator type. */
    public static final int TYPE_ELEVATOR = 0;

    /** The escalator type. */
    public static final int TYPE_ESCALATOR = 1;

    /** The equipment identifier. */
    private String id;

    /** The identifier of the station having this equipment. */
    private String stationId;

    /**
     * The type of the equipment.
     * 
     * @see {@link #TYPE_ELEVATOR}
     * @see {@link #TYPE_ESCALATOR}.
     */
    private String type;

    /** The start floor. */
    private int fromFloor;

    /** The end floor. */
    private int toFloor;

    /** The platform. */
    private int platform;

    /** The last update date of theses informations. */
    private Date lastUpdate;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public final String getId() {

        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     *            the id to set
     */
    public final void setId(final String id) {

        this.id = id;
    }

    /**
     * Gets the stationId.
     * 
     * @return the stationId
     */
    public final String getStationId() {

        return stationId;
    }

    /**
     * Sets the stationId.
     * 
     * @param stationId
     *            the stationId to set
     */
    public final void setStationId(final String stationId) {

        this.stationId = stationId;
    }

    /**
     * Gets the type.
     * 
     * @see {@link #TYPE_ELEVATOR}
     * @see {@link #TYPE_ESCALATOR}
     * @return the type
     */
    public final String getType() {

        return type;
    }

    /**
     * Sets the type.
     * 
     * @see {@link #TYPE_ELEVATOR}
     * @see {@link #TYPE_ESCALATOR}
     * @param type
     *            the type to set
     */
    public final void setType(final String type) {

        this.type = type;
    }

    /**
     * Gets the fromFloor.
     * 
     * @return the fromFloor
     */
    public final int getFromFloor() {

        return fromFloor;
    }

    /**
     * Sets the fromFloor.
     * 
     * @param fromFloor
     *            the fromFloor to set
     */
    public final void setFromFloor(final int fromFloor) {

        this.fromFloor = fromFloor;
    }

    /**
     * Gets the toFloor.
     * 
     * @return the toFloor
     */
    public final int getToFloor() {

        return toFloor;
    }

    /**
     * Sets the toFloor.
     * 
     * @param toFloor
     *            the toFloor to set
     */
    public final void setToFloor(final int toFloor) {

        this.toFloor = toFloor;
    }

    /**
     * Gets the platform.
     * 
     * @return the platform
     */
    public final int getPlatform() {

        return platform;
    }

    /**
     * Sets the platform.
     * 
     * @param platform
     *            the platform to set
     */
    public final void setPlatform(final int platform) {

        this.platform = platform;
    }

    /**
     * Gets the lastUpdate.
     * 
     * @return the lastUpdate
     */
    public final Date getLastUpdate() {

        return lastUpdate;
    }

    /**
     * Sets the lastUpdate.
     * 
     * @param lastUpdate
     *            the lastUpdate to set
     */
    public final void setLastUpdate(final Date lastUpdate) {

        this.lastUpdate = lastUpdate;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("Equipment [id=");
        builder.append(id);
        builder.append(", stationId=");
        builder.append(stationId);
        builder.append(", type=");
        builder.append(type);
        builder.append(", fromFloor=");
        builder.append(fromFloor);
        builder.append(", toFloor=");
        builder.append(toFloor);
        builder.append(", platform=");
        builder.append(platform);
        builder.append(", lastUpdate=");
        builder.append(lastUpdate);
        builder.append("]");
        return builder.toString();
    }

}
