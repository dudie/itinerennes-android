package fr.itinerennes.business.cache;

import java.util.List;

import org.osmdroid.util.BoundingBoxE6;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.itinerennes.database.Columns.LineIconColumns;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.model.LineIcon;

/**
 * Handles save / update / load / delete for {@link LineIcon}.
 * 
 * @author Jérémie Huchet
 */
public class LineIconCacheEntryHandler extends AbstractDatabaseCacheEntryHandler implements
        CacheEntryHandler<LineIcon>, LineIconColumns {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(LineIconCacheEntryHandler.class);

    /** The database table name : {@value LineIconCacheEntryHandler#LINE_ICONS_TABLE_NAME} . */
    private static final String LINE_ICONS_TABLE_NAME = "line_icons";

    /**
     * The SQL where clause to select on {@link LineIconColumns#LINE_ID} : {@value #WHERE_CLAUSE_ID}
     * .
     */
    private static final String WHERE_CLAUSE_ID = String.format("%s = ? ", LINE_ID);

    /**
     * Creates a line icon cache entry handler.
     * 
     * @param dbHelper
     *            the database helper
     */
    public LineIconCacheEntryHandler(final DatabaseHelper dbHelper) {

        super(dbHelper);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#replace(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    @Override
    public final void replace(final String type, final String id, final LineIcon icon) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("replace.start - type={}, identifier={}", type, id);
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("replace {}", icon.toString());
            }
        }
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final ContentValues values = new ContentValues(4);
        values.put(LINE_ID, id);
        values.put(URL, icon.getIconUrl());
        values.put(ICON, icon.getIconBytes());

        final long rowId = database.replace(LINE_ICONS_TABLE_NAME, null, values);
        if (-1 == rowId) {
            LOGGER.error("station was not successfully replaced : {}", icon.toString());
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("replace.end");
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#delete(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public final void delete(final String type, final String lineId) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("delete.start - type={}, line_identifier={}", type, lineId);
        }
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final int delCount = database.delete(LINE_ICONS_TABLE_NAME, WHERE_CLAUSE_ID,
                new String[] { lineId });
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("delete.end - {} rows deleted", delCount);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#load(java.lang.String, java.lang.String)
     */
    @Override
    public final LineIcon load(final String type, final String lineId) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start - type={}, identifier={}", type, lineId);
        }
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        final String[] columns = new String[] { LINE_ID, URL, ICON };
        final String[] selectionArgs = new String[] { lineId };
        final Cursor c = database.query(LINE_ICONS_TABLE_NAME, columns, WHERE_CLAUSE_ID,
                selectionArgs, null, null, null);

        final LineIcon icon;
        if (c.moveToFirst()) {
            icon = new LineIcon();
            icon.setLine(c.getString(0));
            icon.setIconUrl(c.getString(1));
            icon.setIconBytes(c.getBlob(2));
        } else {
            icon = null;
        }
        c.close();

        if (LOGGER.isDebugEnabled()) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.trace("loaded {}", icon.toString());
            }
            LOGGER.debug("load.end - resultNotNull={}", null != icon);
        }
        return icon;
    }

    /**
     * {@inheritDoc}
     * 
     * @deprecated not applicable
     * @see fr.itinerennes.business.cache.CacheEntryHandler#load(java.lang.String,
     *      org.andnav.osm.util.BoundingBoxE6)
     */
    @Deprecated
    @Override
    public final List<LineIcon> load(final String type, final BoundingBoxE6 bbox) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.start");
        }
        // not applicable

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load.end");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.business.cache.CacheEntryHandler#getHandledClass()
     */
    @Override
    public final Class<LineIcon> getHandledClass() {

        return LineIcon.class;
    }
}
