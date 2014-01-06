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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@link SearchUtils#highlight(String, String, String, String)}.
 * 
 * @author Jérémie Huchet
 */
@RunWith(Parameterized.class)
public class SearchUtilsTestHighlight {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchUtilsTestHighlight.class);

    /** The highlight prefix. */
    private static final String PREFIX = "_";

    /** The highlight suffix. */
    private static final String SUFFIX = "|";

    /** The text before the highlight process. */
    private final String text;

    /** The user query to highlight in the {@link #text}. */
    private final String query;

    /** The expected text after the highlight process finished. */
    private final String expectedText;

    /**
     * Test constructor.
     * 
     * @param text
     *            the string before the canonicalizing process
     * @param query
     *            the expected string value after the canonicalizing process
     */
    public SearchUtilsTestHighlight(final String text, final String query, final String expectedText) {

        this.text = text;
        this.query = query;
        this.expectedText = expectedText;
    }

    /**
     * Initialized the test values.
     * 
     * @return a list of constructor parameters to initialize test cases
     */
    @Parameters
    public static List<Object[]> data() {

        final ArrayList<Object[]> data = new ArrayList<Object[]>();

        // 1. text
        // 2. query
        // 3. expected text after highlight process
        data.add(new Object[] { null, null, null });
        data.add(new Object[] { null, "query", null });
        data.add(new Object[] { "some text", null, "some text" });
        data.add(new Object[] { "", "query", "" });
        data.add(new Object[] { "some text", "", "some text" });
        data.add(new Object[] { "some text", " ", "some text" });

        data.add(new Object[] { "query text", "query", "_query| text" });
        data.add(new Object[] { " query text", "query", " _query| text" });
        data.add(new Object[] { "text query", "query", "text _query|" });
        data.add(new Object[] { "text query ", "query", "text _query| " });
        data.add(new Object[] { "text query text", "query", "text _query| text" });

        data.add(new Object[] { "some text ee", "e", "som_e| t_e|xt _e|_e|" });
        data.add(new Object[] { "some queried text", "qù ér'", "some _quer|ied text" });
        data.add(new Object[] { "some queried text", "qu,#.-_)'", "some _qu|eried text" });
        data.add(new Object[] { "some queried text", "ed,#.-_)'text", "some queri_ed text|" });

        data.add(new Object[] { "some dots . dots", ".", "some dots . dots" });
        data.add(new Object[] { "some dots . dots", "dots.", "some _dots| . _dots|" });

        data.add(new Object[] { "Cimetiere Est", "ree", "Cimetie_re E|st" });

        return data;
    }

    /**
     * Test {@link SearchUtils#highlight(String, String, String, String)}.
     */
    @Test
    public final void testHighlight() {

        final String result = SearchUtils.highlight(text, query, PREFIX, SUFFIX);
        assertEquals("highlight with text [" + text + "] and query [" + query + "]", expectedText,
                result);
    }
}
