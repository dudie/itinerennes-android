package fr.itinerennes.ui.adapter;

/*
 * [license]
 * ItineRennes
 * ----
 * Copyright (C) 2013 - 2014 Dudie
 * ----
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * [/license]
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fr.itinerennes.R;
import fr.itinerennes.TypeConstants;
import fr.itinerennes.model.Bookmark;

/**
 * @author Jérémie Huchet
 */
public class BookmarksAdapter extends BaseAdapter {

    /** A map containing icons to use for each type of bookmarks. */
    private static final Map<String, Integer> ICONS = new HashMap<String, Integer>();
    static {
        ICONS.put(TypeConstants.TYPE_BIKE, R.drawable.ic_activity_title_bike);
        ICONS.put(TypeConstants.TYPE_BUS, R.drawable.ic_activity_title_bus);
        ICONS.put(TypeConstants.TYPE_SUBWAY, R.drawable.ic_activity_title_subway);
    }

    /** The layout inflater. */
    private final LayoutInflater inflater;

    /** The bookmarks. */
    private final List<Bookmark> bookmarks;

    /**
     * Creates the bookmarks adapter.
     * 
     * @param context
     *            the application context
     * @param bookmarks
     *            the list of bookmarks to display
     */
    public BookmarksAdapter(final Context context, final List<Bookmark> bookmarks) {

        inflater = LayoutInflater.from(context);
        this.bookmarks = bookmarks;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public final int getCount() {

        return bookmarks.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public final Bookmark getItem(final int position) {

        return bookmarks.get(position);
    }

    /**
     * Returns the item position in the list.
     * <p>
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public final long getItemId(final int position) {

        return position;
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {

        final Bookmark bm = bookmarks.get(position);

        final View row = inflater.inflate(R.layout.li_bookmark, null);

        ((TextView) row.findViewById(R.id.bookmark_label)).setText(bm.getLabel());
        final Integer iconResId = ICONS.get(bm.getType());
        if (null != iconResId) {
            ((ImageView) row.findViewById(R.id.bookmark_type_icon)).setImageResource(iconResId);
        }
        return row;
    }
}
