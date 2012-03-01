package fr.itinerennes;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Easy access to itinerennes.properties configuration.
 * 
 * @author Jérémie Huchet
 */
public final class Conf {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(Conf.class);

    /** True if ACRA reporting is enabled. */
    public static final boolean ACRA_ENABLED;

    static {
        final Properties props = new Properties();
        try {
            props.load(Conf.class.getClassLoader().getResourceAsStream(
                    "assets/itinerennes.properties"));

            ACRA_ENABLED = Boolean.valueOf(props.getProperty("acra.enabled"));

        } catch (final Throwable t) {
            throw new IllegalStateException("Can't load itinerennes.properties configuration file",
                    t);
        }
    }

    /**
     * Private constructor to avoid instantiation.
     */
    private Conf() {

    }

    /**
     * Outputs the current configuration to the logger.
     */
    public static void info() {

        if (LOGGER.isDebugEnabled()) {
            final StringBuilder conf = new StringBuilder();
            conf.append("acra.enabled = ").append(ACRA_ENABLED).append("\n");
            LOGGER.info("Current configuration: \n{}", conf);
        }
    }
}
