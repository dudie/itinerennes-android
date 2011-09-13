package fr.itinerennes.ui.views;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import fr.itinerennes.ui.activity.ItineRennesActivity;

/**
 * A specific image view to display line icons.
 * 
 * @author Jérémie Huchet
 */
public final class LineImageView extends ImageView {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LineImageView.class);

    /** The context. */
    private final ItineRennesActivity context;

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     */
    public LineImageView(final ItineRennesActivity context) {

        super(context);
        this.context = context;
    }

    public void setBounds(final int dipWidth, final int dipHeight) {

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final int pxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipWidth,
                metrics);
        final int pxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipHeight, metrics);

        final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        setLayoutParams(params);
        setAdjustViewBounds(true);
        setMaxWidth(pxWidth);
        setMaxHeight(pxHeight);
    }

    /**
     * Sets the line to display.
     * 
     * @param lineId
     *            the line short name
     */
    public void setLine(final String lineId) {

        final Drawable icon = context.getApplicationContext().getLineIconService()
                .getIconOrDefault(context, lineId);
        setImageDrawable(icon);
    }
}
