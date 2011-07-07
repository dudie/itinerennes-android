package fr.itinerennes;

/**
 * Defines which log levels to enable / disable.
 * 
 * @author Jérémie Huchet
 */
public final class LogLevelConstants {

    /**
     * Avoid instanciation.
     */
    private LogLevelConstants() {

    }

    /** Enable/disable {@link Log#VERBOSE} level. */
    public static final boolean TRACE = false;

    /** Enable/disable {@link Log#DEBUG} level. */
    public static final boolean DEBUG = false;

    /** Enable/disable {@link Log#INFO} level. */
    public static final boolean INFO = false;

    /** Enable/disable {@link Log#WARN} level. */
    public static final boolean WARN = true;

    /** Enable/disable {@link Log#ERROR} level. */
    public static final boolean ERROR = true;
}
