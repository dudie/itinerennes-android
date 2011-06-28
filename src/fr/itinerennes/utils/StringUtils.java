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

            formNFDEnumValue = formClazz.getEnumConstants()[0];
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

        if (null == s) {
            return null;
        }

        try {
            return (String) NORMALIZE.invoke(null, s, NFD);
        } catch (final Exception e) {
            throw new UnsupportedOperationException(
                    "Unable to normalize the given string in this environment", e);
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

        if (null != s && isNormalizeSupported()) {
            return normalize(s).replaceAll("[\u0300-\u036F]", "");
        } else {
            return s;
        }
    }

    /**
     * Highlights the query in the given text using specified prefix and suffix.
     * <ul>
     * <li><b>text</b> Sample string where another string query must be highlighted</li>
     * <li><b>query</b> string</li>
     * <li><b>prefix</b> [p]</li>
     * <li><b>suffix</b> [s]</li>
     * </ul>
     * The result will be <i>Sample [p]string[s] where another [p]string[s] query must be
     * highlighted</i>.
     * 
     * @param text
     *            the text where query must be highlighted
     * @param query
     *            the query to highlight in the text
     * @param prefix
     *            the prefix to use to highlight the text
     * @param suffix
     *            the suffix to use to highlight the text
     */
    public static String highlight(final String text, final String query, final String prefix,
            final String suffix) {

        if (null == text) {
            return null;
        }

        final StringBuilder result = new StringBuilder();
        final String unaccentText = unaccent(text).toLowerCase();
        final String unaccentQuery = unaccent(query).toLowerCase().replaceAll("[^a-zA-Z0-9]", ".");

        int start = 0;
        int index;
        while ((index = unaccentText.indexOf(unaccentQuery, start)) != -1) {
            result.append(text.substring(start, index));
            result.append(prefix);
            result.append(text.substring(index, index + query.length()));
            result.append(suffix);
            start = index + query.length();
        }
        result.append(text.substring(start));

        return result.toString();
    }

}
