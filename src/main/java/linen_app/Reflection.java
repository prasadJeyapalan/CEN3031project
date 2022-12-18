package linen_app;


import java.lang.reflect.*;
import java.util.*;

/**
 * @author Austin Franklin
 * Using reflection API to automate listing out varaible names. Entirely static class (functions)
 */

public class Reflection {
    /**
     * Take an object, parse the object's class, and return a vector of the member field names
     * @param obj Any object
     * @return Vector containing the object's classes field names
     */
    public static Vector<String> getClassVariableNames(Object obj) {
        Vector<String> nameVec = new Vector<String>();
        Field[] fArray = obj.getClass().getDeclaredFields();
        for (Field f : fArray) {
            nameVec.add(parseName(f.getName()));
        }
        return nameVec;
    }

    /**
     * Converts aFieldNameLikeThis to A Field Name Like This
     * @param str
     * @return A title formatted string
     */
    public static String parseName(String str) {
        if (str.length() == 0) {
            return "";
        }
        String retStr = "";
        char c = str.charAt(0);
        retStr += Character.toString(c > 0x60 && c < 0x7B ? c - 0x20 : c); //if lower case make upper case
        for (int i = 1; i < str.length(); ++i) {
            c = str.charAt(i); 

            //Forgoing the space between I and D
            if (c == 'I' && i + 1 != str.length() && str.charAt(i + 1) == 'D') {
                retStr += " ID";
                ++i;
                continue;
            }

            if (c > 0x40 && c < 0x5B) { //upper case
                retStr += " "; //add a space infront of it
            }
            retStr += c;
        }
        return retStr;
    }
}