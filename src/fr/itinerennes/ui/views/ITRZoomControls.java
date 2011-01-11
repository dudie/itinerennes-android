package fr.itinerennes.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

public class ITRZoomControls extends ZoomControls {

    public ITRZoomControls(final Context context) {

        super(context);
    }

    public ITRZoomControls(final Context context, final AttributeSet attr) {

        super(context, attr);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        // Ugly code : force buttons be equal size.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(0).getLayoutParams();
        layoutParams.width = getMeasuredWidth() / 2;
        layoutParams = (LinearLayout.LayoutParams) getChildAt(1).getLayoutParams();
        layoutParams.width = getMeasuredWidth() / 2;
        super.onMeasure(getMeasuredWidth(), getMeasuredHeight());
    }
}
