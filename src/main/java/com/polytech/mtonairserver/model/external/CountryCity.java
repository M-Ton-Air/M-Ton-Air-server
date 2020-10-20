package com.polytech.mtonairserver.model.external;

public class CountryCity {

    private String country;
    private String city;
    private String iso3;

    public CountryCity(String country, String city, String iso3) {
        this.country = country;
        this.city = city;
        this.iso3 = iso3;
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
}
