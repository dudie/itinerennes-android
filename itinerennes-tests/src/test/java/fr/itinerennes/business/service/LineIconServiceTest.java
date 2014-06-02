package fr.itinerennes.business.service;

/*
 * [license]
 * Java-based tests
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

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.graphics.drawable.Drawable;

import com.xtremelabs.robolectric.Robolectric;

import fr.itinerennes.R;
import fr.itinerennes.business.service.LineIconService;
import fr.itinerennes.exceptions.GenericException;
import fr.itinerennes.test.ItineRennesRobolelectricTestRunner;

/**
 * @author Jérémie Huchet
 */
@RunWith(ItineRennesRobolelectricTestRunner.class)
public class LineIconServiceTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LineIconServiceTest.class);

    private final Map<Integer, String> iconMapping = new HashMap<Integer, String>();

    private LineIconService iconService;

    public LineIconServiceTest() {

        iconMapping.put(R.drawable.z_ic_line_1, "1");
        iconMapping.put(R.drawable.z_ic_line_9, "9");
        iconMapping.put(R.drawable.z_ic_line_40, "40");
        iconMapping.put(R.drawable.z_ic_line_40, "40EX");
    }

    @Before
    public void setup() {

        iconService = new LineIconService(Robolectric.application);
    }

    @Test
    public void testResolveIcon() throws GenericException {

        for (final java.util.Map.Entry<Integer, String> mapping : iconMapping.entrySet()) {

            final int resId = mapping.getKey();
            final String name = mapping.getValue();

            final Drawable expectedDrawable = Robolectric.application.getResources().getDrawable(
                    resId);

            final String msg = String.format("check drawable for icon name '%s'", name);
            assertEquals(msg, expectedDrawable, iconService.getIcon(name));
        }
    }

    @Test
    public void testResolveUnknownIcon() throws GenericException {

        assertNotNull(iconService.getIcon(""));
        assertNotNull(iconService.getIcon("unknown"));
    }
}
