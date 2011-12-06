package fr.itinerennes.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import fr.itinerennes.R;
import fr.itinerennes.ui.activity.ItineRennesActivity;

/**
 * Zoom Controls.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */
public class ItinerennesZoomControls extends ZoomControls {

    /** Ressource id of the {@link ItinerennesMapView}. */
    private final int mapResId;

    /** The {@link ItinerennesMapView} containing this Zoom Controls. */
    private ItinerennesMapView map;

    /**
     * Constructor.
     * 
     * @param context
     *            the context
     * @param attr
     *            the set of attributes
     */
    public ItinerennesZoomControls(final Context context, final AttributeSet attr) {

        super(context, attr);

        final TypedArray typedArray = getContext().obtainStyledAttributes(attr,
                R.styleable.ITRZoomControls);
        mapResId = typedArray.getResourceId(R.styleable.ITRZoomControls_mapview, -1);

        tryInitZoomControls();
        setOnZoomInClickListener(new OnZoomInClickListener());
        setOnZoomOutClickListener(new OnZoomOutClickListener());
    }

    /**
     * Zoom in listener.
     * 
     * @author Olivier Boudet
     * @author Jérémie Huchet
     */
    private class OnZoomInClickListener implements OnClickListener {

        @Override
        public void onClick(final View v) {

            if (map != null) {
                map.getController().setZoom(map.getZoomLevel() + 1);
            } else if (mapResId != -1) {
                tryInitZoomControls();
                map.getController().setZoom(map.getZoomLevel() + 1);
            }
        }
    }

    /**
     * Zoom out listener.
     * 
     * @author Olivier Boudet
     * @author Jérémie Huchet
     */
    private class OnZoomOutClickListener implements OnClickListener {

        @Override
        public void onClick(final View v) {

            if (map != null) {
                map.getController().setZoom(map.getZoomLevel() - 1);
            } else if (mapResId != -1) {
                tryInitZoomControls();
                map.getController().setZoom(map.getZoomLevel() - 1);
            }
        }
    }

    /**
     * Initializes zoom controls.
     */
    public final void tryInitZoomControls() {

        map = (ItinerennesMapView) ((ItineRennesActivity) getContext()).findViewById(mapResId);
        if (null != map) {
            map.setBuiltInZoomControls(false);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.LinearLayout#onMeasure(int, int)
     */
    @Override
    protected final void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        // Ugly code : force buttons be equal size.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LayoutParams layoutParams = (LinearLayout.LayoutParams) getChildAt(0).getLayoutParams();
        layoutParams.width = getMeasuredWidth() / 2;
        layoutParams = (LinearLayout.LayoutParams) getChildAt(1).getLayoutParams();
        layoutParams.width = getMeasuredWidth() / 2;
        super.onMeasure(getMeasuredWidth(), getMeasuredHeight());
    }

}
