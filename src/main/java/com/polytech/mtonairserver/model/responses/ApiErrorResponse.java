package com.polytech.mtonairserver.model.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import org.springframework.http.HttpStatus;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Class that represents an Error Response that will be sent to the API users in json format)
 */
public class ApiErrorResponse extends ApiResponse
{
    /**
     * Creates an Api Error Response.
     * @param _status the http status code
     * @param _message the error message
     */
    public ApiErrorResponse(HttpStatus _status, String _message, Exception ex)
    {
        super(_status, _message);
        this.statusCode =  _status.toString();
        this.message = _message;
        this.exception = ex;
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private Exception exception;

    public Exception getException()
    {
        return exception;
    }
}
