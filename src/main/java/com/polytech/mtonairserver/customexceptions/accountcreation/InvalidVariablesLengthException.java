package com.polytech.mtonairserver.customexceptions.accountcreation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;

import java.util.HashMap;

public class InvalidVariablesLengthException extends LoggableException
{
    /**
     * Default constructor for an account creation exception.
     *  @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     * @param _fieldsLength a hashmap containing the field names and their length when they exceed / are below the needed size.
     */
    public InvalidVariablesLengthException(String _errorMessage, Class<?> classInWhichExceptionOccured, HashMap<String, Integer> _fieldsLength)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        this.fieldsLength = _fieldsLength;
        ExceptionLogger.logException(this);
    }

    // a hashmap containing the field names and their length when they exceed / are below the needed size.
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private HashMap<String, Integer> fieldsLength;

    /**
     *
     * @return a hashmap containing the field names and their length when they exceed / are below the needed size.
     */
    public HashMap<String, Integer> getFieldsLength()
    {
        return fieldsLength;
    }
}
