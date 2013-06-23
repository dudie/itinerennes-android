package fr.itinerennes.database.query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import fr.dudie.onebusaway.model.Route;
import fr.dudie.onebusaway.model.ScheduleStopTime;

public class ScheduleForStop extends ParameterizedQuery<List<ScheduleStopTime>> {

    private static final int ARRIVAL_TIME = 0;
    private static final int DEPARTURE_TIME = 1;
    private static final int HEADSIGN = 2;
    private static final int SERVICE_ID = 3;
    private static final int TRIP_ID = 4;
    private static final int ROUTE_ID = 5;
    private static final int AGENCY_ID = 6;
    private static final int ROUTE_SHORT_NAME = 7;
    private static final int ROUTE_LONG_NAME = 8;
    private static final int ROUTE_DESCRIPTION = 9;
    private static final int ROUTE_TYPE = 10;
    private static final int ROUTE_COLOR = 11;
    private static final int ROUTE_TEXT_COLOR = 12;

    public void setDate(final Date date) {
        setParam("date", new SimpleDateFormat("yyyyMMdd").format(date));
    }

    public void setStopId(final String stopId) {
        setParam("stopId", stopId);
    }

    @Override
    protected List<ScheduleStopTime> handleResult(final Cursor results) {

        final List<ScheduleStopTime> stopTimes = new ArrayList<ScheduleStopTime>();

        while (results.moveToNext()) {
            final ScheduleStopTime st = new ScheduleStopTime();
            st.setArrivalTime(new Date(results.getInt(ARRIVAL_TIME)));
            st.setDepartureTime(new Date(results.getInt(DEPARTURE_TIME)));
            st.setHeadsign(results.getString(HEADSIGN));
            st.setServiceId(results.getString(SERVICE_ID));
            st.setTripId(results.getString(TRIP_ID));

            final Route r = new Route();
            r.setId(results.getString(ROUTE_ID));
            r.setAgencyId(results.getString(AGENCY_ID));
            r.setShortName(results.getString(ROUTE_SHORT_NAME));
            r.setLongName(results.getString(ROUTE_LONG_NAME));
            r.setDescription(results.getString(ROUTE_DESCRIPTION));
            r.setType(results.getInt(ROUTE_TYPE));
            r.setColor(results.getString(ROUTE_COLOR));
            r.setTextColor(results.getString(ROUTE_TEXT_COLOR));
            st.setRoute(r);
        }
        return stopTimes;
    }
}
