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

import java.io.Serializable;

import org.osmdroid.util.GeoPoint;

/**
 * @author Olivier Boudet
 */
public class OverlayItem implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 570074780221962142L;

    /** The type of the item. */
    private String type;

    /** The marker position. */
    private GeoPoint location;

    /**
     * Gets the type.
     * 
     * @return the type
     */
    public final String getType() {

        return type;
    }

    /**
     * Sets the type.
     * 
     * @param type
     *            the type to set
     */
    public final void setType(final String type) {

        this.type = type;
    }

    /**
     * Gets the location.
     * 
     * @return the location
     */
    public final GeoPoint getLocation() {

        return location;
    }

    /**
     * Sets the location.
     * 
     * @param location
     *            the location to set
     */
    public final void setLocation(final GeoPoint location) {

        this.location = location;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("OverlayItem [type=");
        builder.append(type);
        builder.append(", location=");
        builder.append(location);
        builder.append("]");
        return builder.toString();
    }

}
