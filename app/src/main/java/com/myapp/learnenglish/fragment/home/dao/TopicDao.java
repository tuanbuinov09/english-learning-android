package com.myapp.learnenglish.fragment.home.dao;

import android.database.sqlite.SQLiteOpenHelper;

import com.myapp.learnenglish.fragment.home.model.Topic;

import java.util.List;

public class TopicDao implements Dao<Topic> {
    private SQLiteOpenHelper sqLiteOpenHelper;

    public TopicDao(SQLiteOpenHelper sqLiteOpenHelper) {
        this.sqLiteOpenHelper = sqLiteOpenHelper;
    }

    @Override
    public Topic get(long id) {
        return null;
    }

    @Override
    public List<Topic> getAll() {
        return null;
    }

    @Override
    public void save(Topic topic) {

    }

    @Override
    public void update(Topic topic) {

    }

    @Override
    public void delete(Topic topic) {

    }
}
