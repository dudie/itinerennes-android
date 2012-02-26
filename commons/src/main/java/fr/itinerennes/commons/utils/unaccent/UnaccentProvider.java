package fr.itinerennes.commons.utils.unaccent;

/**
 * Provides a way to remove accents from a string.
 * 
 * @author Jérémie Huchet
 */
public interface UnaccentProvider {

    /**
     * Remove accentuated characters from the given String.
     * 
     * @param s
     *            the String from which accents must be removed.
     * @return the given String without accents
     */
    String unaccent(String s);
}
