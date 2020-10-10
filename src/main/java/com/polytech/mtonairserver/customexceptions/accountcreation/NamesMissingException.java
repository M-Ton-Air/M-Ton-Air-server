package com.polytech.mtonairserver.customexceptions.accountcreation;

public class NamesMissingException extends  AccountCreationException
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
    }
}
