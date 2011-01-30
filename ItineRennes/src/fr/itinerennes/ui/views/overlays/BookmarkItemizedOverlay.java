package fr.itinerennes.ui.views.overlays;

import java.util.ArrayList;
import java.util.List;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.graphics.drawable.Drawable;

import fr.itinerennes.R;
import fr.itinerennes.business.service.BookmarkService;
import fr.itinerennes.model.Station;
import fr.itinerennes.ui.activity.ITRContext;
import fr.itinerennes.ui.views.overlays.event.OnItemizedOverlayUpdateListener;

/**
 * @author Jérémie Huchet
 */
public final class BookmarkItemizedOverlay<D extends Station> extends ItemizedOverlay<OverlayItem>
        implements OnItemizedOverlayUpdateListener<D> {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory
            .getLogger(BookmarkItemizedOverlay.class);

    /** The itinerennes context. */
    private final ITRContext context;

    /** The bookmark service. */
    private final BookmarkService bookmarkService;

    /** The map view on which this overlay is displayed. */
    private final MapView osmView;

    /**
     * Creates the bookmark overlay.
     * 
     * @param context
     *            the itinerennes context
     */
    public BookmarkItemizedOverlay(final ITRContext context, final MapView osmView) {

        super(context, new ArrayList<OverlayItem>(), getStarOverlayDrawable(context), null, null,
                null, null);
        this.context = context;
        this.bookmarkService = context.getBookmarksService();
        this.osmView = osmView;
    }

    /**
     * Gets the star overlay drawable.
     * 
     * @return the star overlay drawable
     */
    private static Drawable getStarOverlayDrawable(final ITRContext context) {

        return context.getResources().getDrawable(R.drawable.marker_overlay_star);
    }

    /**
     * {@inheritDoc}
     * 
     * @see fr.itinerennes.ui.views.overlays.event.OnItemizedOverlayUpdateListener#onContentUpdated(java.util.List)
     */
    @Override
    public final <I extends Marker<D>> void onContentUpdated(final List<I> items) {

        super.mItemList.clear();
        for (final I item : items) {
            final D bus = item.getData();
            if (bookmarkService.isStarred(bus.getClass().getName(), bus.getId())) {
                final OverlayItem i = new OverlayItem(bus.getClass().getName(), bus.getId(),
                        item.getPoint());
                super.mItemList.add(i);
            }
        }
        osmView.postInvalidate();
    }
}
