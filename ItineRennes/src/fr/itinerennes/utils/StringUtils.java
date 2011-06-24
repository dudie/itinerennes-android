package fr.itinerennes.utils;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some String function utilities.
 * 
 * @author Jérémie Huchet
 */
public final class StringUtils {

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
     * Removes all accents from the given string.
     * 
     * @param s
     *            a string
     * @return the given string without any special character
     */
    public static String unAccent(final String s) {
        final String unAccentString;
        if (null != s) {
        	unAccentString = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
        } else {
            unAccentString = null;
        }
        return unAccentString;
    }
    
	/**
	 * Highlights the query in the given text using specified prefix and suffix.
	 * <ul>
	 * <li><b>text</b> Sample string where another string query must be highlighted</li>
	 * <li><b>query</b> string</li>
	 * <li><b>prefix</b> [p]</li>
	 * <li><b>suffix</b> [s]</li>
	 * </ul>
	 * The result will be <i>Sample [p]string[s] where another [p]string[s] query must be highlighted</i>.
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
    public static String highlight(String text, String query, String prefix, String suffix) {
    	StringBuilder result = new StringBuilder();
    	String unaccentText = unAccent(text).toLowerCase();
    	String unaccentQuery = unAccent(query).toLowerCase().replaceAll("[^a-zA-Z0-9]", ".");
    	
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
