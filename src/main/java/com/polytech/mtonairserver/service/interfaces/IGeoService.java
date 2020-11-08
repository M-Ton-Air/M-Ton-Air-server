package com.polytech.mtonairserver.service.interfaces;

import com.google.gson.JsonObject;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.CoordinatesRetrievalException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.InvalidTokenException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.RequestErrorException;
import com.polytech.mtonairserver.customexceptions.requestaqicnexception.UnknownStationException;
import com.polytech.mtonairserver.model.ReponseObject.CityData;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface IGeoService
{
    public List<CityData> getAllAqicnStationsCoordinates() throws LoggableException, ExecutionException, InterruptedException;

    public void saveAllAqicnStationsCoordinatesToJsonResourceFile() throws IOException, InterruptedException, ExecutionException, LoggableException;
}
