package com.polytech.mtonairserver.customexceptions.loginexception;

import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;

public class WrongPasswordException extends LoggableException
{

    /**
     * Default constructor for a loggable exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public WrongPasswordException(String _errorMessage, Class<?> classInWhichExceptionOccured) {
        super(_errorMessage, classInWhichExceptionOccured);
        this.logException();
    }
}
