package com.polytech.mtonairserver.model.entities;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "station", schema = "mtonairserver", catalog = "")
public class StationEntity
{
    private int idStation;
    private String stationName;
    private String url;
    private String country;
    private String subdivision1;
    private String subdivision2;
    private String subdivision3;
    private String iso2;
    private Collection<DailyAqicnDataEntity> dailyAqicnDataByIdStation;
    private Collection<ForecastEntity> forecastsByIdStation;
    private Double latitude;
    private Double longitude;

    public StationEntity() {}

    public StationEntity(int idStation, String stationName, String url, String country, String subdivision1, String subdivision2, String subdivision3, String iso2)
    {
        this.idStation = idStation;
        this.stationName = stationName;
        this.url = url;
        this.country = country;
        this.subdivision1 = subdivision1;
        this.subdivision2 = subdivision2;
        this.subdivision3 = subdivision3;
        this.iso2 = iso2;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_station", nullable = false)
    public int getIdStation()
    {
        return idStation;
    }

    public void setIdStation(int idStation)
    {
        this.idStation = idStation;
    }

    @Basic
    @Column(name = "station_name", nullable = false, length = 300)
    public String getStationName()
    {
        return stationName;
    }

    public void setStationName(String stationName)
    {
        this.stationName = stationName;
    }

    @Basic
    @Column(name = "url", nullable = false, length = 300)
    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Basic
    @Column(name = "country", nullable = false, length = 100)
    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    @Basic
    @Column(name = "subdivision1", nullable = true, length = 100)
    public String getSubdivision1()
    {
        return subdivision1;
    }

    public void setSubdivision1(String subdivision1)
    {
        this.subdivision1 = subdivision1;
    }

    @Basic
    @Column(name = "subdivision2", nullable = true, length = 100)
    public String getSubdivision2()
    {
        return subdivision2;
    }

    public void setSubdivision2(String subdivision2)
    {
        this.subdivision2 = subdivision2;
    }

    @Basic
    @Column(name = "subdivision3", nullable = true, length = 100)
    public String getSubdivision3()
    {
        return subdivision3;
    }

    public void setSubdivision3(String subdivision3)
    {
        this.subdivision3 = subdivision3;
    }

    @Basic
    @Column(name = "iso2", nullable = false, length = 3)
    public String getIso2()
    {
        return iso2;
    }

    public void setIso2(String iso2)
    {
        this.iso2 = iso2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StationEntity that = (StationEntity) o;
        return idStation == that.idStation &&
                Objects.equals(stationName, that.stationName) &&
                Objects.equals(url, that.url) &&
                Objects.equals(country, that.country) &&
                Objects.equals(subdivision1, that.subdivision1) &&
                Objects.equals(subdivision2, that.subdivision2) &&
                Objects.equals(subdivision3, that.subdivision3) &&
                Objects.equals(iso2, that.iso2) &&
                Objects.equals(dailyAqicnDataByIdStation, that.dailyAqicnDataByIdStation) &&
                Objects.equals(forecastsByIdStation, that.forecastsByIdStation) &&
                Objects.equals(latitude, that.latitude) &&
                Objects.equals(longitude, that.longitude);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idStation, stationName, url, country, subdivision1, subdivision2, subdivision3, iso2, dailyAqicnDataByIdStation, forecastsByIdStation, latitude, longitude);
    }

    @Override
    public String toString()
    {
        return "StationEntity{" +
                ", stationName='" + stationName + '\'' +
                ", url='" + url + '\'' +
                ", country='" + country + '\'' +
                ", iso2='" + iso2 + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    @OneToMany(mappedBy = "stationByIdStation")
    public Collection<DailyAqicnDataEntity> getDailyAqicnDataByIdStation() {
        return dailyAqicnDataByIdStation;
    }

    public void setDailyAqicnDataByIdStation(Collection<DailyAqicnDataEntity> dailyAqicnDataByIdStation) {
        this.dailyAqicnDataByIdStation = dailyAqicnDataByIdStation;
    }

    @OneToMany(mappedBy = "stationByIdStation")
    public Collection<ForecastEntity> getForecastsByIdStation() {
        return forecastsByIdStation;
    }

    public void setForecastsByIdStation(Collection<ForecastEntity> forecastsByIdStation) {
        this.forecastsByIdStation = forecastsByIdStation;
    }

    @Basic
    @Column(name = "latitude", nullable = true, precision = 0)
    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude", nullable = true, precision = 0)
    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }
}
