package com.polytech.mtonairserver.model.external;

import java.util.Objects;

public class CountryCityRegion
{

    private String country;
    private String city;
    private String iso2;
    private String iso3;
    private String region;



    public CountryCityRegion(String _country, String _city, String _iso2, String _iso3, String _region) {
        this.country = _country;
        this.city = _city;
        this.iso2 = _iso2;
        this.iso3 = _iso3;
        this.region = _region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIso2() {
        return iso2;
    }

    public void setIso2(String iso2) {
        this.iso2 = iso2;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getIso3()
    {
        return iso3;
    }

    public void setIso3(String iso3)
    {
        this.iso3 = iso3;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCityRegion that = (CountryCityRegion) o;
        return Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(iso2, that.iso2) &&
                Objects.equals(iso3, that.iso3) &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(country, city, iso2, iso3, region);
    }

    @Override
    public String toString()
    {
        return "CountryCityRegion{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", iso2='" + iso2 + '\'' +
                ", iso3='" + iso3 + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}

