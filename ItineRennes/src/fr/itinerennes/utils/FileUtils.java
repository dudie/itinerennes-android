package fr.itinerennes.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.util.ByteArrayBuffer;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

/**
 * Some file utilities.
 * 
 * @author Jérémie Huchet
 */
public final class FileUtils {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(FileUtils.class);

    /** Char buffer length for input reads. */
    private static final int CHAR_BUF_SIZE = 512;

    /** Byte buffer length for input reads. */
    private static final int BYTE_BUF_SIZE = 1024;

    /**
     * Private constructor to avoid instantiation.
     */
    private FileUtils() {

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
}
