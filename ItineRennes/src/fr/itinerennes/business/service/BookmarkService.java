package fr.itinerennes.business.service;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.BookmarksColumns;
import fr.itinerennes.database.DatabaseHelper;

/**
 * A service to manage user bookmarks.
 * 
 * @author Jérémie Huchet
 */
public class BookmarkService implements BookmarksColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(BookmarkService.class);

    /** The database helper. */
    private final DatabaseHelper dbHelper;

    /**
     * Creates the bookmark service.
     * 
     * @param dbHelper
     *            a database helper
     */
    public BookmarkService(final DatabaseHelper dbHelper) {

        this.dbHelper = dbHelper;
    }

    /**
     * Sets a resource starred.
     * 
     * @param type
     *            the type of the resource
     * @param id
     *            the identifier of the resource
     */
    public final void setStarred(final String type, final String id, final String label) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("setStarred.start - type={}, id={}", type, id);
        }

        final ContentValues values = new ContentValues();
        values.put(LABEL, label);
        values.put(TYPE, type);
        values.put(ID, id);

        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        // TJHU est ce nécessaire de vérifier qu'on ne va pas faire péter la contrainte
        // unique(type,id) ?
        database.insert(BOOKMARKS_TABLE_NAME, null, values);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("setStarred.end");
        }
    }

    /**
     * Unstar a resource.
     * 
     * @param type
     *            the type of the resource
     * @param id
     *            the identifier of the resource
     */
    public final void setNotStarred(final String type, final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("setNotStarred.start - type={}, id={}", type, id);
        }

        final String where = String.format("%s = ? AND %s = ?", TYPE, ID);
        final String[] whereArgs = new String[] { type, id };

        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(BOOKMARKS_TABLE_NAME, where, whereArgs);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("setNotStarred.end");
        }
    }

    /**
     * Returns whether or not a resource is starred.
     * 
     * @param type
     *            the type of the resource
     * @param id
     *            the identifier of the resource
     * @return true if the resource is starred or false
     */
    public final boolean isStarred(final String type, final String id) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isStarred.start - type={}, id={}", type, id);
        }

        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final String selection = String.format("%s = ? AND %s = ?", TYPE, ID);
        final String[] selectionArgs = new String[] { type, id };
        final Cursor c = database.query(BOOKMARKS_TABLE_NAME, new String[] { _ID }, selection,
                selectionArgs, null, null, null);

        final boolean hasResult = c.moveToNext();
        c.close();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("isStarred.end - isStarred={}", hasResult);
        }
        return hasResult;
    }
}
