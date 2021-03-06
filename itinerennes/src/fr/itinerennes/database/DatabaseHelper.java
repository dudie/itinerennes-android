package fr.itinerennes.database;

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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.itinerennes.Conf;
import fr.itinerennes.utils.IOUtils;

/**
 * The database helper. Manage database creation and update.
 * 
 * @author Jérémie Huchet
 */
public final class DatabaseHelper extends SQLiteOpenHelper {

    /** The event logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    /** The database name. */
    private static final String DATABASE_NAME = "fr.itinerennes";

    /** The database create script URI. */
    private static final String CREATE_SCRIPT = "database/create.sql";

    /** The database drop script URI. */
    private static final String DROP_SCRIPT = "database/drop.sql";

    /** The database upgrade script URI. */
    private static final String UPGRADE_SCRIPT = "database/upgrade_%s_to_%s.sql";

    /** The assets manager. */
    private final AssetManager assets;

    /**
     * Creates the database helper.
     * 
     * @param context
     *            the context
     */
    public DatabaseHelper(final Context context) {

        super(context, DATABASE_NAME, null, Conf.DATABASE_SCHEMA_VERSION);
        assets = context.getAssets();
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("database creation - name=%s, version=%d, script=%s",
                    DATABASE_NAME, Conf.DATABASE_SCHEMA_VERSION, CREATE_SCRIPT));
        }

        execScript(db, readScript(CREATE_SCRIPT));
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
     *      int, int)
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("database upgrade - name=%s, oldVersion=%d, newVersion=%d",
                    DATABASE_NAME, oldVersion, newVersion));
        }

        for (int i = oldVersion; i < newVersion; i++) {
            final String script = String.format(UPGRADE_SCRIPT, i, i + 1);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("executing database upgrade script : %s", script));
            }

            execScript(db, readScript(script));
        }

    }

    /**
     * Reads a script located in the assets folder.
     * 
     * @param filename
     *            the name of the asset to read
     * @return the content of the script
     */
    private String readScript(final String filename) {

        try {
            return IOUtils.read(assets.open(filename, AssetManager.ACCESS_STREAMING));
        } catch (final IOException e) {
            final String msg = String
                    .format("Failed to read packaged database script: an error occured while accessing asset %s",
                            filename);
            LOGGER.error(msg);
            throw new IllegalStateException(msg, e);
        }
    }

    /**
     * Executes the given script on the database. The script must contains ';' only for statement
     * separation. If a value contains a ';' the execution will crash.
     * 
     * @param db
     *            the database to use
     * @param script
     *            the sql script to execute
     */
    private void execScript(final SQLiteDatabase db, final String script) {

        final String[] statements = script.replaceAll("[\r\n]*", "").replaceAll("[\n\r]*\\s*$", "")
                .split(";");

        try {
            db.beginTransaction();
            for (final String statement : statements) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("execute SQL : [{}]", statement);
                }
                db.execSQL(statement);

            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
