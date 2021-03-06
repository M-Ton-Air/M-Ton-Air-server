package com.polytech.mtonairserver.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.polytech.mtonairserver.model.entities.keys.ForecastEntityPK;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "forecast", schema = "mtonairserver", catalog = "")
@IdClass(ForecastEntityPK.class)
public class ForecastEntity  {
    private int idForecast;
    private int idStation;
    private Date idDateForecast;
    private Timestamp dateForecasted;
    private Double measureAverage;
    private Double measureMin;
    private Double measureMax;
    private StationEntity stationByIdStation;
    private MeasureEntity measureByIdMeasure;
    private int idMeasure;

    @Id
    @Column(name = "id_forecast", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getIdForecast() {
        return idForecast;
    }

    public void setIdForecast(int idForecast) {
        this.idForecast = idForecast;
    }

    @Id
    @Column(name = "id_station", nullable = false, insertable = false, updatable = false)
    public int getIdStation() {
        return idStation;
    }

    public void setIdStation(int idStation) {
        this.idStation = idStation;
    }

    @Id
    @Column(name = "id_date_forecast", nullable = false)
    public Date getIdDateForecast() {
        return idDateForecast;
    }

    public void setIdDateForecast(Date idDateForecast) {
        this.idDateForecast = idDateForecast;
    }

    @Basic
    @Column(name = "date_forecasted", nullable = false)
    public Timestamp getDateForecasted() {
        return dateForecasted;
    }

    public void setDateForecasted(Timestamp dateForecasted) {
        this.dateForecasted = dateForecasted;
    }

    @Basic
    @Column(name = "measure_average", nullable = true, precision = 0)
    public Double getMeasureAverage() {
        return measureAverage;
    }

    public void setMeasureAverage(Double measureAverage) {
        this.measureAverage = measureAverage;
    }

    @Basic
    @Column(name = "measure_min", nullable = true, precision = 0)
    public Double getMeasureMin() {
        return measureMin;
    }

    public void setMeasureMin(Double measureMin) {
        this.measureMin = measureMin;
    }

    @Basic
    @Column(name = "measure_max", nullable = true, precision = 0)
    public Double getMeasureMax() {
        return measureMax;
    }

    public void setMeasureMax(Double measureMax) {
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

    @JsonIgnore
    @ManyToOne
    //@JoinColumn(name = "id_station", referencedColumnName = "id_station", nullable = false)
    @JoinColumn(name = "id_station", updatable = false, insertable = false)
    public StationEntity getStationByIdStation() {
        return stationByIdStation;
    }

    public void setStationByIdStation(StationEntity stationByIdStation) {
        this.stationByIdStation = stationByIdStation;
    }

    @ManyToOne
    //@JoinColumn(name = "id_measure", referencedColumnName = "id_measure", nullable = false)
    @JoinColumn(name = "id_measure", updatable = false, insertable = false)
    public MeasureEntity getMeasureByIdMeasure() {
        return measureByIdMeasure;
    }

    public void setMeasureByIdMeasure(MeasureEntity measureByIdMeasure) {
        this.measureByIdMeasure = measureByIdMeasure;
    }

    @Id
    @Column(name = "id_measure", nullable = false, insertable = false, updatable = false)
    public int getIdMeasure() {
        return idMeasure;
    }

    public void setIdMeasure(int idMeasure) {
        this.idMeasure = idMeasure;
    }
}
