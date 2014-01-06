package fr.itinerennes.ui.views.overlays;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import fr.dudie.keolis.model.RelayParkState;

/**
 * Represents a park on the map.
 * 
 * @author Olivier Boudet
 */
public class ParkOverlayItem extends OverlayItem {

    /** Serial version UID. */
    private static final long serialVersionUID = -5727299964276508767L;

    /** The marker title. */
    private String label;

    /** Capacity of the car park. */
    private int capacity;

    /** Available spaces in the car park. */
    private int available;

    /** State of the par (open, closed or full). */
    private RelayParkState state;

    /**
     * Gets the label.
     * 
     * @return the label
     */
    public final String getLabel() {

        return label;
    }

    /**
     * Sets the label.
     * 
     * @param label
     *            the label to set
     */
    public final void setLabel(final String label) {

        this.label = label;
    }

    /**
     * Gets the capacity.
     * 
     * @return the capacity
     */
    public final int getCapacity() {

        return capacity;
    }

    /**
     * Sets the capacity.
     * 
     * @param capacity
     *            the capacity to set
     */
    public final void setCapacity(final int capacity) {

        this.capacity = capacity;
    }

    /**
     * Gets the available.
     * 
     * @return the available
     */
    public final int getAvailable() {

        return available;
    }

    /**
     * Sets the available.
     * 
     * @param available
     *            the available to set
     */
    public final void setAvailable(final int available) {

        this.available = available;
    }

    /**
     * Gets the state.
     * 
     * @return the state
     */
    public final RelayParkState getState() {

        return state;
    }

    /**
     * Sets the state.
     * 
     * @param relayParkState
     *            the state to set
     */
    public final void setState(final RelayParkState relayParkState) {

        this.state = relayParkState;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("ParkOverlayItem [label=");
        builder.append(label);
        builder.append(", capacity=");
        builder.append(capacity);
        builder.append(", available=");
        builder.append(available);
        builder.append(", state=");
        builder.append(state);
        builder.append(", getType()=");
        builder.append(getType());
        builder.append(", getLocation()=");
        builder.append(getLocation());
        builder.append("]");
        return builder.toString();
    }

}
