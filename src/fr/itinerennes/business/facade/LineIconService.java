package fr.itinerennes.business.facade;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.LineIconCacheEntryHandler;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.model.LineIcon;

/**
 * Methods to get an icon representing a transport line.
 * 
 * @author Jérémie Huchet
 */
public class LineIconService {

    /** The Keolis service. */
    private final KeolisService keolisService;;

    /** The cache for line icons. */
    private final CacheProvider<LineIcon> iconCache;

    /**
     * Creates a line transport icon service.
     * 
     * @param database
     *            the database
     */
    public LineIconService(final SQLiteDatabase database) {

        keolisService = new KeolisService();
        iconCache = new CacheProvider<LineIcon>(database, new LineIconCacheEntryHandler(database),
                ItineRennesConstants.TTL_LINE_TRANSPORT_ICONS);
    }

    public LineIconService() {

        keolisService = new KeolisService();
        iconCache = null;
    }

    /**
     * Gets an icon representing a transport line.
     * 
     * @param line
     *            the name of the line you want the icon
     * @return the drawable
     */
    public final Drawable getIcon(final LineIcon icon /* String line */) {

        final byte[] data = keolisService.fetchIcon(icon);
        return new BitmapDrawable(BitmapFactory.decodeByteArray(data, 0, data.length));
    }
}
