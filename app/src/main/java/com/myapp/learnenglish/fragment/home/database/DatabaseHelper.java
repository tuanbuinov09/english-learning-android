package com.myapp.learnenglish.fragment.home.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EnglishLearning";

    // table names
    private static final String TABLE_TOPICS = "Topics";
    private static final String TABLE_EXERCISES = "Exercises";
    private static final String TABLE_QUESTIONS = "Questions";

    // topics table column
    private static final String COLUMN_TOPIC_ID = "Id";
    private static final String COLUMN_TOPIC_TITLE = "Title";

    // exercises table column
    private static final String COLUMN_EXERCISE_ID = "Id";
    private static final String COLUMN_EXERCISE_TITLE = "Title";
    private static final String COLUMN_EXERCISE_TOPIC_ID = "TopicId";

    // questions table column
    private static final String COLUMN_QUESTION_ID = "Id";
    private static final String COLUMN_QUESTION_CONTENT = "Content";
    private static final String COLUMN_QUESTION_ANSWER = "Answer";
    private static final String COLUMN_QUESTION_EXERCISE_ID = "ExerciseId";

    // CREATE TABLE query string
    private static final String CREATE_TOPICS_TABLE = "CREATE TABLE " + TABLE_TOPICS +
            "(" +
                COLUMN_TOPIC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOPIC_TITLE + " TEXT" +
            ")";
    private static final String CREATE_EXERCISES_TABLE = "CREATE TABLE " + TABLE_EXERCISES +
            "(" +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_TITLE + " TEXT, " +
                COLUMN_EXERCISE_TOPIC_ID + " INTEGER REFERENCES " + TABLE_TOPICS +
            ")";
    private static final String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS +
            "(" +
                COLUMN_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_QUESTION_CONTENT + " TEXT, " +
                COLUMN_QUESTION_ANSWER + " TEXT, " +
                COLUMN_QUESTION_EXERCISE_ID + " INTEGER REFERENCES " + TABLE_TOPICS +
            ")";

    private Context context;
    private static DatabaseHelper sInstance;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Called when the database is created for the FIRST time.
    // If a database already exists on disk with the same DATABASE_NAME, this method will NOT be called.
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TOPICS_TABLE);
        sqLiteDatabase.execSQL(CREATE_EXERCISES_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUESTIONS_TABLE);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TOPICS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        onCreate(sqLiteDatabase);
    }

    // Called when the database connection is being configured.
    // Configure database settings for things like foreign key support, write-ahead logging, etc.
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }
}
