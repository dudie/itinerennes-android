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
 * Represents a bookmark.
 * 
 * @author Jérémie Huchet
 */
public class Bookmark {

    /** The identifier of the bookmarked resource. */
    private String id;

    /** The type of the bookmarked resource, like "fr.itinerennes.model.BusStation". */
    private String type;

    /** The label of the bookmark. */
    private String label;

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
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("Bookmark [id=");
        builder.append(id);
        builder.append(", type=");
        builder.append(type);
        builder.append(", label=");
        builder.append(label);
        builder.append("]");
        return builder.toString();
    }

}
