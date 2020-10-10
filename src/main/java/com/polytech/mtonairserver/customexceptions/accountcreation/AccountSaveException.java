package com.polytech.mtonairserver.customexceptions.accountcreation;

import com.polytech.mtonairserver.customexceptions.accountcreation.AccountCreationException;
import com.polytech.mtonairserver.model.entities.UserEntity;

public class AccountSaveException extends AccountCreationException
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
    }

    private UserEntity concernedUser;
}
