package com.polytech.mtonairserver.customexceptions;

import java.io.Serializable;
import java.util.Date;

/**
 * A loggable exception.
 */

public abstract class LoggableException extends Exception implements Serializable
{
    protected Date date;
    protected String errorMessage;
    protected String controllerName;
}
