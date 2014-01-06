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

/**
 * Represents a marker on the map.
 * 
 * @author Jérémie Huchet
 * @author Olivier Boudet
 */
public class StopOverlayItem extends OverlayItem implements Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = 1L;

    /** The marker identifier. */
    private String id;

    /** The marker title. */
    private String label;

    /** Is this marker bookmarked ? */
    private boolean bookmarked;

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
     * Gets the bookmarked.
     * 
     * @return the bookmarked
     */
    public final boolean isBookmarked() {

        return bookmarked;
    }

    /**
     * Sets the bookmarked.
     * 
     * @param bookmarked
     *            the bookmarked to set
     */
    public final void setBookmarked(final boolean bookmarked) {

        this.bookmarked = bookmarked;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {

        final StringBuilder builder = new StringBuilder();
        builder.append("StopOverlayItem [id=");
        builder.append(id);
        builder.append(", label=");
        builder.append(label);
        builder.append(", bookmarked=");
        builder.append(bookmarked);
        builder.append(", getType()=");
        builder.append(getType());
        builder.append(", getLocation()=");
        builder.append(getLocation());
        builder.append("]");
        return builder.toString();
    }

}
