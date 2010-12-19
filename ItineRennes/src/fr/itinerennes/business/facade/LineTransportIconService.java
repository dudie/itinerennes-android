package fr.itinerennes.business.facade;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.LineTransportIconCacheEntryHandler;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.model.LineTransportIcon;

/**
 * Methods to get an icon representing a transport line.
 * 
 * @author Jérémie Huchet
 */
public class LineTransportIconService {

    /** The Keolis service. */
    private final KeolisService keolisService;;

    /** The cache for line icons. */
    private final CacheProvider<LineTransportIcon> iconCache;

    /**
     * Creates a line transport icon service.
     * 
     * @param database
     *            the database
     */
    public LineTransportIconService(final SQLiteDatabase database) {

        keolisService = new KeolisService();
        iconCache = new CacheProvider<LineTransportIcon>(database,
                new LineTransportIconCacheEntryHandler(),
                ItineRennesConstants.TTL_LINE_TRANSPORT_ICONS);
    }

    public LineTransportIconService() {

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
    public Drawable getIcon(final LineTransportIcon icon /* String line */) {

        return keolisService.fetchIcon(icon);
    }
}
