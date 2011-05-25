package fr.itinerennes.databse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;

import fr.itinerennes.database.DatabaseHelper;

/**
 * Test class for {@link DatabaseHelper}.
 * 
 * @author Jérémie Huchet
 */
public class DatabaseHelperTest extends AndroidTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelperTest.class);

    /**
     * Test method for {@link DatabaseHelper#getReadableDatabase()}.
     */
    public final void testGetReadableDatabase() {

        LOGGER.info("testOpenDatabase.start");
        final DatabaseHelper dbHlpr = new DatabaseHelper(this.getContext());
        dbHlpr.getReadableDatabase();
        LOGGER.info("testOpenDatabase.end");
    }
}
