package com.polytech.mtonairserver.customexceptions;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Class that handles exception logs.
 * Exceptions are logged over here in dev environment :
 * \target\demo-0.0.1-SNAPSHOT\WEB-INF\classes\M-TON-AIR-LOG
 * and in the WEB-INF\classes\M-TON-AIR-FOLDER in production environment.
 */
public class ExceptionLogger
{

    private static boolean environmentSet = false;

    private static String folderPath = (ExceptionLogger.class.getProtectionDomain().getCodeSource().getLocation().getPath()).replace("%20", " ") + "M-TON-AIR-LOGS/";


    public static void setEnv()
    {
        if(!ExceptionLogger.environmentSet)
        {
            new File(folderPath).mkdir();
            ExceptionLogger.environmentSet = true;
        }
    }

    /**
     *
     * @param exception must be a Loggable Exception.
     */
    public static void logException(Object exception)
    {
        ExceptionLogger.setEnv();
        LoggableException loggable = (LoggableException)exception;
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String filepath = (folderPath + loggable.controllerName + "-" + exception.getClass().getSimpleName() + "-" + ts.toString() + ".json").replace(":", "-").replaceFirst("-", ":");
        try
        {
            Gson gson = new GsonBuilder().create();
            Writer writer = new FileWriter(filepath);
            gson.toJson(loggable, writer);
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
