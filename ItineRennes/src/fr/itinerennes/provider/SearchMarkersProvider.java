package fr.itinerennes.provider;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.impl.AndroidLoggerFactory;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import fr.itinerennes.database.Columns;
import fr.itinerennes.database.DatabaseHelper;

/**
 * Content provider to handle search markers queries and suggestion markers queries.
 * 
 * @author Olivier Boudet
 */
public class SearchMarkersProvider extends ContentProvider {

    /** The event logger. */
    private static final Logger LOGGER = AndroidLoggerFactory
            .getLogger(SearchMarkersProvider.class);

    /** Authority for this content provider. */
    public static final String AUTHORITY = "fr.itinerennes.provider.searchmarkersprovider";

    /** URI to access this content provider. */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/markers");

    /** URI matcher to dispatch accesses of this content provider. */
    private static final UriMatcher URI_MATCHER;

    /** Used when search of markers is queried. */
    private static final int MARKERS = 1;

    /** Used when markers suggestions are queried. */
    private static final int SEARCH_SUGGEST = 2;

    /** Projection map for suggestions queries. */
    private static final HashMap<String, String> SUGGEST_PROJECTION_MAP = new HashMap<String, String>();

    static {

        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "markers", MARKERS);
        URI_MATCHER.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);

        SUGGEST_PROJECTION_MAP.put(BaseColumns._ID, BaseColumns._ID);
        SUGGEST_PROJECTION_MAP.put(Columns.MarkersColumns.ID, Columns.MarkersColumns.ID);
        SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_1, Columns.MarkersColumns.TYPE
                + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_1);
        SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_TEXT_2,
                Columns.MarkersColumns.LABEL + " AS " + SearchManager.SUGGEST_COLUMN_TEXT_2);
        SUGGEST_PROJECTION_MAP.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID,
                Columns.MarkersColumns.ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);

    }

    /** Database helper. */
    private DatabaseHelper dbHelper;

    /**
     * {@inheritDoc}
     * 
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public final boolean onCreate() {

        // TOBO Récupérer celui de l'application ?
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[],
     *      java.lang.String, java.lang.String[], java.lang.String)
     */
    @Override
    public final Cursor query(final Uri uri, final String[] projection, final String selection,
            final String[] selectionArgs, final String sortOrder) {

        final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(Columns.MarkersColumns.MARKERS_TABLE_NAME);

        String[] columns = null;
        String where = selection;
        String[] params = selectionArgs;

        switch (URI_MATCHER.match(uri)) {
        case MARKERS:

            columns = new String[] { BaseColumns._ID, Columns.MarkersColumns.ID,
                    Columns.MarkersColumns.TYPE, Columns.MarkersColumns.LABEL,
                    Columns.MarkersColumns.LONGITUDE, Columns.MarkersColumns.LATITUDE };
            break;
        case SEARCH_SUGGEST:
            where = String.format("%s LIKE ?", Columns.MarkersColumns.LABEL);
            params = new String[] { "%" + selectionArgs[0] + "%" };

            qb.setProjectionMap(SUGGEST_PROJECTION_MAP);
            columns = new String[] { BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1,
                    SearchManager.SUGGEST_COLUMN_TEXT_2,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID };
            break;

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        final Cursor c = qb.query(dbHelper.getReadableDatabase(), columns, where, params, null,
                null, sortOrder);

        return c;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public final String getType(final Uri uri) {

        switch (URI_MATCHER.match(uri)) {
        case MARKERS:
            return Columns.MarkersColumns.CONTENT_TYPE;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public final Uri insert(final Uri uri, final ContentValues values) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final int delete(final Uri uri, final String selection, final String[] selectionArgs) {

        throw new UnsupportedOperationException();
    }

    @Override
    public final int update(final Uri uri, final ContentValues values, final String selection,
            final String[] selectionArgs) {

        throw new UnsupportedOperationException();
    }

}
