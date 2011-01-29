package fr.itinerennes.ui.views;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import fr.itinerennes.business.service.BookmarkService;

public class ToggleStarListener implements OnCheckedChangeListener {

    private final BookmarkService bookmarkService;

    private final String type;

    private final String id;

    private final String label;

    public ToggleStarListener(final BookmarkService bookmarkService, final String type,
            final String id, final String label) {

        this.bookmarkService = bookmarkService;
        this.type = type;
        this.id = id;
        this.label = label;
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {

        if (isChecked) {
            bookmarkService.setStarred(type, id, label);
        } else {
            bookmarkService.setNotStarred(type, id);
        }
    }

}
