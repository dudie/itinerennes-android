package fr.itinerennes.database;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import fr.itinerennes.database.exception.DatabaseAccessException;

@RunWith(RobolectricTestRunner.class)
public class GtfsDaoTest {

    private GtfsDao gtfsDao;

    @Before
    public void setup() throws IOException {
        final DatabaseHelper dbHelper = new DatabaseHelper(Robolectric.application);
        gtfsDao = new GtfsDao(Robolectric.application, dbHelper);
    }

    @Test
    public void canQueryForStopTimes() throws DatabaseAccessException {
        final Calendar c = Calendar.getInstance();
        c.set(2013, 06, 01);
        gtfsDao.getStopTimes("2_1420", c.getTime());
    }

}
