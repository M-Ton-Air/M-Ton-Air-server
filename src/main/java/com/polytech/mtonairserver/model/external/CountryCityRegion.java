package com.polytech.mtonairserver.model.external;

import java.util.Objects;

public class CountryCity {

    private String country;
    private String city;
    private String iso3;
    private String region;



    public CountryCity(String _country, String _city, String _iso3, String _region) {
        this.country = _country;
        this.city = _city;
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

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCity that = (CountryCity) o;
        return Objects.equals(country, that.country) &&
                Objects.equals(city, that.city) &&
                Objects.equals(iso3, that.iso3) &&
                Objects.equals(region, that.region);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(country, city, iso3, region);
    }

    @Override
    public String toString()
    {
        return "CountryCity{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", iso3='" + iso3 + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
