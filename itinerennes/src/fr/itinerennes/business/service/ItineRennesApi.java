package fr.itinerennes.business.service;

import java.io.IOException;
import java.util.Date;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import fr.itinerennes.Conf;
import fr.itinerennes.api.client.ItineRennesApiClient;
import fr.itinerennes.api.client.JsonItineRennesApiClient;
import fr.itinerennes.api.client.model.Agency;
import fr.itinerennes.api.client.model.FeedInfo;
import fr.itinerennes.api.client.model.StopSchedule;
import fr.itinerennes.api.client.model.StopWithRoutes;
import fr.itinerennes.api.client.model.TripSchedule;

/**
 * @author Jeremie Huchet
 */
@EBean
public class ItineRennesApi implements ItineRennesApiClient {

    private ItineRennesApiClient api;

    @Bean
    ItineRennesHttpClient http;

    @AfterInject
    void initializeItinerennesApiClient() {
        api = new JsonItineRennesApiClient(http, Conf.ITINERENNES_API_URL);
    }

    @Override
    public Agency getAgency(String arg0) throws IOException {
        return api.getAgency(arg0);
    }

    @Override
    public FeedInfo getFeedInfo() throws IOException {
        return api.getFeedInfo();
    }

    @Override
    public StopSchedule getScheduleForStop(String arg0, Date arg1)
            throws IOException {
        return api.getScheduleForStop(arg0, arg1);
    }

    @Override
    public StopWithRoutes getStop(String arg0) throws IOException {
        return api.getStop(arg0);
    }

    @Override
    public TripSchedule getTripDetails(String arg0) throws IOException {
        return api.getTripDetails(arg0);
    }
}
