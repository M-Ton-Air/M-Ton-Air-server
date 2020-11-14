package com.polytech.mtonairserver.customexceptions.stations;

import com.polytech.mtonairserver.customexceptions.LoggableException;

public class StationDoesntExistIntoTheDatabaseException extends LoggableException {
    /**
     * Default constructor for a loggable exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public StationDoesntExistIntoTheDatabaseException(String _errorMessage, Class<?> classInWhichExceptionOccured) {
        super(_errorMessage, classInWhichExceptionOccured);
    }
}
