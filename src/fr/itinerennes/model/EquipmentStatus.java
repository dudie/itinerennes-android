package fr.itinerennes.model;

import java.util.Date;

/**
 * Contains informations about an equipment state.
 * 
 * @author Jérémie Huchet
 */
public class EquipmentStatus {

    // TJHU est-ce qu'il faut fusionner ces informations dans fr.itinerennes.beans.Equipment ?

    /** The inactive state constant. */
    public static final int STATE_INACTIVE = 0;

    /** The active state constant. */
    public static final int STATE_ACTIVE = 1;

    /** The identifier of the equipment. */
    private String id;

    /** The state of the equipment. */
    private int state;

    /** The last update of the equipment state. */
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
     * Gets the state.
     * 
     * @return the state
     */
    public final int getState() {

        return state;
    }

    /**
     * Sets the state.
     * 
     * @param state
     *            the state to set
     */
    public final void setState(final int state) {

        this.state = state;
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
        builder.append("EquipmentStatus [id=");
        builder.append(id);
        builder.append(", state=");
        builder.append(state);
        builder.append(", lastUpdate=");
        builder.append(lastUpdate);
        builder.append("]");
        return builder.toString();
    }

}
