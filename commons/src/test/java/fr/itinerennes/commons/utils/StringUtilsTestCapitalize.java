package fr.itinerennes.commons.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@link StringUtils#capitalize(String)}.
 * 
 * @author Jérémie Huchet
 */
public final class StringUtilsTestCapitalize extends AbstractStringOperationTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StringUtilsTestCapitalize.class);

    /**
     * Test constructor.
     * 
     * @param before
     *            the string before the unaccent process
     * @param expected
     *            the expected string value after the unaccent process
     */
    public StringUtilsTestCapitalize(final String before, final String expected) {

        super(before, expected);
    }

    /**
     * Initialized the test values.
     * 
     * @return a list of constructor parameters to initialize test cases
     */
    @Parameters
    public static List<Object[]> data() {

        final ArrayList<Object[]> data = new ArrayList<Object[]>();

        data.add(new Object[] { null, null });
        // NO CAPITALIZATION FOR WORDS LESS OR EQUALS TO 2 CHARACTERS LONG
        data.add(new Object[] { "", "" });
        data.add(new Object[] { " ", " " });
        data.add(new Object[] { "  ", "  " });
        data.add(new Object[] { "a", "a" });
        data.add(new Object[] { "1", "1" });
        data.add(new Object[] { "1a", "1a" });
        data.add(new Object[] { "a1", "a1" });
        data.add(new Object[] { "ab1", "ab1" });
        data.add(new Object[] { "a b", "a b" });
        data.add(new Object[] { "ab", "ab" });
        data.add(new Object[] { "ab cd", "ab cd" });
        data.add(new Object[] { "d'un", "d'un" });
        // MORE THAN 2 CHARACTERS LONG WORDS ARE CAPITALIZED
        data.add(new Object[] { "abc", "Abc" });
        data.add(new Object[] { "abc def", "Abc Def" });
        data.add(new Object[] { "abc,def", "Abc,Def" });
        data.add(new Object[] { "d'abc", "d'Abc" });
        data.add(new Object[] { "abc1", "Abc1" });

        return data;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.commons.utils.AbstractStringOperationTestCase#executeStringOperation(java.lang.String)
     */
    @Override
    protected String executeStringOperation(final String before) {

        return StringUtils.capitalize(before);
    }
}
