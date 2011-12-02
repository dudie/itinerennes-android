package fr.itinerennes.commons.utils;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Some String function utilities.
 * 
 * @author Jérémie Huchet
 */
public final class StringUtils {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

    /**
     * The method
     * <code>java.text.Normalizer#normalize(java.lang.String, java.text.Normalizer.Form)</code>.
     */
    private static final Method NORMALIZE;

    /** The enum value <code>java.text.Normalizer.Form.NFD</code>. */
    private static final Object NFD;

    /*
     * Load a java.text.Normalizer instance to normalize UTF8 string and then remove diachritics
     * characters.
     */
    static {
        Method normalizerNormalizeMethod = null;
        Object formNFDEnumValue = null;
        try {
            final Class<?> normalizerClazz = Class.forName("java.text.Normalizer");
            final Class<?> formClazz = Class.forName("java.text.Normalizer$Form");

            normalizerNormalizeMethod = normalizerClazz.getMethod("normalize", CharSequence.class,
                    formClazz);

            for (final Object enumValue : formClazz.getEnumConstants()) {
                if ("NFD".equals(enumValue.toString())) {
                    formNFDEnumValue = enumValue;
                }
            }
            if (null == formNFDEnumValue) {
                throw new IllegalStateException("Enum value for form NFD not found");
            }
        } catch (final Throwable t) {
            LOGGER.info("No java.text.Normalizer instance found, UTF-8 string normalizing is not supported");
        }
        NORMALIZE = normalizerNormalizeMethod;
        NFD = formNFDEnumValue;
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
     * Gets whether or not UTF-8 string normalizing is supported.
     * 
     * @return true is UTF-8 string normalizing is supported
     */
    public static boolean isNormalizeSupported() {

        return null != NORMALIZE && null != NFD;
    }

    /**
     * Normalizes the given string using the Canonical decomposition form (
     * <code>java.text.Normalizer.Form.NFD</code>).
     * 
     * @param s
     *            the string to normalize
     * @return the normalized string
     */
    public static String normalize(final String s) {

        String result = s;
        if (null != s && isNormalizeSupported()) {
            try {
                result = (String) NORMALIZE.invoke(null, s, NFD);
            } catch (final Exception e) {
                throw new UnsupportedOperationException(
                        "Unable to normalize the given string in this environment", e);
            }
        }
        return result;
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

        if (null != s && isNormalizeSupported()) {
            return normalize(s).replaceAll("[\u0300-\u036F]", "");
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
