package com.polytech.mtonairserver.model.ReponseObject;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {

    @JsonProperty("aqi")
    int aqi;

    @JsonProperty("attributions")
    AttributionsData[] attributions;

    @JsonProperty("city")
    CityData city;

    @JsonProperty("dominentpol")
    String dominentPol;

    @JsonProperty("iaqi")
    IaqiData iaqi;

    @JsonProperty("time")
    TimeData time;

    @JsonProperty("forecast")
    ForecastData forecast;

    @JsonProperty("debug")
    DebugData debug;

    public int getAqi() {
        return aqi;
    }

    public AttributionsData[] getAttributions() {
        return attributions;
    }

    public CityData getCity() {
        return city;
    }

    public String getDominentPol() {
        return dominentPol;
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
