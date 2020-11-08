package com.polytech.mtonairserver.customexceptions.miscellaneous;

import com.polytech.mtonairserver.customexceptions.LoggableException;

public class EmptyBodyJsonResponseException extends LoggableException
{
    /**
     * Default constructor for a loggable exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public EmptyBodyJsonResponseException(String _errorMessage, Class<?> classInWhichExceptionOccured)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        logException();
    }
}
