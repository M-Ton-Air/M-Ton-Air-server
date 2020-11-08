package com.polytech.mtonairserver.model.ReponseObject;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CityData {

    @JsonProperty("geo")
    Double [] geo;

    @JsonProperty("name")
    String name;

    @JsonProperty("url")
    String url;

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
