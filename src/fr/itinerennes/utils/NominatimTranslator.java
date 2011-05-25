package fr.itinerennes.utils;

import java.util.List;

import android.database.Cursor;
import android.database.MatrixCursor;

import fr.itinerennes.database.Columns.NominatimColumns;
import fr.itinerennes.nominatim.model.Address;

/**
 * Translates nominatim {@link Address}es to {@link Cursor}.
 * 
 * @author Jérémie Huchet
 */
public final class NominatimTranslator implements NominatimColumns {

    /**
     * Private constructor to avoid instantiation.
     */
    private NominatimTranslator() {

    }

    /**
     * Translates a list of addresses to a cursor.
     * 
     * @param addresses
     *            a list of {@link Address} objects
     * @return a cursor
     */
    public static Cursor toCursor(final List<Address> addresses) {

        final String[] cols = new String[] { DISPLAY_NAME, LONGITUDE, LATITUDE };
        final MatrixCursor c = new MatrixCursor(cols);

        for (final Address address : addresses) {
            c.addRow(toColumnValues(address));
        }

        return c;
    }

    /**
     * Translates an address to an array of cursor row values.
     * 
     * @param address
     *            the address to translate
     * @return an array of values
     */
    private static Object[] toColumnValues(final Address address) {

        final Object[] values = new Object[3];
        values[0] = address.getDisplayName();
        values[1] = address.getLongitudeE6();
        values[2] = address.getLatitudeE6();
        return values;
    }
}
