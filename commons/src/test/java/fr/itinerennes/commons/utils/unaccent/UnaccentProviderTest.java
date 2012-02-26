package fr.itinerennes.commons.utils.unaccent;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class for {@link UTFUnaccentProvider} and {@link CompatibleUnaccentProvider}.
 * 
 * @author Jérémie Huchet
 */
@RunWith(Parameterized.class)
public class UnaccentProviderTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(UnaccentProviderTest.class);

    /** The UTF based unaccent provider. */
    private final UTFUnaccentProvider utfProvider = new UTFUnaccentProvider();

    /** The compatible provider. */
    private final CompatibleUnaccentProvider compatProvider = new CompatibleUnaccentProvider();

    /** The string before the process. */
    private final String before;

    /** The expected string value after the process. */
    private final String expected;

    /**
     * Test constructor.
     * 
     * @param before
     *            the string before the unaccent process
     * @param expected
     *            the expected string value after the unaccent process
     */
    public UnaccentProviderTest(final String before, final String expected) {

        this.before = before;
        this.expected = expected;
    }

    /**
     * Initializes the test values.
     * 
     * @return a list of constructor parameters to initialize test cases
     */
    @Parameters
    public static List<Object[]> data() {

        final ArrayList<Object[]> data = new ArrayList<Object[]>();

        data.add(new Object[] { null, null });
        data.add(new Object[] { "", "" });
        data.add(new Object[] { " ", " " });
        data.add(new Object[] { "  ", "  " });
        data.add(new Object[] { "a", "a" });
        data.add(new Object[] { "1", "1" });
        data.add(new Object[] { "1a", "1a" });
        data.add(new Object[] { "éàô", "eao" });
        data.add(new Object[] { "ÉÀÙÇ", "EAUC" });
        data.add(new Object[] { "Abç, déf_Ùü'", "Abc, def_Uu'" });

        return data;
    }

    /**
     * Test for the {@link UTFUnaccentProvider}.
     */
    @Test
    public void testUTFUnaccentProvider() {

        assertEquals(expected, utfProvider.unaccent(before));
    }

    /**
     * Test for the {@link CompatibleUnaccentProvider}.
     */
    @Test
    public void testCompatibleUnaccentProvider() {

        assertEquals(expected, compatProvider.unaccent(before));
    }
}
