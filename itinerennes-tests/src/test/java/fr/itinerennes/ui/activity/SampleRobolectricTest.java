package fr.itinerennes.ui.activity;

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

import static com.xtremelabs.robolectric.Robolectric.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.view.View;
import android.widget.ListView;

import com.xtremelabs.robolectric.shadows.ShadowActivity;

import fr.itinerennes.R;

//@RunWith(RobolectricTestRunner.class)
public class SampleRobolectricTest {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleRobolectricTest.class);

    private final NetworkAlertsActivity act = new NetworkAlertsActivity();

    // @org.junit.Test
    public void test() {

        act.onCreate(null);

        final ListView list = (ListView) act.findViewById(R.id.alerts_list);

        final View alert = list.getChildAt(2);
        alert.performClick();

        final ShadowActivity sact = shadowOf(act);
        System.out.println(sact.getLastShownDialogId());
    }
}
