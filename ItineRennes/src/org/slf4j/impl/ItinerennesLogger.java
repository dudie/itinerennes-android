package org.slf4j.impl;

import org.slf4j.helpers.MessageFormatter;

import android.util.Log;

/**
 * A simple logger using the package for the logging tag and prepends the name of the class which
 * log events to the messages.
 * 
 * @author Jérémie Huchet
 */
public class ItinerennesLogger extends AndroidLogger {

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
    public boolean isTraceEnabled() {

        return Log.isLoggable(name, Log.VERBOSE);
    }

    /* @see org.slf4j.Logger#trace(java.lang.String) */
    @Override
    public void trace(final String msg) {

        Log.v(name, msg);
    }

    /* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object) */
    @Override
    public void trace(final String format, final Object param1) {

        Log.v(name, format(format, param1, null));
    }

    /* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public void trace(final String format, final Object param1, final Object param2) {

        Log.v(name, format(format, param1, param2));
    }

    /* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Object[]) */
    @Override
    public void trace(final String format, final Object[] argArray) {

        Log.v(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#trace(java.lang.String, java.lang.Throwable) */
    @Override
    public void trace(final String msg, final Throwable t) {

        Log.v(name, msg, t);
    }

    /* @see org.slf4j.Logger#isDebugEnabled() */
    @Override
    public boolean isDebugEnabled() {

        return Log.isLoggable(name, Log.DEBUG);
    }

    /* @see org.slf4j.Logger#debug(java.lang.String) */
    @Override
    public void debug(final String msg) {

        Log.d(name, msg);
    }

    /* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object) */
    @Override
    public void debug(final String format, final Object arg1) {

        Log.d(name, format(format, arg1, null));
    }

    /* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public void debug(final String format, final Object param1, final Object param2) {

        Log.d(name, format(format, param1, param2));
    }

    /* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Object[]) */
    @Override
    public void debug(final String format, final Object[] argArray) {

        Log.d(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#debug(java.lang.String, java.lang.Throwable) */
    @Override
    public void debug(final String msg, final Throwable t) {

        Log.d(name, msg, t);
    }

    /* @see org.slf4j.Logger#isInfoEnabled() */
    @Override
    public boolean isInfoEnabled() {

        return Log.isLoggable(name, Log.INFO);
    }

    /* @see org.slf4j.Logger#info(java.lang.String) */
    @Override
    public void info(final String msg) {

        Log.i(name, msg);
    }

    /* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object) */
    @Override
    public void info(final String format, final Object arg) {

        Log.i(name, format(format, arg, null));
    }

    /* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public void info(final String format, final Object arg1, final Object arg2) {

        Log.i(name, format(format, arg1, arg2));
    }

    /* @see org.slf4j.Logger#info(java.lang.String, java.lang.Object[]) */
    @Override
    public void info(final String format, final Object[] argArray) {

        Log.i(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#info(java.lang.String, java.lang.Throwable) */
    @Override
    public void info(final String msg, final Throwable t) {

        Log.i(name, msg, t);
    }

    /* @see org.slf4j.Logger#isWarnEnabled() */
    @Override
    public boolean isWarnEnabled() {

        return Log.isLoggable(name, Log.WARN);
    }

    /* @see org.slf4j.Logger#warn(java.lang.String) */
    @Override
    public void warn(final String msg) {

        Log.w(name, msg);
    }

    /* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object) */
    @Override
    public void warn(final String format, final Object arg) {

        Log.w(name, format(format, arg, null));
    }

    /* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {

        Log.w(name, format(format, arg1, arg2));
    }

    /* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Object[]) */
    @Override
    public void warn(final String format, final Object[] argArray) {

        Log.w(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#warn(java.lang.String, java.lang.Throwable) */
    @Override
    public void warn(final String msg, final Throwable t) {

        Log.w(name, msg, t);
    }

    /* @see org.slf4j.Logger#isErrorEnabled() */
    @Override
    public boolean isErrorEnabled() {

        return Log.isLoggable(name, Log.ERROR);
    }

    /* @see org.slf4j.Logger#error(java.lang.String) */
    @Override
    public void error(final String msg) {

        Log.e(name, msg);
    }

    /* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object) */
    @Override
    public void error(final String format, final Object arg) {

        Log.e(name, format(format, arg, null));
    }

    /* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object, java.lang.Object) */
    @Override
    public void error(final String format, final Object arg1, final Object arg2) {

        Log.e(name, format(format, arg1, arg2));
    }

    /* @see org.slf4j.Logger#error(java.lang.String, java.lang.Object[]) */
    @Override
    public void error(final String format, final Object[] argArray) {

        Log.e(name, format(format, argArray));
    }

    /* @see org.slf4j.Logger#error(java.lang.String, java.lang.Throwable) */
    @Override
    public void error(final String msg, final Throwable t) {

        Log.e(name, msg, t);
    }

    /**
     * For formatted messages substitute arguments.
     * 
     * @param format
     * @param arg1
     * @param arg2
     */
    private String format(final String format, final Object arg1, final Object arg2) {

        return String.format("%s-30.30 - %s", this.classname,
                MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    /**
     * For formatted messages substitute arguments.
     * 
     * @param format
     * @param args
     */
    private String format(final String format, final Object[] args) {

        return String.format("%s-30.30 - %s", this.classname,
                MessageFormatter.arrayFormat(format, args).getMessage());
    }

}
