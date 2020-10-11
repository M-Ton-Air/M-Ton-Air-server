package com.polytech.mtonairserver.customexceptions.loginexception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;

public class UnknownEmailException extends LoggableException
{
    /**
     * Default constructor for a loggable exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public UnknownEmailException(String _errorMessage, Class<?> classInWhichExceptionOccured, String _unrecognizedEmail) {
        super(_errorMessage, classInWhichExceptionOccured);
        this.unrecognizedEmail = _unrecognizedEmail;
        ExceptionLogger.logException(this);
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String unrecognizedEmail;
}
