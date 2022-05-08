package com.myapp.dtbassethelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.myapp.model.EnWord;
import com.myapp.model.ExampleDetail;
import com.myapp.model.Meaning;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;


    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    public void open() {
        this.db = openHelper.getWritableDatabase();
    }

    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    public ArrayList<EnWord> getAllEnWord(){
        ArrayList<EnWord> list = new ArrayList<>();
Cursor cursor;
        cursor= db.rawQuery("select id, word, pronunciation from en_word",null);
        while (cursor.moveToNext()) {
            list.add(getOneEnWord(cursor.getInt(0)));
        }
        cursor.close();
        return list;
    }
    public ArrayList<EnWord> getAllEnWord_NoPopulate(){
        ArrayList<EnWord> list = new ArrayList<>();
        Cursor cursor;
        cursor= db.rawQuery("select id, word, pronunciation from en_word",null);
        while (cursor.moveToNext()) {
            list.add(new EnWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), getOneMeaningOfEnWord(cursor.getInt(0))));
        }
        cursor.close();
        return list;
    }
public ArrayList<EnWord> getAllEnWord_NoPopulateWithOffsetLimit(int offset, int limit){
        ArrayList<EnWord> list = new ArrayList<>();
        Cursor cursor;
        cursor= db.rawQuery("select id, word, pronunciation from en_word limit "+limit+" offset +"+offset,null);
        while (cursor.moveToNext()) {
            list.add(new EnWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2), getOneMeaningOfEnWord(cursor.getInt(0))));
        }
        cursor.close();
        return list;
    }
    public ArrayList<EnWord> getFakeSavedWord_NoPopulateWithOffsetLimit(int offset, int limit){
        ArrayList<EnWord> list = new ArrayList<>();
        Cursor cursor;
        cursor= db.rawQuery("select id, word, pronunciation from en_word limit "+limit+" offset +"+offset,null);
        while (cursor.moveToNext()) {
            list.add(new EnWord(cursor.getInt(0), cursor.getString(1), cursor.getString(2),getOneMeaningOfEnWord(cursor.getInt(0))));
        }
        cursor.close();
        return list;
    }

    public EnWord getOneEnWord(int id) {
        EnWord enWord = new EnWord();
        Cursor cursor;
        cursor = db.rawQuery("select * from en_word where id = " + id, new String[]{});
        while (cursor.moveToNext()) {
            enWord.setId(id);
            enWord.setWord(cursor.getString(1));
            enWord.setPronunciation(cursor.getString(2));
            enWord.setListMeaning(getAllMeaningOfEnWord(id));
        }
//        System.out.println("-----------"+enWord.toString());
        cursor.close();
        return enWord;
    }

    public ArrayList<Meaning> getOneMeaningOfEnWord(int enWordId) {
        ArrayList<Meaning> listMeaning = new ArrayList<Meaning>();
        Cursor cursor;
        cursor = db.rawQuery("select meaning from meaning where en_word_id = " + enWordId+" limit 1", new String[]{});
        while (cursor.moveToNext()) {
            Meaning meaning = new Meaning();
//            meaning.setId(cursor.getInt(0));
            meaning.setMeaning(cursor.getString(0));
            listMeaning.add(meaning);
            break;
        }

        cursor.close();
        return listMeaning;
    }

    public ArrayList<Meaning> getAllMeaningOfEnWord(int enWordId) {
        ArrayList<Meaning> listMeaning = new ArrayList<Meaning>();
        Cursor cursor;
        cursor = db.rawQuery("select meaningTB.id, part_of_speech.name, meaning from (select id, part_of_speech_id, meaning from meaning " +
                "where en_word_id = " + enWordId + ") as meaningTB inner join part_of_speech on part_of_speech.id = meaningTB.part_of_speech_id ", new String[]{});

        while (cursor.moveToNext()) {
            Meaning meaning = new Meaning();

            meaning.setId(cursor.getInt(0));
            meaning.setEnWordId(enWordId);
            meaning.setPartOfSpeechName(cursor.getString(1));
            meaning.setMeaning(cursor.getString(2));
            meaning.setListExampleDetails(getAllExampleDetailOfMeaning(cursor.getInt(0)));

            listMeaning.add(meaning);
        }
        cursor.close();
        return listMeaning;
    }
    public ArrayList<ExampleDetail> getAllExampleDetailOfMeaning(int meaning_id){
        ArrayList<ExampleDetail> listExampleDetail = new ArrayList<ExampleDetail>();
        Cursor cursor;
//        c = db.rawQuery("select example_detail.id, example, example_meaning from meaning inner join example_detail on meaning.id = example_detail.meaning_id where meaning_id = " + meaning_id, new String[]{});
        cursor = db.rawQuery("select example_detail.id, example, example_meaning from example_detail where meaning_id = " + meaning_id, new String[]{});

        while (cursor.moveToNext()) {
            ExampleDetail exampleDetail = new ExampleDetail();
            exampleDetail.setId(cursor.getInt(0));
            exampleDetail.setMeaningId(meaning_id);
            exampleDetail.setExample(cursor.getString(1));
            exampleDetail.setExampleMeaning(cursor.getString(2));

            listExampleDetail.add(exampleDetail);
        }
        cursor.close();
        return listExampleDetail;
    }
    public List<String> getWord() {
        List<String> list = new ArrayList<>();
        c = db.rawQuery("select * from en_word", new String[]{});
        while (c.moveToNext()) {
           String id = c.getColumnName(1);
           String word = c.getString(1);
            System.out.println("------------"+word);
            list.add(word);
        }
        return list;
    }
}
