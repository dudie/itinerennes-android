package fr.itinerennes.utils;

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
    private final static Logger LOGGER = LoggerFactory.getLogger(StringUtils.class);

    private final static Method normalizerNormalizeMethod;

    private final static Object formNFDEnumValue;

    /*
     * Load a java.text.Normalizer instance to normalize UTF8 string and then remove diachritics
     * characters.
     */
    static {
        Method _normalizerNormalizeMethod = null;
        Object _formNFDEnumValue = null;
        try {
            final Class<?> normalizerClazz = Class.forName("java.text.Normalizer");
            final Class<?> formClazz = Class.forName("java.text.Normalizer$Form");

            _normalizerNormalizeMethod = normalizerClazz.getMethod("normalize", CharSequence.class,
                    formClazz);

            _formNFDEnumValue = formClazz.getEnumConstants()[0];
        } catch (final Throwable t) {
            LOGGER.info("No java.text.Normalizer instance found, UTF-8 string normalizing is not supported");
        }
        normalizerNormalizeMethod = _normalizerNormalizeMethod;
        formNFDEnumValue = _formNFDEnumValue;
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

    /**
     * Gets whether or not UTF-8 string normalizing is supported.
     * 
     * @return true is UTF-8 string normalizing is supported
     */
    public static boolean isNormalizeSupported() {

        return null != normalizerNormalizeMethod && null != formNFDEnumValue;
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

        try {
            return (String) normalizerNormalizeMethod.invoke(null, s, formNFDEnumValue);
        } catch (final Exception e) {
            throw new UnsupportedOperationException(
                    "Unable to normalize the given string in this environment", e);
        }
    }

    public static String unaccent(final String s) {

        if (isNormalizeSupported()) {
            return normalize(s).replaceAll("[\u0300-\u036F]", "");
        } else {
            return s;
        }
    }

}
