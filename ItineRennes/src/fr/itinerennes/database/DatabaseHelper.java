package fr.itinerennes.database;

import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.itinerennes.ItineRennesConstants;
import fr.itinerennes.utils.FileUtils;

/**
 * The database helper. Manage database creation and update.
 * 
 * @author Jérémie Huchet
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(DatabaseHelper.class);

    /**
     * Creates the database helper.
     * 
     * @param context
     *            the context
     */
    public DatabaseHelper(final Context context) {

        super(context, ItineRennesConstants.DATABASE_NAME, null,
                ItineRennesConstants.DATABASE_VERSION);
    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    @Override
    public final void onCreate(final SQLiteDatabase db) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("database creation - name=%s, version=%d, script=%s",
                    ItineRennesConstants.DATABASE_NAME, ItineRennesConstants.DATABASE_VERSION,
                    ItineRennesConstants.DATABASE_CREATE_SCRIPT_URI));
        }
        execScript(
                db,
                FileUtils.read(getClass().getResourceAsStream(
                        ItineRennesConstants.DATABASE_CREATE_SCRIPT_URI)));

    }

    /**
     * {@inheritDoc}
     * 
     * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
     *      int, int)
     */
    @Override
    public final void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("database upgrade - name=%s, oldVersion=%d, newVersion=%d",
                    ItineRennesConstants.DATABASE_NAME, oldVersion, newVersion));
        }
        execScript(
                db,
                FileUtils.read(getClass().getResourceAsStream(
                        ItineRennesConstants.DATABASE_DROP_SCRIPT_URI)));
        execScript(
                db,
                FileUtils.read(getClass().getResourceAsStream(
                        ItineRennesConstants.DATABASE_CREATE_SCRIPT_URI)));
    }

    /**
     * Executes the given script on the database. The script must contains ';' only for statement
     * separation. If a value contains a ';' the execution will crash.
     * 
     * @param db
     * @param script
     */
    private final void execScript(final SQLiteDatabase db, final String script) {

        final String[] statements = script.replaceAll("[\r\n]*", "").replaceAll("[\n\r]*\\s*$", "")
                .split(";");
        for (final String statement : statements) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("execute SQL : [{}]", statement);
            }
            db.execSQL(statement);
        }
    }
}
