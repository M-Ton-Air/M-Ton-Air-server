package com.polytech.mtonairserver.customexceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * A loggable exception.
 */

public abstract class LoggableException extends Exception implements Serializable
{
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected Date date;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    protected String controllerName;

    /**
     * Default constructor for a loggable exception.
     * @param _errorMessage the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public LoggableException(String _errorMessage, Class<?> classInWhichExceptionOccured)
    {
        super(_errorMessage);
        this.controllerName = classInWhichExceptionOccured.getSimpleName();
        this.date = new Date();
        ExceptionLogger.logException(this);
    }

    public Date getDate()
    {
        return date;
    }

    public String getControllerName()
    {
        return controllerName;
    }
}
