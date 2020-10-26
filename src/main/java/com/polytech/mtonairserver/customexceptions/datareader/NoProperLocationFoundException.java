package com.polytech.mtonairserver.customexceptions.datareader;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;

public class NoProperLocationFoundException extends LoggableException
{
    /**
     * Default constructor for a loggable exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public NoProperLocationFoundException(String _errorMessage, Class<?> classInWhichExceptionOccured, String _unrecognizedLocation)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        this.unrecognizedLocation = _unrecognizedLocation;
        this.setStackTrace(new StackTraceElement[]{this.getStackTrace()[0], this.getStackTrace()[1], this.getStackTrace()[2]});
        this.logException();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String unrecognizedLocation;

    public String getUnrecognizedLocation()
    {
        return unrecognizedLocation;
    }

}
