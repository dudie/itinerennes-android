package fr.itinerennes.commons.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract helper class to implement test cases for String operation methods. Signature of methods
 * this class will help to test should typically look like : <code>String method(String);</code>
 * 
 * @author Jérémie Huchet
 */
@RunWith(Parameterized.class)
public abstract class AbstractStringOperationTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractStringOperationTestCase.class);

    /** The string before the process. */
    private final String before;

    /** The expected string value after the process. */
    private final String expected;

    /**
     * Test constructor.
     * 
     * @param before
     *            the string before the process
     * @param expected
     *            the expected string value after the process
     */
    public AbstractStringOperationTestCase(final String before, final String expected) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("Initialize test, input value [{}], output value [{}]", before, expected);
        }
        this.before = before;
        this.expected = expected;
    }

    /**
     * Executes the string operation on the test values given at construction time.
     */
    @Test
    public final void testStringOperation() {

        final String result = executeStringOperation(before);
        assertEquals("execute operation with value [" + before + "]", expected, result);
    }

    /**
     * Must be implemented by subclasses and execute the String operation to test.
     * 
     * @param before
     *            this will be the string to process with the tested operation
     * @return the value returned by the string operation
     */
    protected abstract String executeStringOperation(String before);
}
