package fr.itinerennes.commons.utils.unaccent;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jérémie Huchet
 */
public final class UTFUnaccentProvider implements UnaccentProvider {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UTFUnaccentProvider.class);

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
     * Normalizes the given string using the Canonical decomposition form (
     * <code>java.text.Normalizer.Form.NFD</code>).
     * 
     * @param s
     *            the string to normalize
     * @return the normalized string
     */
    private String normalize(final String s) {

        String result = s;
        try {
            result = (String) NORMALIZE.invoke(null, s, NFD);
        } catch (final Exception e) {
            throw new UnsupportedOperationException(
                    "Unable to normalize the given string in this environment", e);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.commons.utils.unaccent.UnaccentProvider#unaccent(java.lang.String)
     */
    public String unaccent(final String s) {

        if (null != s) {
            return normalize(s).replaceAll("[\u0300-\u036F]", "");
        } else {
            return null;
        }
    }

    /**
     * Gets whether or not this provider is functional.
     * 
     * @return true if this provider is fully functional
     */
    public boolean isCompatible() {

        return null != NORMALIZE && null != NFD;
    }
}
