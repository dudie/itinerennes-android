package fr.itinerennes.commons.utils;

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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facilities for search related manipulations.
 * 
 * @author Jérémie Huchet
 */
public class SearchUtils {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchUtils.class);

    /**
     * Private constructor to avoid instantiation.
     */
    private SearchUtils() {

    }

    /**
     * Canonicalize the given query.
     * <p>
     * Do the following operations:
     * <ul>
     * <li>remove accents/diachritics characters</li>
     * <li>remove all non alphanumeric characters (keeps only a-z0-9) <strong>and replace them with
     * _ SQL wildcard</strong></li>
     * <li><strong>add % SQL wildcard</strong> at beginning and ending</li>
     * </ul>
     * 
     * @param query
     *            the query string
     * @return the canonicalized query
     */
    public static String canonicalize(final String query) {

        if (null != query) {
            final String normalizedQuery = StringUtils.unaccent(query);
            if (null != normalizedQuery) {
                return String.format("%%%s%%",
                        normalizedQuery.toLowerCase().replaceAll("[^a-z0-9]", "_"));
            }
        }
        return null;
    }

    /**
     * Highlights the query in the given text using specified prefix and suffix.
     * <ul>
     * <li><b>text</b> Sample string where another string query must be highlighted</li>
     * <li><b>query</b> string</li>
     * <li><b>prefix</b> [p]</li>
     * <li><b>suffix</b> [s]</li>
     * </ul>
     * The result will be <i>Sample [p]string[s] where another [p]string[s] query must be
     * highlighted</i>.
     * 
     * @param text
     *            the text where query must be highlighted
     * @param query
     *            the query to highlight in the text
     * @param prefix
     *            the prefix to use to highlight the text
     * @param suffix
     *            the suffix to use to highlight the text
     */
    public static String highlight(final String text, final String query, final String prefix,
            final String suffix) {

        final String res;

        if (null == text) {
            // text is null, highlighted text is null
            res = null;
        } else if (StringUtils.isBlank(query)) {
            // query is blank, the is nothing to highlight
            res = text;
        } else {

            final StringBuilder result = new StringBuilder();
            final String unaccentText = StringUtils.toLowerCase(StringUtils.unaccent(text));

            final String queryPattern = buildQueryPattern(query);
            if (null != queryPattern) {
                final Matcher m = Pattern.compile(queryPattern).matcher(unaccentText);

                int lastEnd = 0;
                while (m.find()) {
                    result.append(text.substring(lastEnd, m.start()));
                    result.append(prefix);
                    result.append(text.substring(m.start(), m.end()));
                    result.append(suffix);
                    lastEnd = m.end();
                }
                result.append(text.substring(lastEnd));

                res = result.toString();
            } else {
                res = text;
            }
        }
        return res;
    }

    /**
     * Gets a regular expression to search to the given string query ignoring spaces.
     * <p>
     * Examples:
     * <p>
     * <em>"renn"</em> given as an input parameter the result value will be
     * <em>"(<b>r</b>\s*<b>e</b>\s*<b>n</b>\s*<b>n</b>)"</em>
     * <p>
     * <em>"rénn"</em> given as an input parameter the result value will be
     * <em>"(<b>r</b>\s*<b>.?</b>\s*<b>n</b>\s*<b>n</b>)"</em>
     * <p>
     * <em>"r,#n"</em> given as an input parameter the result value will be
     * <em>"(<b>r</b>\s*<b>n</b>)"</em>
     * <p>
     * In case query parameter <code>query</code> doesn't contains any alpnanumeric characters, the
     * query pattern returned is <code>null</code>.
     * 
     * @param unaccentQuery
     *            the text query
     * @return a regular expression to search to the given string query ignoring spaces
     */
    private static String buildQueryPattern(final String query) {

        final String unaccentQuery = StringUtils.toLowerCase(StringUtils.unaccent(query))
                .replaceAll("(\\s|\\p{Punct})", "").replaceAll("[^a-zA-Z0-9]", ".");

        final char[] queryChars = unaccentQuery.toCharArray();
        final StringBuilder pattern = new StringBuilder();

        for (int i = 0; i < queryChars.length; i++) {
            if ('.' == queryChars[i]) {
                pattern.append(".?");
            } else {
                pattern.append(queryChars[i]);
            }
            if (i < queryChars.length - 1) {
                pattern.append("\\s*");
            }
        }

        final String p;
        if (0 == pattern.length()) {
            p = null;
        } else {
            pattern.insert(0, '(');
            pattern.append(')');
            p = pattern.toString();
        }

        return p;
    }
}
