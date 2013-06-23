package fr.itinerennes.database.query;

import android.database.Cursor;
import fr.dudie.onebusaway.model.Stop;

public class StopById extends ParameterizedQuery<Stop> {

    private static final int STOP_ID = 0;
    private static final int STOP_CODE = 1;
    private static final int STOP_NAME = 2;
    private static final int STOP_LAT = 3;
    private static final int STOP_LON = 4;

    public void setStopId(final String stopId) {
        setParam("stopId", stopId);
    }

    @Override
    protected Stop handleResult(final Cursor result) {
        final Stop s;

        if (result.moveToNext()) {
            s = new Stop();
            s.setId(result.getString(STOP_ID));
            s.setCode(result.getInt(STOP_CODE));
            s.setName(result.getString(STOP_NAME));
            s.setLat(result.getInt(STOP_LAT) / 1E6);
            s.setLon(result.getInt(STOP_LON) / 1E6);
            s.setDirection(null);
        } else {
            s = null;
        }

        return s;
    }
}
