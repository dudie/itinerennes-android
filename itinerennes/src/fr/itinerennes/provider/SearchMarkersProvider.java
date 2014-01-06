package fr.itinerennes.provider;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import fr.itinerennes.ItineRennesApplication;
import fr.itinerennes.database.Columns;
import fr.itinerennes.database.MarkerDao;

/**
 * Content provider to handle search markers queries and suggestion markers queries.
 * 
 * @author Olivier Boudet
 */
public class SearchMarkersProvider extends ContentProvider {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchMarkersProvider.class);

    /** Authority for this content provider. */
    public static final String AUTHORITY = "fr.itinerennes.provider.searchMarkersProvider";

    /** URI to access this content provider. */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /** URI matcher to dispatch accesses of this content provider. */
    private static final UriMatcher URI_MATCHER;

    /** Used when search of markers is queried. */
    private static final int GET_MARKERS = 1;

    /** Used when search of markers is queried. */
    private static final int GET_MARKER = 2;

    /** Used when markers suggestions are queried. */
    private static final int GET_SUGGEST = 3;

    static {

        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, "/", GET_MARKERS);
        URI_MATCHER.addURI(AUTHORITY, "/#", GET_MARKER);
        URI_MATCHER.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, GET_SUGGEST);

    }

    /** The Marker service to retrieve markers from the database. */
    private MarkerDao markerService;

    /**
     * {@inheritDoc}
     * 
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public final boolean onCreate() {

        final boolean initialized;
        if (getContext() instanceof ItineRennesApplication) {

            markerService = ((ItineRennesApplication) getContext()).getMarkerDao();
            initialized = true;
        } else {
            LOGGER.error("Bad application context type, expected {} but was {}",
                    ItineRennesApplication.class, getContext().getClass());
            initialized = false;
        }
        return initialized;
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

        switch (URI_MATCHER.match(uri)) {
        case GET_MARKER:
            return markerService.getMarker(uri.getLastPathSegment());
        case GET_MARKERS:

            if (selectionArgs == null) {
                throw new IllegalArgumentException("selectionArgs must be provided for the Uri: "
                        + uri);
            }

            return markerService.searchMarkers(selectionArgs[0]);
        case GET_SUGGEST:

            if (selectionArgs == null || selectionArgs.length < 1) {
                throw new IllegalArgumentException("selectionArgs must be provided for the Uri: "
                        + uri);
            }

            if (selectionArgs[0] == null || selectionArgs[0].length() < 3) {
                return null;
            }

            return markerService.getSuggestions(selectionArgs[0]);

        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public final String getType(final Uri uri) {

        switch (URI_MATCHER.match(uri)) {
        case GET_MARKERS:
            return Columns.MarkersColumns.CONTENT_TYPE;
        case GET_SUGGEST:
            return SearchManager.SUGGEST_MIME_TYPE;

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
