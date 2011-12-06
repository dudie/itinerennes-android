package fr.itinerennes.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;

/**
 * Helper class to load resources by name.
 * 
 * @author Jérémie Huchet
 */
public final class ResourceResolver {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceResolver.class);

    /**
     * Avoid instantiation.
     */
    private ResourceResolver() {

    }

    /**
     * Gets the identifier of the given drawable name. Returns the given default drawable identifier
     * if no resource matched the given name.
     * 
     * @param context
     *            the context
     * @param resName
     *            the drawable name you want the identifier
     * @param defaultRes
     *            the default resource identifier to return
     * @return either the found drawable id or the given default
     */
    public static int getDrawableId(final Context context, final String resName,
            final int defaultRes) {

        int resId = 0;
        if (resName != null) {
            final String resNameLow = resName.toLowerCase();
            resId = context.getResources().getIdentifier(resNameLow, "drawable",
                    context.getPackageName());
        }

        if (resId == 0) {
            resId = defaultRes;
        }

        return resId;
    }

    /**
     * Resolves the given drawable resource id and applies a filter to make it more consistent with
     * Android UI guidelines for dialog icons.
     * 
     * @param context
     *            the context
     * @param drawableResourceId
     *            a drawable resource identifier
     * @return a drawable representing the given resource but a little more lightened
     */
    public static Drawable fromMenuToDialogIcon(final Context context, final int drawableResourceId) {

        final Drawable drawable = context.getResources().getDrawable(drawableResourceId);
        drawable.setColorFilter(new LightingColorFilter(Color.WHITE, Color.DKGRAY));
        return drawable;
    }
}
