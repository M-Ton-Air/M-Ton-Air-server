package com.polytech.mtonairserver.model.reponseobject.data.time;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeData {

    private String s;
    String iso;

    public String getS() {
        return s;
    }

    public String getIso() {
        return iso;
    }
}
