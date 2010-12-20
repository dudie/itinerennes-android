package org.slf4j.impl;

import org.slf4j.helpers.MessageFormatter;

import android.util.Log;

import fr.itinerennes.LogLevelConstants;

/**
 * A simple logger using the package for the logging tag and prepends the name of the class which
 * log events to the messages.
 * 
 * @author Jérémie Huchet
 */
public class ItinerennesLogger extends AndroidLogger {

    /** The serial version UID. */
    private static final long serialVersionUID = -922236915857722949L;

    /** The name of the class using this logger. */
    private final String classname;

    /**
     * Creates a new logger.
     * 
     * @param tag
     *            the tag name
     * @param classname
     *            the name of the class using this logger
     */
    ItinerennesLogger(final String tag, final String classname) {

        super(tag);
        this.classname = classname;
    }

    /* @see org.slf4j.Logger#isTraceEnabled() */
    @Override
    public final boolean isTraceEnabled() {

        return LogLevelConstants.TRACE || Log.isLoggable(name, Log.VERBOSE);
    }

    /* @see org.slf4j.Logger#trace(java.lang.String) */
    @Override
    public final void trace(final String msg) {

        Log.v(name, format(msg));
    }

    /* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object) */
    @Override
    public final void trace(final String format, final Object param1) {

        Log.v(name, format(format, param1, null));
    }

    /* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public final void trace(final String format, final Object param1, final Object param2) {

        Log.v(name, format(format, param1, param2));
    }

    /* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[]) */
    @Override
    public final void trace(final String format, final Object[] argArray) {

        Log.v(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable) */
    @Override
    public final void trace(final String msg, final Throwable t) {

        Log.v(name, format(msg), t);
    }

    /* @see org.slf4j.Logger#isDebugEnabled() */
    @Override
    public final boolean isDebugEnabled() {

        return LogLevelConstants.DEBUG || Log.isLoggable(name, Log.DEBUG);
    }

    /* @see org.slf4j.Logger#debug(java.lang.String) */
    @Override
    public final void debug(final String msg) {

        Log.d(name, format(msg));
    }

    /* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object) */
    @Override
    public final void debug(final String format, final Object arg1) {

        Log.d(name, format(format, arg1, null));
    }

    /* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public final void debug(final String format, final Object param1, final Object param2) {

        Log.d(name, format(format, param1, param2));
    }

    /* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[]) */
    @Override
    public final void debug(final String format, final Object[] argArray) {

        Log.d(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable) */
    @Override
    public final void debug(final String msg, final Throwable t) {

        Log.d(name, format(msg), t);
    }

    /* @see org.slf4j.Logger#isInfoEnabled() */
    @Override
    public final boolean isInfoEnabled() {

        return LogLevelConstants.INFO || Log.isLoggable(name, Log.INFO);
    }

    /* @see org.slf4j.Logger#info(java.lang.String) */
    @Override
    public final void info(final String msg) {

        Log.i(name, format(msg));
    }

    /* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object) */
    @Override
    public final void info(final String format, final Object arg) {

        Log.i(name, format(format, arg, null));
    }

    /* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public final void info(final String format, final Object arg1, final Object arg2) {

        Log.i(name, format(format, arg1, arg2));
    }

    /* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[]) */
    @Override
    public final void info(final String format, final Object[] argArray) {

        Log.i(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable) */
    @Override
    public final void info(final String msg, final Throwable t) {

        Log.i(name, format(msg), t);
    }

    /* @see org.slf4j.Logger#isWarnEnabled() */
    @Override
    public final boolean isWarnEnabled() {

        return LogLevelConstants.WARN || Log.isLoggable(name, Log.WARN);
    }

    /* @see org.slf4j.Logger#warn(java.lang.String) */
    @Override
    public final void warn(final String msg) {

        Log.w(name, format(msg));
    }

    /* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object) */
    @Override
    public final void warn(final String format, final Object arg) {

        Log.w(name, format(format, arg, null));
    }

    /* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public final void warn(final String format, final Object arg1, final Object arg2) {

        Log.w(name, format(format, arg1, arg2));
    }

    /* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[]) */
    @Override
    public final void warn(final String format, final Object[] argArray) {

        Log.w(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable) */
    @Override
    public final void warn(final String msg, final Throwable t) {

        Log.w(name, format(msg), t);
    }

    /* @see org.slf4j.Logger#isErrorEnabled() */
    @Override
    public final boolean isErrorEnabled() {

        return LogLevelConstants.ERROR || Log.isLoggable(name, Log.ERROR);
    }

    /* @see org.slf4j.Logger#error(java.lang.String) */
    @Override
    public final void error(final String msg) {

        Log.e(name, format(msg));
    }

    /* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object) */
    @Override
    public final void error(final String format, final Object arg) {

        Log.e(name, format(format, arg, null));
    }

    /* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public final void error(final String format, final Object arg1, final Object arg2) {

        Log.e(name, format(format, arg1, arg2));
    }

    /* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[]) */
    @Override
    public final void error(final String format, final Object[] argArray) {

        Log.e(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable) */
    @Override
    public final void error(final String msg, final Throwable t) {

        Log.e(name, format(msg), t);
    }

    /**
     * Prepends the class name to the message.
     * 
     * @param message
     *            a message
     * @return the given message preceded by the logger class name
     */
    private String format(final String message) {

        return String.format("%-30.30s %s", this.classname, message);
    }

    /**
     * For formatted messages substitute arguments.
     * 
     * @param format
     *            a message, use <code>{}</code> to insert arguments
     * @param arg1
     *            will replace the first <code>{}</code>
     * @param arg2
     *            will replace the second <code>{}</code>
     * @return the string with arguments inserted
     */
    private String format(final String format, final Object arg1, final Object arg2) {

        return format(MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    /**
     * For formatted messages substitute arguments.
     * 
     * @param format
     *            a message, use <code>{}</code> to insert arguments
     * @param args
     *            will replace <code>{}</code>s
     * @return the string with arguments inserted
     */
    private String format(final String format, final Object[] args) {

        return format(MessageFormatter.arrayFormat(format, args).getMessage());
    }

}
