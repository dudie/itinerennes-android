package fr.itinerennes.model;

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
