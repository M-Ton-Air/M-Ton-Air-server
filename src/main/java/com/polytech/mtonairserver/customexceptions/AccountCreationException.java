package com.polytech.mtonairserver.customexceptions;

import com.polytech.mtonairserver.controller.AuthenticationController;

import java.util.Date;

/**
 * Exception that occurs when an account can't be created.
 */
public class AccountCreationException extends LoggableException
{
    /**
     * Default constructor for an account creation exception.
     * @param _errorMessage the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public AccountCreationException(String _errorMessage, Class<?> classInWhichExceptionOccured)
    {
        this.controllerName = classInWhichExceptionOccured.getName();
        this.date = new Date();
        this.errorMessage = _errorMessage;
        ExceptionLogger.LogException(this);
    }

}
