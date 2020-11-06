package com.polytech.mtonairserver.model.ReponseObject;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributionsData {

    @JsonProperty("url")
    String url;

    @JsonProperty("name")
    String name;

    @JsonProperty("logo")
    String logo;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }
}
