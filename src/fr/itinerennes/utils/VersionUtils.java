package fr.itinerennes.utils;

/**
 * Class providing some useful method to work with application versions.
 * 
 * @author Olivier Boudet
 */
public final class VersionUtils {

    /**
     * Private constructor to avoid instantiation.
     */
    private VersionUtils() {

    }

    /**
     * Compares the specified versions. Returns 0 if the two versions are equals. Returns a negative
     * integer if the second version is greater than the first. Returns a positive integer if the
     * first version is greater than the second.
     * 
     * @param v1
     *            first version
     * @param v2
     *            second version
     * @return 0 if the versions are equals, a negative integer if the second is greater than the
     *         first and a positive integer if the first is greater than the second.
     */
    public static int compare(final String v1, final String v2) {

        final String s1 = normalizeVersion(v1);
        final String s2 = normalizeVersion(v2);

        return s1.compareTo(s2);
    }

    /**
     * Deletes any dots from the specified string.
     * 
     * @param version
     *            version to normalize
     * @return a string without dots
     */
    public static String normalizeVersion(final String version) {

        return version.replaceAll("\\.", "");

    }

}
