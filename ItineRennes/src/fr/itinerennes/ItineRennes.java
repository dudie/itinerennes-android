package fr.itinerennes;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Application;

import fr.itinerennes.database.DatabaseHelper;

/**
 * @author Jérémie Huchet
 */
@ReportsCrashes(formKey = "dHVwTGY1ZTBOcEQ5SzdXT2dEN2diY3c6MQ")
public class ItineRennes extends Application {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(ItineRennes.class);

    /** The database helper. */
    private DatabaseHelper databaseHelper;

    /**
     * Inits ACRA.
     * 
     * @see android.app.Application#onCreate()
     */
    @Override
    public final void onCreate() {

        // The following line triggers the initialization of ACRA
        ACRA.init(this);

        super.onCreate();

    }

    /**
     * Close the database helper. If this method is derived, you must ensure to call
     * <code>super.onDestroy()</code>.
     * 
     * @see android.app.Application#onTerminate()
     */
    @Override
    public final void onTerminate() {

        super.onTerminate();
        if (databaseHelper != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DBHELPER will request #close() on the database helper");
            }
            databaseHelper.close();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DBHELPER #close() on the database helper terminated");
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("DBHELPER database helper was not opened");
        }
    }

    /**
     * Gets a reference to the database helper.
     * 
     * @return a reference to the database helper
     */
    public final DatabaseHelper getDatabaseHelper() {

        if (databaseHelper == null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("DBHELPER initializing a database helper");
            }
            databaseHelper = new DatabaseHelper(getBaseContext());
        }
        return databaseHelper;
    }
}
