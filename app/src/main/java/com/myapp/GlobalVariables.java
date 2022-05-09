package com.myapp;

import java.util.ArrayList;
import java.util.Locale;

public class GlobalVariables {
    public static String username = null;
    public static ArrayList listAllWords = null;
    public static ArrayList listAllSavedWords = null;
    public static int limit = 15;
    public static int offset = 0;

    //SETTINGS
    public static Locale VOICE_LANGUAGE = Locale.US;
    public static float VOICE_SPEED = 1; // NORMAL

    public static String FILE_CONFIG_NAME = "config.dat";
}
