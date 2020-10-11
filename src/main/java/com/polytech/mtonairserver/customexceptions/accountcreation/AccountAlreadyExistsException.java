package com.polytech.mtonairserver.customexceptions.accountcreation;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AccountAlreadyExistsException extends AccountCreationException
{
    /**
     * Default constructor for an account creation exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public AccountAlreadyExistsException(String _errorMessage, Class<?> classInWhichExceptionOccured, String _existingMail)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        this.existingMail = _existingMail;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String existingMail;

    public String getExistingMail()
    {
        return existingMail;
    }
}
