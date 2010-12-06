package org.slf4j.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * A logger factory producing {@link ItinerennesLogger}s.
 * 
 * @author Jérémie Huchet
 */
public final class ItinerennesLoggerFactory implements ILoggerFactory {

    /** The base string to remove from log tags. */
    private static final String PKG_BASE = "fr.itinerennes.";

    /**
     * Private constructor to avoid instantiation.
     */
    private ItinerennesLoggerFactory() {

    }

    /**
     * Gets a logger.
     * 
     * @param name
     *            the logger name
     * @return a logger
     * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
     */
    @Override
    public Logger getLogger(final String name) {

        return new ItinerennesLogger(getTag(name), getName(name));
    }

    /**
     * Gets a logger.
     * 
     * @param clazz
     *            the class which will send log messages to the logger.
     * @return a logger
     */
    public static Logger getLogger(final Class<?> clazz) {

        return new ItinerennesLogger(getTag(clazz.getPackage().getName()), clazz.getSimpleName());
    }

    /**
     * Gets a simplified package name.
     * 
     * @param packageName
     *            the package name
     * @return the package name substituted from {@value #PKG_BASE}
     */
    private static String getTag(final String packageName) {

        // removes base package name
        if (packageName.startsWith(PKG_BASE)) {
            return packageName.substring(PKG_BASE.length());
        } else {
            return packageName;
        }
    }

    /**
     * Gets a simplified class name.
     * 
     * @param name
     *            the name
     * @return a name matching [a-zA-Z0-9]*
     */
    private static String getName(final String name) {

        final Matcher m = Pattern.compile("\\w*$").matcher(name);
        if (m.matches()) {
            return m.group(m.groupCount());
        } else {
            return name;
        }
    }
}
