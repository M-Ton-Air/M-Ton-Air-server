package com.polytech.mtonairserver.customexceptions.accountcreation;

import com.polytech.mtonairserver.customexceptions.accountcreation.AccountCreationException;

public class UnvalidEmailException extends AccountCreationException
{
    /**
     * Default constructor for an account creation exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public UnvalidEmailException(String _errorMessage, Class<?> classInWhichExceptionOccured, String _invalidMail)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        this.invalidMail = _invalidMail;
    }

    private String invalidMail;

    public String getInvalidMail()
    {
        return invalidMail;
    }
}
