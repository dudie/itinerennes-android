package fr.itinerennes.business.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import android.content.Context;

import fr.dudie.onebusaway.client.IOneBusAwayClient;
import fr.dudie.onebusaway.model.ArrivalAndDeparture;
import fr.dudie.onebusaway.model.BusStation;
import fr.dudie.onebusaway.model.Stop;
import fr.dudie.onebusaway.model.StopSchedule;
import fr.dudie.onebusaway.model.TripSchedule;
import fr.itinerennes.database.DatabaseHelper;
import fr.itinerennes.database.GtfsDao;

public class GtfsService implements IOneBusAwayClient {

    private final GtfsDao dao;
    
    public GtfsService(final Context context, final DatabaseHelper databaseHelper) {
        this.dao = new GtfsDao(context, databaseHelper);
    }
    
    public List<BusStation> getStopsForRoute(String routeId, String direction) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public TripSchedule getTripDetails(String tripId) throws IOException {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public List<ArrivalAndDeparture> getArrivalsAndDeparturesForStop(String stopId) throws IOException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public StopSchedule getScheduleForStop(String stopId, Date date) throws IOException {
        final Stop stop = dao.getStop(stopId);

        final StopSchedule schedule = new StopSchedule();
        schedule.setDate(date);
        schedule.setStop(stop);
        schedule.getStopTimes().addAll(dao.getStopTimes(stopId, date));

        return schedule;
    }

    public Stop getStop(String stopId) throws IOException {
        return dao.getStop(stopId);
    }

}
