package fr.itinerennes.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import fr.itinerennes.R;

/**
 * @author Olivier Boudet
 */
public class ToolbarButton extends ImageView {

    private final LayerDrawable ld;

    private final Drawable drawable;

    public ToolbarButton(final Context context, final AttributeSet attrs) {

        super(context, attrs);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ToolbarButton,
                0, 0);

        drawable = array.getDrawable(R.styleable.ToolbarButton_drawable);

        ld = new LayerDrawable(new Drawable[] {
                context.getResources().getDrawable(R.drawable.bg_toolbar_button), drawable });

        setBackgroundDrawable(ld);

        array.recycle();
    }

    /*
     * (non-javadoc)
     * @see android.view.View#onLayout(boolean, int, int, int, int)
     */
    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right,
            final int bottom) {

        final int height = getHeight();
        final int width = getWidth();

        ld.setLayerInset(1, (width - drawable.getMinimumWidth()) / 2,
                (height - drawable.getMinimumHeight()) / 2,
                (width - drawable.getMinimumWidth()) / 2,
                (height - drawable.getMinimumHeight()) / 2);
        super.onLayout(changed, left, top, right, bottom);
    }

}
