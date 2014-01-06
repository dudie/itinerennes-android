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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jérémie Huchet
 */
public class CompatibleUnaccentProvider implements UnaccentProvider {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompatibleUnaccentProvider.class);

    /** An optional map containing replacement value for some characters. */
    private final Map<String, String> REPLACEMENTS;

    /**
     * Constructor.
     */
    public CompatibleUnaccentProvider() {

        REPLACEMENTS = new HashMap<String, String>();
        REPLACEMENTS.put("àáâãäå", "a");
        REPLACEMENTS.put("ÀÁÂÃÄÅ", "A");
        REPLACEMENTS.put("ç", "c");
        REPLACEMENTS.put("Ç", "C");
        REPLACEMENTS.put("èéêë", "e");
        REPLACEMENTS.put("ÈÉÊË", "E");
        REPLACEMENTS.put("ìíîï", "i");
        REPLACEMENTS.put("ÌÍÎÏ", "I");
        REPLACEMENTS.put("òóôõö", "o");
        REPLACEMENTS.put("ÒÓÔÕÖ", "O");
        REPLACEMENTS.put("ùúûü", "u");
        REPLACEMENTS.put("ÙÚÛÜ", "U");
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.commons.utils.unaccent.UnaccentProvider#unaccent(java.lang.String)
     */
    public String unaccent(final String s) {

        String result = s;
        if (null != result) {
            for (final Entry<String, String> replacement : REPLACEMENTS.entrySet()) {
                result = result.replaceAll(String.format("[%s]", replacement.getKey()),
                        replacement.getValue());
            }
        }
        return result;
    }
}
