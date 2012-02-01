package fr.itinerennes.test;

import java.io.File;

import org.junit.runners.model.InitializationError;

import com.xtremelabs.robolectric.RobolectricTestRunner;

public class ItineRennesRobolelectricTestRunner extends RobolectricTestRunner {

    public ItineRennesRobolelectricTestRunner(final Class<?> testClass) throws InitializationError {

        super(testClass, new File("../itinerennes"));
    }
}
