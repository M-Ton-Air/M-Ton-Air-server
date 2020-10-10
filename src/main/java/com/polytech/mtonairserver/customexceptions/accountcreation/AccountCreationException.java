package com.polytech.mtonairserver.customexceptions.accountcreation;

import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;

import java.util.Date;

/**
 * Exception that occurs when an account can't be created.
 */
public abstract class AccountCreationException extends LoggableException
{
    /**
     * Default constructor for an account creation exception.
     * @param _errorMessage the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public AccountCreationException(String _errorMessage, Class<?> classInWhichExceptionOccured)
    {
        this.controllerName = classInWhichExceptionOccured.getSimpleName();
        this.date = new Date();
        this.errorMessage = _errorMessage;
        ExceptionLogger.logException(this);
    }

}
