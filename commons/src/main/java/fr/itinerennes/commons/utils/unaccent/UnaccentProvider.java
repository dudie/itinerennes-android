package fr.itinerennes.commons.utils.unaccent;

/*
 * [license]
 * Common tools
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
 * Provides a way to remove accents from a string.
 * 
 * @author Jérémie Huchet
 */
public interface UnaccentProvider {

    /**
     * Remove accentuated characters from the given String.
     * 
     * @param s
     *            the String from which accents must be removed.
     * @return the given String without accents
     */
    String unaccent(String s);
}
