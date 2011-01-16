package fr.itinerennes.business.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;

import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.LineIcon;

/**
 * Test class for {@link LineIconService}.
 * 
 * @author Jérémie Huchet
 */
public class LineIconServiceTest extends AndroidTestCase {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(LineIconServiceTest.class);

    /** The writable database. */
    private SQLiteDatabase database;

    /** The line icon service. */
    private LineIconService lineIconService;

    /** All line names known by Keolis Service. */
    private ArrayList<String> lines;

    /** Some misspelled lines names. */
    private final String[] misspelledLines = new String[] { "abcd", "-13", "" };

    /**
     * {@inheritDoc}
     * 
     * @see android.test.AndroidTestCase#setUp()
     */
    @Override
    protected final void setUp() throws Exception {

        LOGGER.info("loading database and service...");
        final DatabaseHelper dbHlpr = new DatabaseHelper(this.getContext());
        lineIconService = new LineIconService(dbHlpr);

        LOGGER.info("loading test data...");
        // gets all lines name and store them in property "lines"
        final KeolisService keolisService = new KeolisService();
        final List<LineIcon> allLineIcons = keolisService.getAllLineIcons();
        lines = new ArrayList<String>(allLineIcons.size());
        for (final LineIcon icon : allLineIcons) {
            lines.add(icon.getLine());
        }
        if (lines.size() <= 0) {
            final String message = "there should be more than 0 lines names found";
            LOGGER.error(message);
            throw new Exception(message);
        }
        LOGGER.info("ready!");
    }

    /**
     * Test method for {@link LineIconServiceTest#getIcon} using existing line identifier.
     */
    public void testGetExistingLineIcon() {

        LOGGER.info("testGetExistingLineIcon.start");

        for (final String line : lines) {
            LOGGER.debug("testing retrieval for {}", line);
            Drawable image = null;
            try {
                image = lineIconService.getIcon(line);
            } catch (final GenericException e) {
                LOGGER.error(e.getMessage(), e);
                fail(e.getMessage());
            }
            assertNotNull("icon image shouldn't be null", image);
        }
        LOGGER.info("testGetExistingLineIcon.end");
    }

    /**
     * Test method for {@link LineIconServiceTest#getIcon} using misspelled line identifier.
     */
    public void testGetUnexistingLineIcon() {

        LOGGER.info("testGetUnexistingLineIcon.start");

        for (final String line : misspelledLines) {
            LOGGER.debug("testing retrieval for {}", line);
            Drawable image = null;
            try {
                image = lineIconService.getIcon(line);
            } catch (final GenericException e) {
                LOGGER.error(e.getMessage(), e);
                fail(e.getMessage());
            }
            // TJHU le résultat devrait changer lorsque le service retournera une icone générique
            assertNull("icon image should be null", image);
        }
        LOGGER.info("testGetUnexistingLineIcon.end");
    }
}
