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

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@link StringUtils#unaccent(String)}.
 * 
 * @author Jérémie Huchet
 */
public final class StringUtilsTestUnaccent extends AbstractStringOperationTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtilsTestUnaccent.class);

    /**
     * Test constructor.
     * 
     * @param before
     *            the string before the unaccent process
     * @param expected
     *            the expected string value after the unaccent process
     */
    public StringUtilsTestUnaccent(final String before, final String expected) {

        super(before, expected);
    }

    /**
     * Initializes the test values.
     * 
     * @return a list of constructor parameters to initialize test cases
     */
    @Parameters
    public static List<Object[]> data() {

        final ArrayList<Object[]> data = new ArrayList<Object[]>();

        data.add(new Object[] { null, null });
        data.add(new Object[] { "", "" });
        data.add(new Object[] { " ", " " });
        data.add(new Object[] { "  ", "  " });
        data.add(new Object[] { "a", "a" });
        data.add(new Object[] { "1", "1" });
        data.add(new Object[] { "1a", "1a" });
        data.add(new Object[] { "éàô", "eao" });
        data.add(new Object[] { "ÉÀÙÇ", "EAUC" });
        data.add(new Object[] { "Abç, déf_Ùü'", "Abc, def_Uu'" });

        return data;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.commons.utils.AbstractStringOperationTestCase#executeStringOperation(java.lang.String)
     */
    @Override
    protected String executeStringOperation(final String before) {

        return StringUtils.unaccent(before);
    }
}
