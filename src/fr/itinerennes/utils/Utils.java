package fr.itinerennes.utils;

/**
 * Some system utilites.
 * 
 * @author Jérémie Huchet
 */
public class Utils {

    /**
     * Returns the current system time in seconds since January 1, 1970 00:00:00 UTC.
     * <p>
     * Time is given by {@link System#currentTimeMillis()}.
     * 
     * @return the local system time in seconds.
     */
    public static long getCurrentTimeSeconds() {

        return System.currentTimeMillis() / 1000;
    }
}
