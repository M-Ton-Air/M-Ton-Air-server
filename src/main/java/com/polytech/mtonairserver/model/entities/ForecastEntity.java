package com.polytech.mtonairserver.model.entities;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "forecast", schema = "mtonairserver", catalog = "")
@IdClass(ForecastEntityPK.class)
public class ForecastEntity {
    private int idForecast;
    private int idStation;
    private Date idDateForecast;
    private Timestamp dateForecasted;
    private double measureAverage;
    private double measureMin;
    private double measureMax;

    @Id
    @Column(name = "id_forecast")
    public int getIdForecast() {
        return idForecast;
    }

    public void setIdForecast(int idForecast) {
        this.idForecast = idForecast;
    }

    @Id
    @Column(name = "id_station")
    public int getIdStation() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    @Id
    @Column(name = "id_date_forecast")
    public Date getIdDateForecast() {
        return idDateForecast;
    }

    public void setIdDateForecast(Date idDateForecast) {
        this.idDateForecast = idDateForecast;
    }

    @Basic
    @Column(name = "date_forecasted")
    public Timestamp getDateForecasted() {
        return dateForecasted;
    }

    public void setDateForecasted(Timestamp dateForecasted) {
        this.dateForecasted = dateForecasted;
    }

    @Basic
    @Column(name = "measure_average")
    public double getMeasureAverage() {
        return measureAverage;
    }

    public void setMeasureAverage(double measureAverage) {
        this.measureAverage = measureAverage;
    }

    @Basic
    @Column(name = "measure_min")
    public double getMeasureMin() {
        return measureMin;
    }

    public void setMeasureMin(double measureMin) {
        this.measureMin = measureMin;
    }

    @Basic
    @Column(name = "measure_max")
    public double getMeasureMax() {
        return measureMax;
    }

    public void setMeasureMax(double measureMax) {
        this.measureMax = measureMax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForecastEntity that = (ForecastEntity) o;
        return idForecast == that.idForecast &&
                idStation == that.idStation &&
                Double.compare(that.measureAverage, measureAverage) == 0 &&
                Double.compare(that.measureMin, measureMin) == 0 &&
                Double.compare(that.measureMax, measureMax) == 0 &&
                Objects.equals(idDateForecast, that.idDateForecast) &&
                Objects.equals(dateForecasted, that.dateForecasted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idForecast, idStation, idDateForecast, dateForecasted, measureAverage, measureMin, measureMax);
    }
}
