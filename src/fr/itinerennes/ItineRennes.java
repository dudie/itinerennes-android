package fr.itinerennes;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.slf4j.Logger;
import org.slf4j.impl.ItinerennesLoggerFactory;

import android.app.Application;

/**
 * @author Jérémie Huchet
 */
@ReportsCrashes(formKey = "dHVwTGY1ZTBOcEQ5SzdXT2dEN2diY3c6MQ")
public class ItineRennes extends Application {

    /** The event logger. */
    private static final Logger LOGGER = ItinerennesLoggerFactory.getLogger(ItineRennes.class);

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
}
