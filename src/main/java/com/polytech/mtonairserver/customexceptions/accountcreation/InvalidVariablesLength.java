package com.polytech.mtonairserver.customexceptions.accountcreation;

public class InvalidVariablesLength extends AccountCreationException
{
    /**
     * Default constructor for an account creation exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public InvalidVariablesLength(String _errorMessage, Class<?> classInWhichExceptionOccured)
    {
        super(_errorMessage, classInWhichExceptionOccured);
    }
}
