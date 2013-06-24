package fr.itinerennes.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.test.AndroidTestCase;
import fr.dudie.onebusaway.model.Stop;

public class GtfsDaoTest extends AndroidTestCase {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(GtfsDaoTest.class);

    private DatabaseHelper dbHelper;

    private GtfsDao gtfsDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dbHelper = new DatabaseHelper(getContext());
        gtfsDao = new GtfsDao(getContext(), dbHelper);
    }

    public void testCanFindStop() {
        final Stop s = gtfsDao.getStop("0034");
        assertNotNull(s);
    }

    public void testNullIdNoStopFound() {
        assertNull(gtfsDao.getStop("none"));
    }
}
