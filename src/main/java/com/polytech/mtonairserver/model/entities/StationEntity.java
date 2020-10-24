package com.polytech.mtonairserver.model.entities;


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
    private String region;
    private String city;
    private Collection<DailyAqicnDataEntity> dailyAqicnDataByIdStation;
    private Collection<ForecastEntity> forecastsByIdStation;

    public StationEntity(int idStation, String stationName, String url, String country, String region, String city)
    {
        this.idStation = idStation;
        this.stationName = stationName;
        this.url = url;
        this.country = country;
        this.region = region;
        this.city = city;
    }

    public StationEntity() {}

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
    @Column(name = "region", nullable = true, length = 100)
    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    @Basic
    @Column(name = "city", nullable = false, length = 100)
    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
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
                Objects.equals(region, that.region) &&
                Objects.equals(city, that.city);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(idStation, stationName, url, country, region, city);
    }

    @Override
    public String toString()
    {
        return "StationEntity{" +
                "idStation=" + idStation +
                ", stationName='" + stationName + '\'' +
                ", url='" + url + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
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
}
