package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/**
 * A logger factory producing {@link ItinerennesLogger}s.
 * 
 * @author Jérémie Huchet
 */
public final class AndroidLoggerFactory implements ILoggerFactory {

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

        Class<?> clazz = null;
        final String classFullName;
        try {
            clazz = Class.forName(name);
        } catch (final ClassNotFoundException e) {
            // nothing to do, the logger name will be used
        }

        if (clazz == null) {
            classFullName = name;
        } else {
            classFullName = clazz.getName();
        }

        return new ItinerennesLogger(classFullName);
    }

    /**
     * Gets a logger.
     * 
     * @param clazz
     *            the class which will send log messages to the logger.
     * @return a logger
     */
    public static Logger getLogger(final Class<?> clazz) {

        return new ItinerennesLogger(clazz.getName());
    }
}
