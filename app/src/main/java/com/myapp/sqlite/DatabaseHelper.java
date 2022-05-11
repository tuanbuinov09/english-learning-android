package com.myapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;
    private static DatabaseHelper Instance = null;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (Instance == null) {
            Instance = new DatabaseHelper(context.getApplicationContext());
        }
        return Instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TRANSLATION_HISTORY_QUERY);
        db.execSQL(CREATE_TABLE_User);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
        onCreate(db);
    }

    public static final String DB_NAME = "app.db";
    //public static final String DB_NAME = "tudien.db";
    public static final String TABLE_USER = "User";
    public static final String TABLE_TRANSLATION_HISTORY = "translation_history";

    public static final String TABLE_TRANSLATION_HISTORY_ID = "id";
    public static final String TABLE_TRANSLATION_HISTORY_ORIGINAL_TEXT = "original_text";
    public static final String TABLE_TRANSLATION_HISTORY_TRANSLATED_TEXT = "translated_text";
    public static final String TABLE_TRANSLATION_HISTORY_DATE = "date";

    public static final String TABLE_USER_ID_User = "ID_User";
    public static final String TABLE_USER_HoTen = "HoTen";
    public static final String TABLE_USER_Point = "Point";
    public static final String TABLE_USER_Email = "Email";
    public static final String TABLE_USER_SDT = "SDT";

    private static final String CREATE_TABLE_TRANSLATION_HISTORY_QUERY = "CREATE TABLE " + TABLE_TRANSLATION_HISTORY + " (" +
            TABLE_TRANSLATION_HISTORY_ID + " INTEGER PRIMARY KEY, " +
            TABLE_TRANSLATION_HISTORY_ORIGINAL_TEXT + " TEXT, " +
            TABLE_TRANSLATION_HISTORY_TRANSLATED_TEXT + " TEXT, " +
            TABLE_TRANSLATION_HISTORY_DATE + " DATE );";
    private static final String CREATE_TABLE_User = "CREATE TABLE " + TABLE_USER + " (" +
            TABLE_USER_ID_User + " varchar (10) PRIMARY KEY, " +
            TABLE_USER_HoTen + " varchar(50), " +
            TABLE_USER_Point + " varchar(50), " +
            TABLE_USER_Email + " varchar(50), " +
            TABLE_USER_SDT + " varchar(50));";
}
