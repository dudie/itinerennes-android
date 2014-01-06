package fr.itinerennes.startup.version.model;

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
