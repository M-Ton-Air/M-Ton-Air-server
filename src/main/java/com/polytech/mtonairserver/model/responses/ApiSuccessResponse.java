package com.polytech.mtonairserver.model.responses;

import org.springframework.http.HttpStatus;

public class ApiSuccessResponse extends ApiResponse
{
    /**
     * Creates an API success Response.
     *
     * @param _status  the status code (http)
     * @param _message the success message (response)
     */
    public ApiSuccessResponse(HttpStatus _status, String _message)
    {
        super(_status, _message);
    }
}
