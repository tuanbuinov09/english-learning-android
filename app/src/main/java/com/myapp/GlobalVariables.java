package com.myapp;

import com.myapp.model.EnWord;

import java.util.ArrayList;
import java.util.Locale;

public class GlobalVariables {
    public static String username = null;
    public static ArrayList<EnWord> listAllWords = null;
    public static ArrayList<EnWord>  listAllSavedWords = null;

    public static ArrayList<EnWord>  listFilteredWords = new ArrayList();
    public static int limit = 15;
    public static int offset = 0;

    //SETTINGS
    public static Locale VOICE_LANGUAGE = Locale.US;
    public static float VOICE_SPEED = 1; // NORMAL

    public static String FILE_CONFIG_NAME = "config.dat";
//    public static Settings SETTINGS;
}
