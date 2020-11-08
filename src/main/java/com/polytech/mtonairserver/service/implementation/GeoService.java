package com.polytech.mtonairserver.service.implementation;

import com.google.gson.*;
import com.polytech.mtonairserver.customexceptions.miscellaneous.EmptyBodyJsonResponseException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.CoordinatesRetrievalException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.external.aqicn.AqicnHttpCaller;
import com.polytech.mtonairserver.model.ReponseObject.CityData;
import com.polytech.mtonairserver.model.entities.StationEntity;
import com.polytech.mtonairserver.service.interfaces.IGeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class GeoService implements IGeoService
{
    private AqicnHttpCaller httpCaller;

    private StationService stationService;

    private Resource stations_geo;

    @Autowired
    public GeoService(AqicnHttpCaller httpCaller, StationService stationService,
                      @Value(value = "classpath:/data/stations_geo.json") Resource stations_geo)
    {
        this.httpCaller = httpCaller;
        this.stationService = stationService;
        this.stations_geo = stations_geo;
    }


    @Override
    public void saveAllAqicnStationsCoordinatesToJsonResourceFile() throws IOException, InterruptedException, ExecutionException, InvalidTokenException, CoordinatesRetrievalException, RequestErrorException, UnknownStationException
    {
        Gson gson = new GsonBuilder().create();
        List<CityData> citiesData = this.getAllAqicnStationsCoordinates();
        Writer writer = new FileWriter(this.stations_geo.getFile().getPath());
        gson.toJson(citiesData, writer);
        // CAUTION : flush does not seem to work so in  order to retrieve all coordinates to json resource file, break here
        // and get the json string content !

        // or fix the writer so it does well access the resource files or save it to a specific folder (cause resources
        // files are supposed to be read only)
        writer.flush();
        writer.close();
    }


    private static boolean exceptionOccured = false;

    @Override
    public List<CityData> getAllAqicnStationsCoordinates() throws UnknownStationException, InvalidTokenException, RequestErrorException, CoordinatesRetrievalException, ExecutionException, InterruptedException
    {
        List<StationEntity> stationsCausingErrors = new ArrayList<>();

        // todo : can this be factorized ? cause it is used many times in the code (threading)
        List<CityData> stationsCoordinates = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newWorkStealingPool();
        List<Future<?>> futures = new ArrayList<Future<?>>();
        List<StationEntity> allStations = this.stationService.findAll();
        for(StationEntity station : allStations)
        {


            futures.add
            (
                executor.submit( () ->
                {
                    JsonObject currentStationJson = null;
                    try
                    {
                        System.out.println(station.getUrl());
                        currentStationJson = this.httpCaller.callExternalApi(station.getUrl());
                    }
                    catch (RequestErrorException | InvalidTokenException | UnknownStationException | EmptyBodyJsonResponseException e)
                    {
                        exceptionOccured = true;
                        stationsCausingErrors.add(station);
                        e.printStackTrace();
                    }

                    if(currentStationJson != null)
                    {
                        JsonObject city = currentStationJson.getAsJsonObject("data").getAsJsonObject("city");
                        if(!city.get("geo").isJsonNull())
                        {
                            CityData cityObject = new Gson().fromJson(city.toString(), CityData.class);
                            stationsCoordinates.add(cityObject);
                        }
                        else
                        {
                            exceptionOccured = true;
                        }

                    }
                    else
                    {
                        exceptionOccured = true;
                        stationsCausingErrors.add(station);
                    }
                })
            );

        }

        if(exceptionOccured)
        {
            exceptionOccured = false;
            throw new CoordinatesRetrievalException("Could not retrieve the coordinates for the given station(s).", GeoService.class, stationsCausingErrors);
        }

        //blocking : waits for all threads to be completed
        for(Future<?> future : futures)
        {
            future.get();
        }

        boolean allDone = true;
        for(Future<?> future : futures)
        {
            allDone &= future.isDone();
        }
        executor.shutdown();

        return stationsCoordinates;
    }

}
