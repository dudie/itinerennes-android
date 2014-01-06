package fr.itinerennes.ui.views;

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

    /**
     * Sets the max bounds the image view must fit to.
     * 
     * @param dipWidth
     *            the maximum width in DIP
     * @param dipHeight
     *            the maximum height in DIP
     */
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
        setMaxHeight(pxHeight);
        setMaxWidth(pxWidth);
    }

    /**
     * Sets the max width the image view must fit to. The height is automatically adjusted to keep
     * image ratio.
     * 
     * @param dipWidth
     *            the maximum width in DIP
     */
    public void fitToWidth(final int dipWidth) {

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final int pxWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipWidth,
                metrics);

        final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        setLayoutParams(params);
        setAdjustViewBounds(true);
        setMaxWidth(pxWidth);
    }

    /**
     * Sets the max height the image view must fit to. The width is automatically adjusted to keep
     * image ratio.
     * 
     * @param dipHeight
     *            the maximum height in DIP
     */
    public void fitToHeight(final int dipHeight) {

        final DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        final int pxHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipHeight, metrics);

        final LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);

        setLayoutParams(params);
        setAdjustViewBounds(true);
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
