package com.polytech.mtonairserver.customexceptions.accountcreation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.model.entities.UserEntity;

public class AccountSaveException extends LoggableException
{

    /**
     * Default constructor for an account creation exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public AccountSaveException(String _errorMessage, Class<?> classInWhichExceptionOccured, UserEntity user)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        this.concernedUser = user;
        this.logException();
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private UserEntity concernedUser;
}
