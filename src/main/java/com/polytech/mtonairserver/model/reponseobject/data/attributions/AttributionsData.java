package com.polytech.mtonairserver.model.reponseobject.data.attributions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributionsData {

    private String url;
    private String name;
    private String logo;

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
