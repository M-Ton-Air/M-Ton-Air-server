package com.polytech.mtonairserver.model.responseobject.data;

import com.polytech.mtonairserver.model.responseobject.data.attributions.AttributionsData;
import com.polytech.mtonairserver.model.responseobject.data.city.CityData;
import com.polytech.mtonairserver.model.responseobject.data.debug.DebugData;
import com.polytech.mtonairserver.model.responseobject.data.forecast.ForecastData;
import com.polytech.mtonairserver.model.responseobject.data.iaqi.IaqiData;
import com.polytech.mtonairserver.model.responseobject.data.time.TimeData;

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
