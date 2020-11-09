package com.polytech.mtonairserver.model.reponseobject.data.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MeasureDailyForecastData {

    private ValuesMeasureDailyForecastData[] o3;
    private ValuesMeasureDailyForecastData[] pm25;
    private ValuesMeasureDailyForecastData [] pm10;
    private ValuesMeasureDailyForecastData [] uvi;

    public ValuesMeasureDailyForecastData[] getO3() {
        return o3;
    }

    public ValuesMeasureDailyForecastData[] getPm25() {
        return pm25;
    }

    public ValuesMeasureDailyForecastData[] getPm10() {
        return pm10;
    }

    public ValuesMeasureDailyForecastData[] getUvi() {
        return uvi;
    }
}
