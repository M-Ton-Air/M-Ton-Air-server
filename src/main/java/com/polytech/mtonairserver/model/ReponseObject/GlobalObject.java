package com.polytech.mtonairserver.model.ReponseObject;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GlobalObject {

    @JsonProperty("status")
    String status;

    @JsonProperty("data")
    Data data;

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }
}
