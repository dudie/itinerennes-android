package fr.itinerennes.business.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.R;
import fr.itinerennes.business.cache.CacheProvider;
import fr.itinerennes.business.cache.CacheProvider.CacheEntry;
import fr.itinerennes.business.cache.LineIconCacheEntryHandler;
import fr.itinerennes.business.http.keolis.KeolisService;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.model.LineIcon;
import fr.itinerennes.utils.DateUtils;

/**
 * Methods to get an icon representing a transport line.
 * 
 * @author Jérémie Huchet
 */
public class LineIconService extends AbstractService {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(LineIconService.class);

    /** The Keolis service. */
    private final KeolisService keolisService = new KeolisService();

    /** The cache for line icons. */
    private final CacheProvider<LineIcon> iconCache;

    /** The last time all the cache was updated (in seconds). */
    private long lastGlobalUpdate = 0;

    /**
     * Creates a line transport icon service.
     * 
     * @param dbHelper
     *            the database helper
     */
    public LineIconService(final DatabaseHelper dbHelper) {

        super(dbHelper);
        iconCache = new CacheProvider<LineIcon>(dbHelper, new LineIconCacheEntryHandler(dbHelper));;
    }

    /**
     * Gets an icon representing a transport line.
     * 
     * @param line
     *            the name of the line you want the icon
     * @return the drawable
     */
    public final BitmapDrawable getIcon(final String line) throws GenericException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getIcon.start - line={}", line);
        }

        BitmapDrawable image = null;

        // TOBO a virer quand l'api keolis fournira les icones de ces lignes...
        if (line.equalsIgnoreCase("158")) {
            return null;
        }

        LineIcon lineIcon = null;
        CacheEntry<LineIcon> cachedLineIcon = iconCache.load(line);

        // icon isn't available in cache, AND the time between two global cache update is expired
        if (cachedLineIcon == null
                && DateUtils.isExpired(lastGlobalUpdate,
                        ItineRennesConstants.MIN_TIME_BETWEEN_KEOLIS_GET_ALL_CALLS)) {
            final List<LineIcon> allIcons = keolisService.getAllLineIcons();
            iconCache.replace(allIcons);
            lastGlobalUpdate = DateUtils.currentTimeSeconds();
            cachedLineIcon = iconCache.load(line);
        }

        if (cachedLineIcon != null) {
            lineIcon = cachedLineIcon.getValue();
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
            // image = getDefaultIcon(line);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getIcon.end - imageNotNull={}", null != image);
        }
        return image;
    }

    /**
     * Returns a mock icon. This method always returns a displayable drawable.
     * 
     * @param line
     *            the name of the line you want the icon
     * @return the drawable
     */
    public final Drawable getDefaultIcon(final Context context, final String line) {

        final Bitmap icon = Bitmap.createBitmap(46, 46, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(icon);

        final Paint p = new Paint();
        p.setColor(Color.RED);
        canvas.drawLine(0, 0, 46, 46, p);

        final LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);

        final Bitmap iconBackground = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.default_line_icon);

        final TextView text = new TextView(context);
        text.setBackgroundDrawable(new BitmapDrawable(iconBackground));
        text.setText(line);
        text.setGravity(Gravity.CENTER);
        text.setLayoutParams(params);
        text.layout(0, 0, 0, 0);

        final RelativeLayout layout = new RelativeLayout(context);
        layout.addView(text);
        layout.forceLayout();
        layout.draw(canvas);

        final Drawable d = new BitmapDrawable(icon);
        d.setBounds(0, 0, 46, 46);
        return d;
    }
}
