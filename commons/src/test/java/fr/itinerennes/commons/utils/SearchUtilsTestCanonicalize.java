package fr.itinerennes.commons.utils;

import java.util.ArrayList;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@link SearchUtils#canonicalize(String)}.
 * 
 * @author Jérémie Huchet
 */
public class SearchUtilsTestCanonicalize extends AbstractStringOperationTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchUtilsTestCanonicalize.class);

    /**
     * Test constructor.
     * 
     * @param before
     *            the string before the canonicalizing process
     * @param expected
     *            the expected string value after the canonicalizing process
     */
    public SearchUtilsTestCanonicalize(final String before, final String expected) {

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
        data.add(new Object[] { "", "" });
        data.add(new Object[] { " ", "" });
        data.add(new Object[] { "  ", "" });
        data.add(new Object[] { "a", "a" });
        data.add(new Object[] { "A", "a" });
        data.add(new Object[] { "A b ", "ab" });
        data.add(new Object[] { " ,°+1a", "1a" });
        data.add(new Object[] { "à~' .bÇ  D?é:f", "abcdef" });

        return data;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.commons.utils.AbstractStringOperationTestCase#executeStringOperation(java.lang.String)
     */
    @Override
    protected String executeStringOperation(final String before) {

        return SearchUtils.canonicalize(before);
    }
}
