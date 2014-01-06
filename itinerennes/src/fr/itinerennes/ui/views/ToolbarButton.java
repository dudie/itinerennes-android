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

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;

import fr.itinerennes.R;

/**
 * An image view which changes its background when clicked and focused.
 * 
 * @author Olivier Boudet
 * @author Jérémie Huchet
 */
public final class ToolbarButton extends Button {

    /** The final image used to make the button. */
    private final LayerDrawable buttonImage;

    /** The original drawable to make the button. */
    private final Drawable drawable;

    /**
     * Constructor from XML.
     * 
     * @param context
     *            the context
     * @param attrs
     *            the view attributes
     */
    public ToolbarButton(final Context context, final AttributeSet attrs) {

        super(context, attrs);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ToolbarButton,
                0, 0);

        drawable = array.getDrawable(R.styleable.ToolbarButton_drawable);

        if (null == drawable) {
            throw new IllegalArgumentException("Attribute android:drawable is mandatory");
        }

        final Drawable bg = getContext().getResources().getDrawable(R.drawable.bg_toolbar_button);
        buttonImage = new LayerDrawable(new Drawable[] { bg, drawable });

        setBackgroundDrawable(buttonImage);
        setFocusable(true);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.view.View#onLayout(boolean, int, int, int, int)
     */
    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right,
            final int bottom) {

        final int height = getHeight();
        final int width = getWidth();

        buttonImage.setLayerInset(1, (width - drawable.getMinimumWidth()) / 2,
                (height - drawable.getMinimumHeight()) / 2,
                (width - drawable.getMinimumWidth()) / 2,
                (height - drawable.getMinimumHeight()) / 2);

        super.onLayout(changed, left, top, right, bottom);
    }
}
