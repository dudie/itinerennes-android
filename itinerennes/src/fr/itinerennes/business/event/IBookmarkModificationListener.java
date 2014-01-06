package fr.itinerennes.business.event;

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
 * Implementing this interface allow a class to listen to bookmarks additions and removals.
 * 
 * @author Jérémie Huchet
 */
public interface IBookmarkModificationListener {

    /**
     * Called when a bookmark is removed or added. E.g. called when the bookmarked state of an
     * element is modified.
     * 
     * @param type
     *            the type of the bookmark
     * @param id
     *            the identifier of the bookmark
     * @param bookmarked
     *            true if the element pointed out by parameters <code>type</code> and
     *            <code>id</code> is bookmarked, else false
     */
    void onBookmarkStateChanged(String type, String id, boolean bookmarked);
}
