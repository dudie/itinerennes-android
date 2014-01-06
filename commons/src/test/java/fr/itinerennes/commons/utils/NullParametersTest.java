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

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This test class will pass <code>null</code> paramters to every methods of each utility class.
 * 
 * @author Jérémie Huchet
 */
@RunWith(Parameterized.class)
public final class NullParametersTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(NullParametersTest.class);

    /** Classes to test with null parameters. */
    @SuppressWarnings("rawtypes")
    private static final Class[] CLASSES = new Class[] { SearchUtils.class, StringUtils.class };

    /** A String representation of the method signature. */
    private final String signature;

    /** The method. */
    private final Method method;

    /**
     * Test constructor.
     * 
     * @param clazz
     *            the class
     * @param method
     *            the method
     */
    public NullParametersTest(final Class<?> clazz, final Method method) {

        final StringBuilder sign = new StringBuilder();
        sign.append(clazz.getSimpleName()).append("#");
        sign.append(method.getName()).append("(");
        final Iterator<Class<?>> i = Arrays.asList(method.getParameterTypes()).iterator();
        while (i.hasNext()) {
            final Class<?> paramType = i.next();
            sign.append(paramType.getSimpleName());
            if (i.hasNext()) {
                sign.append(", ");
            }
        }
        sign.append(")");

        this.signature = sign.toString();
        this.method = method;

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Initialize test for method {}", signature);
        }
    }

    /**
     * Initialized the test values.
     * 
     * @return a list of constructor parameters to initialize test cases
     */
    @Parameters
    public static List<Object[]> data() {

        final ArrayList<Object[]> data = new ArrayList<Object[]>();
        for (final Class<?> clazz : CLASSES) {
            for (final Method method : clazz.getMethods()) {

                if (method.getDeclaringClass().equals(clazz)
                        && method.getParameterTypes().length > 0) {
                    data.add(new Object[] { clazz, method });
                }
            }
        }
        return data;
    }

    /**
     * The the method with null arguments.
     */
    @Test
    public void testNullArguments() {

        final int paramCount = method.getParameterTypes().length;
        final Object[] params = new Object[paramCount];
        try {
            method.invoke(null, params);
        } catch (final Exception e) {
            LOGGER.error("Method {} failed for null arguments", signature);
            fail(String.format("Method %s failed for null arguments", signature));
        }
    }
}
