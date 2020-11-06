package com.polytech.mtonairserver.utils.doubleextensions;

public final class DoubleUtils {

    public static boolean tryParse(String potentialDouble)
    {
        try
        {
            Double.parseDouble(potentialDouble);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
    }
}
