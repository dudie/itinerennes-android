package org.slf4j.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import android.util.Log;

/**
 * A logger factory producing {@link ItinerennesLogger}s.
 * 
 * @author Jérémie Huchet
 */
public final class AndroidLoggerFactory implements ILoggerFactory {

    /** The base string to remove from log tags. */
    private static final String PKG_BASE = "fr.itinerennes.";

    /** The prefix prepended to all log tags. */
    public static final String PREFIX = "ITR.";

    /** The maximum tag length. */
    private static final int MAX_TAG_LENGTH = 23;

    /**
     * Constructor to avoid instantiation from anywhere.
     */
    AndroidLoggerFactory() {

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
            return truncate(packageName.substring(PKG_BASE.length()));
        } else {
            return truncate(packageName);
        }
    }

    /**
     * Truncates the given pakage name until its length is lower or equals to the maximum tag length
     * ({@link #MAX_TAG_LENGTH}).
     * 
     * @param packageName
     *            the package name to truncate
     * @return the truncated package name
     */
    private static String truncate(final String packageName) {

        int size = PREFIX.length() + packageName.length();

        if (size <= MAX_TAG_LENGTH) {
            return PREFIX + packageName;
        }

        final String[] names = packageName.split("\\.");

        while (size > MAX_TAG_LENGTH) {
            int longestIdx = -1;
            for (int i = 0; i < names.length; i++) {
                if (-1 == longestIdx
                        || (i != names.length - 1 && names[i].length() > names[longestIdx].length())) {
                    longestIdx = i;
                }
            }
            names[longestIdx] = names[longestIdx].replaceFirst("..$", "*");
            size -= 1;
        }

        final StringBuilder truncatedName = new StringBuilder();
        truncatedName.append(PREFIX).deleteCharAt(truncatedName.length() - 1);

        for (final String name : names) {
            truncatedName.append(".");
            truncatedName.append(name);
        }

        if (Log.isLoggable(PREFIX + "LoggerFactory", Log.INFO)) {
            Log.i(AndroidLoggerFactory.class.getSimpleName(), "Log tag " + PREFIX + packageName
                    + " is longer than 23 characters, using " + truncatedName.toString()
                    + " instead");
        }

        return truncatedName.toString();
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
