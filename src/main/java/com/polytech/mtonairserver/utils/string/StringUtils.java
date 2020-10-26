package com.polytech.mtonairserver.utils.string;

public class StringUtils
{

    /**
     * Removes dashes into the contained string and makes every start letter of each word an uppercase letter.
     * @param str the concerned string.
     * @return str without dashes and with the first letter of each single word with an upper case.
     */
    public static String upperRemoveDash(String str)
    {
        if(str == null)
        {
            return null;
        }
        String noDash = str.replace("-", " ");
        String[] splittedResult = noDash.split(" ");
        String finalString = "";
        for(String s : splittedResult)
        {
            if(!s.isEmpty())
            {
                finalString += s.replace(s.charAt(0), Character.toUpperCase(s.charAt(0))) + " ";
            }
        }
        return finalString.trim();
    }
}
