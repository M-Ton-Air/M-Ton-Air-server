package com.polytech.mtonairserver.model.reponseobject;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.polytech.mtonairserver.model.reponseobject.data.Data;

public class GlobalObject {

    private String status;
    private Data data;

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }
}
