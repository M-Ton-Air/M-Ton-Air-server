package com.polytech.mtonairserver.customexceptions.requestaqicnexception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.polytech.mtonairserver.customexceptions.ExceptionLogger;
import com.polytech.mtonairserver.customexceptions.LoggableException;
import com.polytech.mtonairserver.model.entities.StationEntity;

import java.util.List;

public class CoordinatesRetrievalException extends LoggableException
{
    /**
     * Default constructor for a loggable exception.
     *
     * @param _errorMessage                the error message.
     * @param classInWhichExceptionOccured the class in which the exception occured.
     */
    public CoordinatesRetrievalException(String _errorMessage, Class<?> classInWhichExceptionOccured, List<StationEntity> stationsCausingErrors)
    {
        super(_errorMessage, classInWhichExceptionOccured);
        this.stationsCausingErrors = stationsCausingErrors;
        this.logException();
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    private List<StationEntity> stationsCausingErrors;

    public List<StationEntity> getStationsCausingErrors()
    {
        return stationsCausingErrors;
    }
}
