package fr.itinerennes.ui.views.event;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import fr.itinerennes.Conf;
import fr.itinerennes.R;
import fr.itinerennes.business.service.BookmarkService;
import fr.itinerennes.ui.activity.ItineRennesActivity;

/**
 * A listener for toggle button which changes the bookmark state of a resource.
 * 
 * @author Jérémie Huchet
 */
public class ToggleStarListener implements OnCheckedChangeListener {

    /** The itinerennes context. */
    private final ItineRennesActivity context;

    /** The bookmarks service. */
    private final BookmarkService bookmarks;

    /** The type of the resources (ex: fr.itinerennes.model.BusStation). */
    private final String type;

    /** The identifier of the resource. */
    private final String id;

    /** The label to use to bookmark the resource. */
    private final String label;

    /** The toast notification displayed when the user clicks on the button. */
    private final Toast notification;

    /**
     * Creates the star button change listener.
     * 
     * @param context
     *            the itinerennes context
     * @param type
     *            the type of the bookmark
     * @param id
     *            the id of the bookmark
     * @param label
     *            the label of the bookmark
     */
    public ToggleStarListener(final ItineRennesActivity context, final String type,
            final String id, final String label) {

        this.context = context;
        this.bookmarks = context.getApplicationContext().getBookmarksService();
        this.type = type;
        this.id = id;
        this.label = label;
        this.notification = Toast.makeText(context, null, Conf.TOAST_DURATION);
    }

    /**
     * Adds or remove the resource from the bookmarks depending on the new state of the button.
     * Displays a toast notification to confirm the modification.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.widget.CompoundButton.OnCheckedChangeListener#onCheckedChanged(android.widget.CompoundButton,
     *      boolean)
     */
    @Override
    public final void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

        notification.cancel();
        if (isChecked) {
            bookmarks.setStarred(type, id, label);
            notification.setText(context.getString(R.string.added_to_bookmarks, label));
            notification.show();
        } else {
            bookmarks.setNotStarred(type, id);
            notification.setText(context.getString(R.string.removed_from_bookmarks, label));
            notification.show();
        }
    }

}
