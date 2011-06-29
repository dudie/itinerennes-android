package fr.itinerennes.database;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;

import fr.itinerennes.commons.utils.StringUtils;

/**
 * A cursor wrapper to highlight the text.
 * 
 * @author Jérémie Huchet
 */
public final class HighlightCursor implements Cursor {

    /** The prefix used to highlight: {@value #PREFIX}. */
    private static final String PREFIX = "<b>";

    /** The suffix used to highlight: {@value #SUFFIX}. */
    private static final String SUFFIX = "</b>";

    /** The wrapped cursor. */
    private final Cursor cursor;

    /** The string to highlight. */
    private final String query;

    /**
     * Creates a cursor which will highlight the given query in all strings it will return.
     * 
     * @param cursor
     *            a cursor
     * @param query
     *            the query to highlight
     */
    public HighlightCursor(final Cursor cursor, final String query) {

        this.cursor = cursor;
        this.query = query;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Highlight the value.</b>
     * 
     * @see android.database.Cursor#getString(int)
     */
    @Override
    public String getString(final int columnIndex) {

        return Html.fromHtml(StringUtils.highlight(cursor.getString(columnIndex), query, PREFIX,
                SUFFIX));
    }

    /**
     * {@inheritDoc}
     * <p>
     * <b>Highlight the value.</b>
     * 
     * @see android.database.Cursor#copyStringToBuffer(int, android.database.CharArrayBuffer)
     */
    @Override
    public void copyStringToBuffer(final int columnIndex, final CharArrayBuffer buffer) {

        final String result = this.getString(columnIndex);
        if (result != null) {
            final char[] data = buffer.data;
            if (data == null || data.length < result.length()) {
                buffer.data = result.toCharArray();
            } else {
                result.getChars(0, result.length(), data, 0);
            }
            buffer.sizeCopied = result.length();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getCount()
     */
    @Override
    public int getCount() {

        return cursor.getCount();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getPosition()
     */
    @Override
    public int getPosition() {

        return cursor.getPosition();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#move(int)
     */
    @Override
    public boolean move(final int offset) {

        return cursor.move(offset);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#moveToPosition(int)
     */
    @Override
    public boolean moveToPosition(final int position) {

        return cursor.moveToPosition(position);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#moveToFirst()
     */
    @Override
    public boolean moveToFirst() {

        return cursor.moveToFirst();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#moveToLast()
     */
    @Override
    public boolean moveToLast() {

        return cursor.moveToLast();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#moveToNext()
     */
    @Override
    public boolean moveToNext() {

        return cursor.moveToNext();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#moveToPrevious()
     */
    @Override
    public boolean moveToPrevious() {

        return cursor.moveToPrevious();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#isFirst()
     */
    @Override
    public boolean isFirst() {

        return cursor.isFirst();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#isLast()
     */
    @Override
    public boolean isLast() {

        return cursor.isLast();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#isBeforeFirst()
     */
    @Override
    public boolean isBeforeFirst() {

        return cursor.isBeforeFirst();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#isAfterLast()
     */
    @Override
    public boolean isAfterLast() {

        return cursor.isAfterLast();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getColumnIndex(java.lang.String)
     */
    @Override
    public int getColumnIndex(final String columnName) {

        return cursor.getColumnIndex(columnName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getColumnIndexOrThrow(java.lang.String)
     */
    @Override
    public int getColumnIndexOrThrow(final String columnName) throws IllegalArgumentException {

        return cursor.getColumnIndexOrThrow(columnName);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getColumnName(int)
     */
    @Override
    public String getColumnName(final int columnIndex) {

        return cursor.getColumnName(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getColumnNames()
     */
    @Override
    public String[] getColumnNames() {

        return cursor.getColumnNames();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getColumnCount()
     */
    @Override
    public int getColumnCount() {

        return cursor.getColumnCount();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getBlob(int)
     */
    @Override
    public byte[] getBlob(final int columnIndex) {

        return cursor.getBlob(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getShort(int)
     */
    @Override
    public short getShort(final int columnIndex) {

        return cursor.getShort(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getInt(int)
     */
    @Override
    public int getInt(final int columnIndex) {

        return cursor.getInt(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getLong(int)
     */
    @Override
    public long getLong(final int columnIndex) {

        return cursor.getLong(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getFloat(int)
     */
    @Override
    public float getFloat(final int columnIndex) {

        return cursor.getFloat(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getDouble(int)
     */
    @Override
    public double getDouble(final int columnIndex) {

        return cursor.getDouble(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#isNull(int)
     */
    @Override
    public boolean isNull(final int columnIndex) {

        return cursor.isNull(columnIndex);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#deactivate()
     */
    @Override
    public void deactivate() {

        cursor.deactivate();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#requery()
     */
    @Override
    public boolean requery() {

        return cursor.requery();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#close()
     */
    @Override
    public void close() {

        cursor.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#isClosed()
     */
    @Override
    public boolean isClosed() {

        return cursor.isClosed();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#registerContentObserver(android.database.ContentObserver)
     */
    @Override
    public void registerContentObserver(final ContentObserver observer) {

        cursor.registerContentObserver(observer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#unregisterContentObserver(android.database.ContentObserver)
     */
    @Override
    public void unregisterContentObserver(final ContentObserver observer) {

        cursor.unregisterContentObserver(observer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#registerDataSetObserver(android.database.DataSetObserver)
     */
    @Override
    public void registerDataSetObserver(final DataSetObserver observer) {

        cursor.registerDataSetObserver(observer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#unregisterDataSetObserver(android.database.DataSetObserver)
     */
    @Override
    public void unregisterDataSetObserver(final DataSetObserver observer) {

        cursor.unregisterDataSetObserver(observer);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#setNotificationUri(android.content.ContentResolver,
     *      android.net.Uri)
     */
    @Override
    public void setNotificationUri(final ContentResolver cr, final Uri uri) {

        cursor.setNotificationUri(cr, uri);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getWantsAllOnMoveCalls()
     */
    @Override
    public boolean getWantsAllOnMoveCalls() {

        return cursor.getWantsAllOnMoveCalls();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#getExtras()
     */
    @Override
    public Bundle getExtras() {

        return cursor.getExtras();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.Cursor#respond(android.os.Bundle)
     */
    @Override
    public Bundle respond(final Bundle extras) {

        return cursor.respond(extras);
    }

}
