package fr.itinerennes.utils;

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

import org.acra.ErrorReporter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import fr.itinerennes.R;

/**
 * Class providing some useful method to work with application versions.
 * 
 * @author Olivier Boudet
 */
public final class VersionUtils {

    /**
     * Private constructor to avoid instantiation.
     */
    private VersionUtils() {

    }

    /**
     * Compares the specified versions. Returns 0 if the two versions are equals. Returns a negative
     * integer if the second version is greater than the first. Returns a positive integer if the
     * first version is greater than the second.
     * 
     * @param v1
     *            first version
     * @param v2
     *            second version
     * @return 0 if the versions are equals, a negative integer if the second is greater than the
     *         first and a positive integer if the first is greater than the second.
     */
    public static int compare(final String v1, final String v2) {

        final String s1 = normalizeVersion(v1);
        final String s2 = normalizeVersion(v2);

        return s1.compareTo(s2);
    }

    /**
     * Deletes any dots from the specified string.
     * 
     * @param version
     *            version to normalize
     * @return a string without dots
     */
    public static String normalizeVersion(final String version) {

        if (null == version) {
            return "";
        } else {
            return version.replaceAll("\\.", "");
        }
    }

    /**
     * Gets the version of the running ItineRennes instance.
     * 
     * @param context
     *            the context
     * @return the current running version
     */
    public static String getCurrent(final Context context) {

        final PackageManager pkgManager = context.getPackageManager();
        final String pkgName = context.getPackageName();
        String version;

        try {
            version = pkgManager.getPackageInfo(pkgName, 0).versionName;
        } catch (final NameNotFoundException e) {
            version = context.getString(R.string.about_unknown_version);
            ErrorReporter.getInstance().handleSilentException(e);
        }
        return version;
    }

    /**
     * Gets the version code of the running ItineRennes instance.
     * 
     * @param context
     *            the context
     * @return the current code running version
     */
    public static int getCode(final Context context) {

        final PackageManager pkgManager = context.getPackageManager();
        final String pkgName = context.getPackageName();
        int versionCode;

        try {
            versionCode = pkgManager.getPackageInfo(pkgName, 0).versionCode;
        } catch (final NameNotFoundException e) {
            versionCode = 0;
            ErrorReporter.getInstance().handleSilentException(e);
        }
        return versionCode;
    }

}
