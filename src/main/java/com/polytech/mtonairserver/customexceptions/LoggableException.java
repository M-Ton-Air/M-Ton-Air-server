package com.polytech.mtonairserver.customexceptions;

import java.io.Serializable;
import java.util.Date;

/**
 * A loggable exception.
 */

public abstract class LoggableException extends Exception implements Serializable
{
    protected Date date;
    protected String errorMessage;
    protected String controllerName;

    /**
     * Default constructor for a loggable exception.
     * @param _errorMessage the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public LoggableException(String _errorMessage, Class<?> classInWhichExceptionOccured)
    {
        this.controllerName = classInWhichExceptionOccured.getSimpleName();
        this.date = new Date();
        this.errorMessage = _errorMessage;
        ExceptionLogger.logException(this);
    }
}
