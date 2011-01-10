package fr.itinerennes.business.facade;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.LineIconCacheEntryHandler;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.LineIcon;

/**
 * Methods to get an icon representing a transport line.
 * 
 * @author Jérémie Huchet
 */
public class LineIconService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(LineIconService.class);

    /** The Keolis service. */
    private final KeolisService keolisService;;

    /** The service delayer used to retrieve icons. */
    private final AbstractDelayedService<LineIcon> delayedService;

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
        delayedService = new AbstractDelayedService<LineIcon>(iconCache,
                ItineRennesConstants.MIN_TIME_BETWEEN_KEOLIS_GET_ALL_CALLS) {

            @Override
            protected List<LineIcon> getAll() throws GenericException {

                return keolisService.getAllLineIcons();
            }
        };
    }

    /**
     * Gets an icon representing a transport line.
     * 
     * @param line
     *            the name of the line you want the icon
     * @return the drawable
     */
    public final Drawable getIcon(final String line) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getIcon.start - line={}", line);
        }

        BitmapDrawable image = null;

        // TOBO a virer quand l'api keolis fournira les icones de ces lignes...
        if (line.equalsIgnoreCase("158")) {
            return null;
        }

        LineIcon lineIcon = iconCache.load(line);

        if (lineIcon == null) {
            lineIcon = delayedService.getResource(line);
        }

        if (null != lineIcon && null == lineIcon.getIconBytes()) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("fetching icon for line {}", line);
            }
            final byte[] data = keolisService.fetchIcon(lineIcon);
            lineIcon.setIconBytes(data);
            iconCache.replace(lineIcon);
        }

        if (null != lineIcon && null != lineIcon.getIconBytes()) {
            image = new BitmapDrawable(BitmapFactory.decodeByteArray(lineIcon.getIconBytes(), 0,
                    lineIcon.getIconBytes().length));
        }

        if (null == image) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("icon not found for line {}, using default", line);
            }
            // TJHU mettre une image par défaut (besoin du contexte applicatif à priori)
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getIcon.end - imageNotNull={}", null != image);
        }
        return image;
    }
}
