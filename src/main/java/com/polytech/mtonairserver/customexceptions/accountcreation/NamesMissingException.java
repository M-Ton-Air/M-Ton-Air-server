package com.polytech.mtonairserver.customexceptions.accountcreation;

import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;

public class NamesMissingException extends LoggableException
{
    /**
     * Default constructor for an account creation exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public NamesMissingException(String _errorMessage, Class<?> classInWhichExceptionOccured)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        this.logException();
    }
}
