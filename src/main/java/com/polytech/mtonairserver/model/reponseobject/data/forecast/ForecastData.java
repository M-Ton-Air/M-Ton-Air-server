package com.polytech.mtonairserver.model.reponseobject.data.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ForecastData {

    private MeasureDailyForecastData daily;

    public MeasureDailyForecastData getDaily() {
        return daily;
    }
}
