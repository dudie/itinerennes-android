package fr.itinerennes.commons.utils.unaccent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jérémie Huchet
 */
public class CompatibleUnaccentProvider implements UnaccentProvider {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(CompatibleUnaccentProvider.class);

    /** An optional map containing replacement value for some characters. */
    private final Map<String, String> REPLACEMENTS;

    /**
     * Constructor.
     */
    public CompatibleUnaccentProvider() {

        REPLACEMENTS = new HashMap<String, String>();
        REPLACEMENTS.put("àáâãäå", "a");
        REPLACEMENTS.put("ÀÁÂÃÄÅ", "A");
        REPLACEMENTS.put("ç", "c");
        REPLACEMENTS.put("Ç", "C");
        REPLACEMENTS.put("èéêë", "e");
        REPLACEMENTS.put("ÈÉÊË", "E");
        REPLACEMENTS.put("ìíîï", "i");
        REPLACEMENTS.put("ÌÍÎÏ", "I");
        REPLACEMENTS.put("òóôõö", "o");
        REPLACEMENTS.put("ÒÓÔÕÖ", "O");
        REPLACEMENTS.put("ùúûü", "u");
        REPLACEMENTS.put("ÙÚÛÜ", "U");
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.commons.utils.unaccent.UnaccentProvider#unaccent(java.lang.String)
     */
    public String unaccent(final String s) {

        String result = s;
        if (null != result) {
            for (final Entry<String, String> replacement : REPLACEMENTS.entrySet()) {
                result = result.replaceAll(String.format("[%s]", replacement.getKey()),
                        replacement.getValue());
            }
        }
        return result;
    }
}
