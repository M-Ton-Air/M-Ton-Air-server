package com.polytech.mtonairserver.model.ReponseObject.data;

import com.polytech.mtonairserver.model.ReponseObject.data.attributions.AttributionsData;
import com.polytech.mtonairserver.model.ReponseObject.data.city.CityData;
import com.polytech.mtonairserver.model.ReponseObject.data.debug.DebugData;
import com.polytech.mtonairserver.model.ReponseObject.data.forecast.ForecastData;
import com.polytech.mtonairserver.model.ReponseObject.data.iaqi.IaqiData;
import com.polytech.mtonairserver.model.ReponseObject.data.time.TimeData;

public class Data {

    Double aqi;
    AttributionsData[] attributions;
    CityData city;
    String dominentpol;
    IaqiData iaqi;
    TimeData time;
    ForecastData forecast;
    DebugData debug;

    public Double getAqi() {
        return aqi;
    }

    public AttributionsData[] getAttributions() {
        return attributions;
    }

    public CityData getCity() {
        return city;
    }

    public String getDominentpol() {
        return dominentpol;
    }

    public IaqiData getIaqi() {
        return iaqi;
    }

    public TimeData getTime() {
        return time;
    }

    public ForecastData getForecast() {
        return forecast;
    }

    public DebugData getDebug() {
        return debug;
    }
}
