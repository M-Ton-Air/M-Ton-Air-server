package com.polytech.mtonairserver.customexceptions.accountcreation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;

public class InvalidEmailException extends LoggableException
{
    /**
     * Default constructor for an account creation exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public InvalidEmailException(String _errorMessage, Class<?> classInWhichExceptionOccured, String _invalidMail)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        this.invalidMail = _invalidMail;
        this.logException();
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String invalidMail;

    public String getInvalidMail()
    {
        return invalidMail;
    }
}
