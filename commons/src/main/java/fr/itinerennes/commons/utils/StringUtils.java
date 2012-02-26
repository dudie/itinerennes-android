package fr.itinerennes.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.itinerennes.commons.utils.unaccent.CompatibleUnaccentProvider;
import fr.itinerennes.commons.utils.unaccent.UTFUnaccentProvider;
import fr.itinerennes.commons.utils.unaccent.UnaccentProvider;

/**
 * Some String function utilities.
 * 
 * @author Jérémie Huchet
 */
public final class StringUtils {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

    /** A tool to remove accents. */
    private static final UnaccentProvider unaccent;

    static {
        final UTFUnaccentProvider utfProvider = new UTFUnaccentProvider();
        if (utfProvider.isCompatible()) {
            unaccent = utfProvider;
        } else {
            unaccent = new CompatibleUnaccentProvider();
        }
    }

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
    public static String capitalize(final String s) {

        if (null != s) {
            final char[] chars = s.toLowerCase().toCharArray();
            final Matcher m = Pattern.compile("[^\\p{Punct}\\s\\d]{3,}").matcher(s);
            while (m.find()) {
                chars[m.start()] = Character.toUpperCase(chars[m.start()]);
            }
            return String.valueOf(chars);
        } else {
            return null;
        }
    }

    /**
     * Remove accents from the given string.
     * 
     * @param s
     *            the string you want to remove accents
     * @return the given string without special characters if operation is supported, else the same
     *         string as the given one
     */
    public static String unaccent(final String s) {

        if (null != s) {
            return unaccent.unaccent(s);
        } else {
            return s;
        }
    }

    /**
     * Same as {@link String#toLowerCase()} with null check.
     * 
     * @param s
     *            the string to convert to lowercase
     * @return the String, converted to lowercase
     */
    public static String toLowerCase(final String s) {

        if (null != s) {
            return s.toLowerCase();
        } else {
            return null;
        }
    }

    /**
     * Same as {@link String#toUpperCase()} with null check.
     * 
     * @param s
     *            the string to convert to uppercase
     * @return the String, converted to uppercase
     */
    public static String toUpperCase(final String s) {

        if (null != s) {
            return s.toUpperCase();
        } else {
            return null;
        }
    }

    /**
     * Check if the given string is empty or null.
     * 
     * @param s
     *            the string to apply the regexp
     * @return <code>true</code> if the given string is empty or null.
     */
    public static boolean isEmpty(final String s) {

        return null == s || "".equals(s);
    }

    /**
     * Check if the given string is blank or null.
     * <ul>
     * <li>"" is empty and blank</li>
     * <li>" " is not empty but blank</li>
     * <li>"        " is not empty but blank</li>
     * </ul>
     * 
     * @param s
     *            the string to apply the regexp
     * @return <code>true</code> if the given string is empty or null.
     */
    public static boolean isBlank(final String s) {

        return null == s || isEmpty(s.trim());
    }
}
