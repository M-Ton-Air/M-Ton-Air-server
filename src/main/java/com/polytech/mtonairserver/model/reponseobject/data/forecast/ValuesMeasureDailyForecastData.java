package com.polytech.mtonairserver.model.reponseobject.data.forecast;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;

public class ValuesMeasureDailyForecastData {


    private Double avg;
    private String day;
    private Double max;
    private Double min;

    public Double getAvg() {
        return avg;
    }

    public String getDay() {
        return day;
    }

    public Double getMax() {
        return max;
    }

    public Double getMin() {
        return min;
    }
}
