package com.polytech.mtonairserver.model.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Abstract class representing an api response.
 */
public abstract class ApiResponse
{
    // the http status code string formatted
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected String statusCode;

    // Represents when the error occured
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected String date;

    // error or success message
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected String message;

    /**
     *  Creates an API Response.
     * @param _status the status code (http)
     * @param _message the message (response)
     */
    public ApiResponse(HttpStatus _status, String _message)
    {
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        this.date = sdf.format(dt);
        this.message = _message;
        this.statusCode = _status.toString();
    }
}
