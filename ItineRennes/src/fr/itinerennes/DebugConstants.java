package fr.itinerennes;

/**
 * Defines which log levels to enable / disable.
 * 
 * @author Jérémie Huchet
 */
public final class DebugConstants {

    /**
     * Avoid instanciation.
     */
    private DebugConstants() {

    }

    /** Enable/disable Acra bug reports. */
    public static final boolean ENABLE_ACRA_BUG_REPORTS = true;

    /** Enable/disable {@link Log#VERBOSE} level. */
    public static final boolean LOG_TRACE = false;

    /** Enable/disable {@link Log#LOG_DEBUG} level. */
    public static final boolean LOG_DEBUG = false;

    /** Enable/disable {@link Log#LOG_INFO} level. */
    public static final boolean LOG_INFO = false;

    /** Enable/disable {@link Log#LOG_WARN} level. */
    public static final boolean LOG_WARN = false;

    /** Enable/disable {@link Log#LOG_ERROR} level. */
    public static final boolean LOG_ERROR = false;
}
