package com.myapp.sqlite.dao;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.myapp.model.TranslationHistory;
import com.myapp.sqlite.DatabaseHelper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TranslationHistoryDao implements Dao<TranslationHistory> {
    private SQLiteDatabase db;
    private final SQLiteOpenHelper dbHelper;

    public TranslationHistoryDao(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void fillData() {
    }

    @Override
    public boolean insertOne(TranslationHistory translationHistory) {
        boolean result = true;
        db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.TABLE_TRANSLATION_HISTORY_ORIGINAL_TEXT, translationHistory.getOriginalText());
            cv.put(DatabaseHelper.TABLE_TRANSLATION_HISTORY_TRANSLATED_TEXT, translationHistory.getTranslatedText());
            cv.put(DatabaseHelper.TABLE_TRANSLATION_HISTORY_DATE, translationHistory.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            db.insert(DatabaseHelper.TABLE_TRANSLATION_HISTORY, null, cv);

            db.setTransactionSuccessful();
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
            Log.e("inser error", ex.getMessage());
        } finally {
            db.endTransaction();
        }
        db.close();

        return result;
    }

    @Override
    public boolean updateOne(TranslationHistory translationHistory) {
        boolean result = true;
        db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.TABLE_TRANSLATION_HISTORY_ORIGINAL_TEXT, translationHistory.getOriginalText());
            cv.put(DatabaseHelper.TABLE_TRANSLATION_HISTORY_TRANSLATED_TEXT, translationHistory.getTranslatedText());
            cv.put(DatabaseHelper.TABLE_TRANSLATION_HISTORY_DATE, translationHistory.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            db.update(DatabaseHelper.TABLE_TRANSLATION_HISTORY, cv, DatabaseHelper.TABLE_TRANSLATION_HISTORY_ID + " = ?", new String[]{String.valueOf(translationHistory.getId())});
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();

        return result;
    }

    @Override
    public boolean deleteOne(TranslationHistory translationHistory) {
        boolean result = true;
        db = dbHelper.getWritableDatabase();

        db.beginTransaction();
        try {
            db.delete(DatabaseHelper.TABLE_TRANSLATION_HISTORY, DatabaseHelper.TABLE_TRANSLATION_HISTORY_ID + " = ?", new String[]{String.valueOf(translationHistory.getId())});
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
        db.close();

        return result;
    }

    @Override
    public List<TranslationHistory> getAll() {
        List<TranslationHistory> receipts = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        String queryString = "SELECT * FROM " + DatabaseHelper.TABLE_TRANSLATION_HISTORY;
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String originalText = cursor.getString(1);
                String translatedText = cursor.getString(2);
                String date = cursor.getString(3);

                LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                TranslationHistory translationHistory = new TranslationHistory(id, originalText, translatedText, localDate);
                receipts.add(translationHistory);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return receipts;
    }

    @Override
    public TranslationHistory getOne(String id) {
        TranslationHistory translationHistory = null;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TRANSLATION_HISTORY, null, DatabaseHelper.TABLE_TRANSLATION_HISTORY_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            int translationHistoryId = cursor.getInt(0);
            String originalText = cursor.getString(1);
            String translatedText = cursor.getString(2);
            String date = cursor.getString(3);

            LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);

            translationHistory = new TranslationHistory(translationHistoryId, originalText, translatedText, localDate);
        }
        return translationHistory;
    }

    @Override
    @Deprecated
    public List<TranslationHistory> search(String keyword) {
        return null;
    }
}
