package fr.itinerennes.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

/**
 * Some String function utilities.
 * 
 * @author Jérémie Huchet
 */
public final class StringUtils {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory.getLogger(StringUtils.class);

    /**
     * Private constructor to avoid instantiation.
     */
    private StringUtils() {

    }

    /**
     * Translates a string to a start case string.
     * <p>
     * <dt>example
     * <dl>
     * Translates a STRING to a start case string : Translates A String To A Start Case String
     * </dl>
     * 
     * @param s
     *            a string to translate in start case
     * @return the string "start case-ed"
     */
    public static String toStartCase(final String s) {

        if (null != s) {
            final char[] chars = s.toLowerCase().toCharArray();
            final Matcher m = Pattern.compile("\\w+").matcher(s);
            while (m.find()) {
                chars[m.start()] = Character.toUpperCase(chars[m.start()]);
            }
            return String.valueOf(chars);
        } else {
            return null;
        }
    }
}
