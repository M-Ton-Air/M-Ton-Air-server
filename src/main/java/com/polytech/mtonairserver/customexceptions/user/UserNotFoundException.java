package com.polytech.mtonairserver.customexceptions.user;

import com.polytech.mtonairserver.customexceptions.LoggableException;

public class UserNotFoundException extends LoggableException {
    /**
     * Default constructor for a loggable exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public UserNotFoundException(String _errorMessage, Class<?> classInWhichExceptionOccured) {
        super(_errorMessage, classInWhichExceptionOccured);
        this.logException();
    }
}
