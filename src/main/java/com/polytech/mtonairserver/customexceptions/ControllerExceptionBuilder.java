package com.polytech.mtonairserver.customexceptions;

import com.polytech.mtonairserver.model.responses.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class ControllerExceptionBuilder
{
    public static ResponseEntity<ApiErrorResponse> buildErrorResponseAndPrintStackTrace(HttpStatus status, String message, Exception ex)
    {
        ex.printStackTrace();
        ex.setStackTrace(new StackTraceElement[]{ex.getStackTrace()[0]});
        return new ResponseEntity<ApiErrorResponse>
        (
                new ApiErrorResponse(status, message, ex),
                status
        );
    }
}
