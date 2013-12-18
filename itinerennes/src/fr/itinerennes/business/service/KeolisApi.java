package fr.itinerennes.business.service;

import java.io.IOException;
import java.util.List;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;

import fr.dudie.keolis.client.JsonKeolisClient;
import fr.dudie.keolis.client.KeolisClient;
import fr.dudie.keolis.model.BikeStation;
import fr.dudie.keolis.model.LineAlert;
import fr.dudie.keolis.model.LineIcon;
import fr.dudie.keolis.model.RelayPark;
import fr.dudie.keolis.model.SubwayStation;
import fr.itinerennes.Conf;

@EBean
public class KeolisApi implements KeolisClient {

    private KeolisClient keolis;

    @Bean
    ItineRennesHttpClient http;

    @AfterInject
    void initializeKeolisApiClient() {
        keolis = new JsonKeolisClient(http, Conf.KEOLIS_API_URL,
                Conf.KEOLIS_API_KEY);
    }

    @Override
    public List<BikeStation> getAllBikeStations() throws IOException {
        return keolis.getAllBikeStations();
    }

    @Override
    public List<BikeStation> getBikeStationsNearFrom(int latitude, int longitude)
            throws IOException {
        return keolis.getBikeStationsNearFrom(latitude, longitude);
    }

    @Override
    public BikeStation getBikeStation(String id) throws IOException {
        return keolis.getBikeStation(id);
    }

    @Override
    public List<SubwayStation> getAllSubwayStations() throws IOException {
        return keolis.getAllSubwayStations();
    }

    @Override
    public List<SubwayStation> getSubwayStationsNearFrom(int latitude,
            int longitude) throws IOException {
        return keolis.getSubwayStationsNearFrom(latitude, longitude);
    }

    @Override
    public SubwayStation getSubwayStation(String id) throws IOException {
        return keolis.getSubwayStation(id);
    }

    @Override
    public List<LineIcon> getAllLineIcons() throws IOException {
        return keolis.getAllLineIcons();
    }

    @Override
    public List<RelayPark> getAllRelayParks() throws IOException {
        return keolis.getAllRelayParks();
    }

    @Override
    public List<RelayPark> getRelayParksNearFrom(int latitude, int longitude)
            throws IOException {
        return keolis.getRelayParksNearFrom(latitude, longitude);
    }

    @Override
    public List<LineAlert> getAllLinesAlerts() throws IOException {
        return keolis.getAllLinesAlerts();
    }

    @Override
    public List<LineAlert> getLinesAlertsForLine(String line)
            throws IOException {
        return keolis.getLinesAlertsForLine(line);
    }
}
