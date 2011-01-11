package fr.itinerennes.ui.views;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

import fr.itinerennes.R;

/**
 * Simple widget displaying an icon left of the text.
 * 
 * @author Jérémie Huchet
 */
public final class IconTextView extends TextView {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(IconTextView.class);

    /** The icon to display left of the text. */
    private final Drawable icon;

    public IconTextView(final Context context, final AttributeSet attrs, final int defStyle) {

        super(context, attrs, defStyle);
        icon = context.getResources().getDrawable(R.drawable.bus_activity_title_icon);
    }

    public IconTextView(final Context context, final AttributeSet attrs) {

        this(context, attrs, 0);
    }

    public IconTextView(final Context context) {

        this(context, null, 0);
    }

    /**
     * Draws the icon and move the text to the right before drawing it.
     * 
     * @see android.widget.TextView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(final Canvas canvas) {

        final int width = icon.getIntrinsicWidth();
        final int height = icon.getIntrinsicHeight();

        int y = 0;
        y = (getHeight() - height) / 2;

        icon.setBounds(0, y, width, y + height);
        icon.draw(canvas);

        super.onDraw(canvas);
    }
}
