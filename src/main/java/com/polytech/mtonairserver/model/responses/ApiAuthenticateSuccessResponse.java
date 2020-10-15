package com.polytech.mtonairserver.model.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

public class ApiAuthenticateSuccessResponse extends ApiSuccessResponse
{
    /**
     * Creates an API success Response.
     *
     * @param _status  the status code (http)
     * @param _message the success message (response)
     * @param _userId the userentity id
     * @param _apiToken the generated userentity token.
     */
    public ApiAuthenticateSuccessResponse(HttpStatus _status, String _message, int _userId, String _apiToken)
    {
        super(_status, _message);
        this.userId = _userId;
        this.apiToken = _apiToken;
    }

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private int userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String apiToken;


}