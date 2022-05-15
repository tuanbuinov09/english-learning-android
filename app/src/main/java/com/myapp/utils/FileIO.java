package com.myapp.utils;

import android.content.Context;
import android.util.Log;

import com.myapp.GlobalVariables;
import com.myapp.model.Settings;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class FileIO {
    public static void writeToFile(Settings settings, Context context) {
        File path = context.getApplicationContext().getFilesDir();
        File file = new File(path, GlobalVariables.FILE_CONFIG_NAME);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(fos));
            outputStream.writeObject(settings);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Settings readFromFile(Context context) {
        File path = context.getApplicationContext().getFilesDir();
        File file = new File(path, GlobalVariables.FILE_CONFIG_NAME);

        Settings settings = null;
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(fis));
            settings = (Settings) ois.readObject();
            ois.close();
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("read error", e.getLocalizedMessage() == null ? "" : e.getLocalizedMessage());
            e.printStackTrace();
        }
        return settings;
    }
}
