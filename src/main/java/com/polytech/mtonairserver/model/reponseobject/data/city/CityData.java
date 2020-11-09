package com.polytech.mtonairserver.model.reponseobject.data.city;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CityData {

    private Double [] geo;
    private String name;
    private String url;

    public Double[] getGeo() {
        return geo;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
