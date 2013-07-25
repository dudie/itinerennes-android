package fr.itinerennes.database;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import fr.itinerennes.Conf;
import fr.itinerennes.utils.IOUtils;

@RunWith(RobolectricTestRunner.class)
public class GtfsDaoTest {

    private DatabaseHelper dbHelper;

    @Before
    public void setup() throws IOException {
        dbHelper = new DatabaseHelper(Robolectric.application);
    }

    @Test
    public void test() {
        new SQLiteOpenHelper(Robolectric.application, "", null, 1) {

            /** The event logger. */
            private final Logger LOGGER = LoggerFactory.getLogger(SQLiteOpenHelper.class);

            /** The database name. */
            private static final String DATABASE_NAME = "fr.itinerennes";

            /** The database create script URI. */
            private static final String CREATE_SCRIPT = "database/create.sql";

            /** The database drop script URI. */
            private static final String DROP_SCRIPT = "database/drop.sql";

            /** The database upgrade script URI. */
            private static final String UPGRADE_SCRIPT = "database/upgrade_%s_to_%s.sql";

            /** The assets manager. */
            private final AssetManager assets = Robolectric.application.getAssets();

            /**
             * {@inheritDoc}
             * 
             * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
             */
            @Override
            public void onCreate(final SQLiteDatabase db) {

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String
                            .format("database creation - name=%s, version=%d, script=%s", DATABASE_NAME, Conf.DATABASE_SCHEMA_VERSION, CREATE_SCRIPT));
                }
                db.execSQL("CREATE table test ( \"_id\" INTEGER primary key)");
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
                    LOGGER.debug(String.format("database upgrade - name=%s, oldVersion=%d, newVersion=%d", DATABASE_NAME, oldVersion, newVersion));
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
                    return IOUtils.read(assets.open(filename));
                } catch (final IOException e) {
                    final String msg = String.format("Failed to read packaged database script: an error occured while accessing asset %s", filename);
                    LOGGER.error(msg);
                    throw new IllegalStateException(msg, e);
                }
            }

            /**
             * Executes the given script on the database. The script must
             * contains ';' only for statement separation. If a value contains a
             * ';' the execution will crash.
             * 
             * @param db
             *            the database to use
             * @param script
             *            the sql script to execute
             */
            private void execScript(final SQLiteDatabase db, final String script) {

                final String[] statements = script.replaceAll("[\r\n]*", "").replaceAll("[\n\r]*\\s*$", "").split(";");

                for (final String statement : statements) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("execute SQL : [{}]", statement);
                    }
                    try {
                    db.execSQL(statement);
                    } catch (Throwable t) {
                        LOGGER.info(statement,t);
                    }
                }
            }
        }.getWritableDatabase();
    }

    @Test
    @Ignore
    public void canOpenDatabase() {
        dbHelper.getWritableDatabase();
    }
}
