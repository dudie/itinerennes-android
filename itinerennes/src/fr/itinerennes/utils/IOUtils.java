package fr.itinerennes.utils;

/*
 * [license]
 * ItineRennes
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.database.Cursor;

/**
 * Some utilities to deal with I/O resources.
 * 
 * @author Jérémie Huchet
 */
public final class IOUtils {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(IOUtils.class);

    /** Char buffer length for input reads. */
    private static final int CHAR_BUF_SIZE = 512;

    /** Byte buffer length for input reads. */
    private static final int BYTE_BUF_SIZE = 1024;

    /**
     * Private constructor to avoid instantiation.
     */
    private IOUtils() {

    }

    /**
     * Reads the given input stream and returns it as a string.
     * 
     * @param in
     *            an input stream to read
     * @return a string with the content provided by the given input stream
     */
    public static String read(final InputStream in) {

        final Reader reader = new InputStreamReader(in);
        final char[] buffer = new char[CHAR_BUF_SIZE];
        final StringBuilder script = new StringBuilder();
        int len = 0;
        try {
            while ((len = reader.read(buffer)) != -1) {
                script.append(buffer, 0, len);
            }
        } catch (final IOException e) {
            LOGGER.error("unable to read the input stream", e);
        }

        return script.toString();
    }

    /**
     * Reads the given input stream and returns it as an array of bytes.
     * 
     * @param in
     *            an input stream to read
     * @return an array of bytes with the content provided by the given input stream
     */
    public static byte[] readBytes(final InputStream in) {

        final byte[] buffer = new byte[BYTE_BUF_SIZE];
        final ByteArrayBuffer bytes = new ByteArrayBuffer(10 * BYTE_BUF_SIZE);
        int len = 0;
        try {
            while ((len = in.read(buffer)) != -1) {
                bytes.append(buffer, 0, len);
            }
        } catch (final IOException e) {
            LOGGER.error("unable to read the input stream", e);
        }

        return bytes.toByteArray();
    }

    /**
     * Closes an InputStream.
     * <p>
     * Equivalent to {@link Cursor#close()} but accept null values.
     * 
     * @param cursor
     *            the {@link Cursor} to close, may be null or already closed
     */
    public static void close(final Cursor cursor) {

        if (null != cursor && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
