package fr.itinerennes.business.service;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.AccessibilityColumns;
import fr.itinerennes.database.DatabaseHelper;

/**
 * A service to fetch accessibility attributes from database.
 * 
 * @author Olivier Boudet
 */
public class AccessibilityService extends AbstractService implements AccessibilityColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(AccessibilityService.class);

    /**
     * Creates the accessibility service.
     * 
     * @param dbHelper
     *            a database helper
     */
    public AccessibilityService(final DatabaseHelper dbHelper) {

        super(dbHelper);
    }

    /**
     * Returns whether or not a resource is accessible for wheelchairs.
     * 
     * @param id
     *            the identifier of the resource
     * @param type
     *            type of the resource
     * @return true if the resource is accessible or false
     */
    public final boolean isAccessible(final String id, final String type) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isAccessible.start - id={}, type={}", id, type);
        }
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final String selection = String
                .format("%s = ? AND %s = ? AND %s = ?", ID, TYPE, WHEELCHAIR);
        final String[] selectionArgs = new String[] { id, type, "1" };
        final Cursor c = database.query(ACCESSIBILITY_TABLE_NAME, new String[] { _ID }, selection,
                selectionArgs, null, null, null);

        final boolean hasResult = c.moveToNext();
        c.close();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isAccessible.end - isAccessible={}", hasResult);
        }
        return hasResult;
    }

}
