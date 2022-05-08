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
//        db.execSQL(CREATE_TABLE_PRODUCT_QUERY);
//        db.execSQL(CREATE_TABLE_WAREHOUSE_QUERY);
//        db.execSQL(CREATE_TABLE_RECEIPT_QUERY);
//        db.execSQL(CREATE_TABLE_RECEIPT_DETAIL_QUERY);
//        db.execSQL(CREATE_TABLE_User);
        db.execSQL(CREATE_TABLE_TRANSLATION_HISTORY_QUERY);
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

    public static final String TABLE_TRANSLATION_HISTORY = "translation_history";

    public static final String TABLE_TRANSLATION_HISTORY_ID = "id";
    public static final String TABLE_TRANSLATION_HISTORY_ORIGINAL_TEXT = "original_text";
    public static final String TABLE_TRANSLATION_HISTORY_TRANSLATED_TEXT = "translated_text";
    public static final String TABLE_TRANSLATION_HISTORY_DATE = "translated_text";

    private static final String CREATE_TABLE_TRANSLATION_HISTORY_QUERY = "CREATE TABLE " + TABLE_TRANSLATION_HISTORY + " (" +
            TABLE_TRANSLATION_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TABLE_TRANSLATION_HISTORY_ORIGINAL_TEXT + " TEXT, " +
            TABLE_TRANSLATION_HISTORY_TRANSLATED_TEXT + " TEXT, " +
            TABLE_TRANSLATION_HISTORY_DATE + " DATE );";
}
